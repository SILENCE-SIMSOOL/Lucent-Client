package silence.simsool.lucentclient.features.fastrender;

import java.lang.reflect.Field;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.fabricmc.loader.api.FabricLoader;

public class IrisCompat {

	public static boolean IRIS_LOADED = false;

	public static BooleanSupplier isRenderingLevel;
	public static Consumer<Boolean> renderWithExtendedVertexFormat;
	public static ThreadLocal<Boolean> skipExtension;

	@SuppressWarnings("unchecked")
	public static void init() {
		if (!FabricLoader.getInstance().isModLoaded("iris")) {
			return;
		}

		try {
			final Class<?> immediateStateClass = Class.forName("net.irisshaders.iris.vertices.ImmediateState");

			final Field isRenderingLevelField = immediateStateClass.getDeclaredField("isRenderingLevel");
			isRenderingLevelField.setAccessible(true);
			isRenderingLevel = () -> {
				try {
					return isRenderingLevelField.getBoolean(null);
				} catch (IllegalAccessException e) {
					return false;
				}
			};

			final Field renderWithExtendedVertexFormatField = immediateStateClass.getDeclaredField("renderWithExtendedVertexFormat");
			renderWithExtendedVertexFormatField.setAccessible(true);
			renderWithExtendedVertexFormat = val -> {
				try {
					renderWithExtendedVertexFormatField.setBoolean(null, val);
				} catch (IllegalAccessException ignored) {}
			};

			final Field skipExtensionField = immediateStateClass.getDeclaredField("skipExtension");
			skipExtensionField.setAccessible(true);
			skipExtension = (ThreadLocal<Boolean>) skipExtensionField.get(null);

			IRIS_LOADED = true;
		} catch (Throwable ignored) {}
	}

}