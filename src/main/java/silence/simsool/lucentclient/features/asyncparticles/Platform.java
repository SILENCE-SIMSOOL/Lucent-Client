package silence.simsool.lucentclient.features.asyncparticles;

import silence.simsool.lucentclient.features.asyncparticles.compat.Mappings;
import silence.simsool.lucentclient.features.asyncparticles.fabric.FabricPlatform;
import net.minecraft.world.phys.AABB;

public interface Platform {

	Platform PLATFORM = new FabricPlatform();

	boolean isForge();

	boolean isClient();

	boolean isModLoaded(String modId);

	boolean isForgeModLoaded(String modId);

	boolean isFabricModLoaded(String modId);

	boolean versionCheck(String modId, String minInclusive, String maxExclusive);

	String versionToString(String modId);

	boolean isDevelopmentEnvironment();

	AABB infinityAABB();

	Mappings.IMappings getMappings();

}