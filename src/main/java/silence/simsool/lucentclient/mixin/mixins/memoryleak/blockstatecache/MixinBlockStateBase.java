package silence.simsool.lucentclient.mixin.mixins.memoryleak.blockstatecache;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import silence.simsool.lucentclient.mods.impl.performance.memory.MemoryLeakFixMod;
import silence.simsool.lucentclient.mods.impl.performance.memory.impl.BlockStateCacheImpl;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class MixinBlockStateBase {

	@Shadow
	protected abstract BlockState asState();

	@Inject(method = "initCache", at = @At("HEAD"))
	public void cacheStateHead(CallbackInfo ci) {
		if (MemoryLeakFixMod.isEnabled() && MemoryLeakFixMod.BlockstateCacheDeduplication) {
			BlockStateCacheImpl.deduplicateCachePre(asState());
		}
	}

	@Inject(method = "initCache", at = @At("TAIL"))
	public void cacheStateTail(CallbackInfo ci) {
		if (MemoryLeakFixMod.isEnabled() && MemoryLeakFixMod.BlockstateCacheDeduplication) {
			BlockStateCacheImpl.deduplicateCachePost(asState());
		}
	}

}