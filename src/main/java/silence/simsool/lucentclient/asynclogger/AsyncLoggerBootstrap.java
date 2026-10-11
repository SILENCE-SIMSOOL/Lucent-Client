package silence.simsool.lucentclient.asynclogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import silence.simsool.lucentclient.LucentClient;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.async.BasicAsyncLoggerContextSelector;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.impl.Log4jContextFactory;

public class AsyncLoggerBootstrap implements PreLaunchEntrypoint {

	private static boolean initialized = false;

	@Override
	public void onPreLaunch() {
		init();
	}

	public static synchronized void init() {
		if (initialized) {
			return;
		}
		initialized = true;

		try {
			if (System.getProperty("log4j2.asyncLoggerRingBufferSize") == null) {
				System.setProperty("log4j2.asyncLoggerRingBufferSize", "16384");
			}

			LoggerContext context = (LoggerContext) LogManager.getContext(false);
			Configuration configuration = context.getConfiguration();
			LoggerConfig originalRoot = configuration.getRootLogger();
			Map<String, Appender> originalAppenders = new HashMap<>(originalRoot.getAppenders());
			List<AppenderRef> originalAppenderRefs = new ArrayList<>(originalRoot.getAppenderRefs());

			BasicAsyncLoggerContextSelector selector = new BasicAsyncLoggerContextSelector();
			LogManager.setFactory(new Log4jContextFactory(selector));

			ConfigurationSource source = configuration.getConfigurationSource();
			if (source != null && source.getURI() != null) Configurator.reconfigure(source.getURI());
			else Configurator.reconfigure();

			LoggerConfig root = ((LoggerContext) LogManager.getContext(false)).getConfiguration().getRootLogger();
			for (Map.Entry<String, Appender> entry : originalAppenders.entrySet()) {
				if (!root.getAppenders().containsKey(entry.getKey())) {
					Level level = Level.ALL;
					Filter filter = null;
					for (AppenderRef ref : originalAppenderRefs) {
						if (ref.getRef().equals(entry.getKey())) {
							level = ref.getLevel() != null ? ref.getLevel() : Level.ALL;
							filter = ref.getFilter();
							break;
						}
					}
					root.addAppender(entry.getValue(), level, filter);
				}
			}

			LogManager.getLogger(LucentClient.NAME).info("Async logger initialized successfully.");
		} catch (Throwable t) {
			LogManager.getLogger(LucentClient.NAME).error("Failed to initialize async logger", t);
		}
	}

}