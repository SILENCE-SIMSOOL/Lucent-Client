package silence.simsool.lucentclient.features.asyncparticles.fabric;

import silence.simsool.lucentclient.features.asyncparticles.Platform;
import silence.simsool.lucentclient.features.asyncparticles.compat.fabric.ModListHelperImpl;
import silence.simsool.lucentclient.features.asyncparticles.core.fabric.GameUtilImpl;
import net.minecraft.world.phys.AABB;

public class FabricPlatform implements Platform {

	public boolean isForge() {
		return false;
	}

	public boolean isClient() {
		return ModListHelperImpl.isClient();
	}

	public boolean isModLoaded(String modId) {
		return ModListHelperImpl.isModLoaded(modId);
	}

	@Override
	public boolean isForgeModLoaded(String modId) {
		return false;
	}

	@Override
	public boolean isFabricModLoaded(String modId) {
		return isModLoaded(modId);
	}

	public boolean versionCheck(String modId, String minInclusive, String maxExclusive) {
		return ModListHelperImpl.versionCheck(modId, minInclusive, maxExclusive);
	}

	public String versionToString(String modId) {
		return ModListHelperImpl.versionToString(modId);
	}

	public boolean isDevelopmentEnvironment() {
		return ModListHelperImpl.isDevelopmentEnvironment();
	}

	@Override
	public AABB infinityAABB() {
		return GameUtilImpl.infinityAABB();
	}

}