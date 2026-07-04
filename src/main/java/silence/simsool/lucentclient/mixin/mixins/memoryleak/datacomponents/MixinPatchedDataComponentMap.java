package silence.simsool.lucentclient.mixin.mixins.memoryleak.datacomponents;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import silence.simsool.lucentclient.mods.impl.performance.memory.MemoryLeakFixMod;

import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;

@Mixin(PatchedDataComponentMap.class)
public class MixinPatchedDataComponentMap {

	@Shadow
	private Reference2ObjectMap<DataComponentType<?>, Optional<?>> patch;

	@Shadow
	private boolean copyOnWrite;

	@Inject(method = {"applyPatch(Lnet/minecraft/core/component/DataComponentPatch;)V", "restorePatch"}, at = @At("RETURN"))
	private void saveMemoryIfEmpty(CallbackInfo ci) {
		if (MemoryLeakFixMod.isEnabled() && MemoryLeakFixMod.DataComponentPatch) {
			if (patch.isEmpty()) {
				this.patch = Reference2ObjectMaps.emptyMap();
				this.copyOnWrite = true;
			}
		}
	}

	@Inject(method = {"set", "remove"}, at = @At("RETURN"))
	private void saveMemoryIfEmptyWithReturn(CallbackInfoReturnable<?> ci) {
		saveMemoryIfEmpty(ci);
	}

}