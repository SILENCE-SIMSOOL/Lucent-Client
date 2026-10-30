package silence.simsool.lucentclient.features.asyncparticles.core.particle.tick;

import com.mojang.logging.LogUtils;
import silence.simsool.lucentclient.features.asyncparticles.util.AsyncParticleWorkerThread;
import org.slf4j.Logger;

import java.util.concurrent.ForkJoinPool;

public class AsyncTickerThread extends AsyncParticleWorkerThread {

	private static final Logger LOGGER = LogUtils.getLogger();

	public AsyncTickerThread(ForkJoinPool forkJoinPool) {
		super(forkJoinPool);
	}

	protected void onTermination(Throwable throwable) {
		if (throwable != null) {
			LOGGER.warn("{} died", this.getName(), throwable);
		} else {
			LOGGER.debug("{} shutdown", this.getName());
		}

		super.onTermination(throwable);
	}

}