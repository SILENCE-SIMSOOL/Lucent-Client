package silence.simsool.lucentclient.mixin.mixins.packmanager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.packs.repository.Pack;
import silence.simsool.lucentclient.mods.impl.utility.packmanager.PackManagerMod;

@Mixin(Pack.class)
public abstract class MixinResourcePackProfile {

	@Inject(method = "isFixedPosition", at = @At("RETURN"), cancellable = true)
	private void isFixedPosition(CallbackInfoReturnable<Boolean> cir) {
		if (PackManagerMod.isEnabled() && PackManagerMod.ServerUnlocker) {
			cir.setReturnValue(false);
		}
	}

}