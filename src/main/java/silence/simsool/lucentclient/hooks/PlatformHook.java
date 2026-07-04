package silence.simsool.lucentclient.hooks;

import net.fabricmc.loader.api.FabricLoader;
import silence.simsool.lucentclient.ducks.IPlatform;

public class PlatformHook implements IPlatform {

	@Override
	public String computeBlockstateCacheFieldName() {
		return FabricLoader.getInstance().getMappingResolver().mapFieldName("official",
				"net.minecraft.world.level.block.state.BlockBehaviour$BlockStateBase",
				// cache
				"cache",
				// AbstractBlockState.Cache
				"Lnet/minecraft/world/level/block/state/BlockBehaviour$BlockStateBase$Cache;"
		);
	}

}