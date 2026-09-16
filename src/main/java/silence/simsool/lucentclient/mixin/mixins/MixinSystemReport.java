package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.SystemReport;

@Mixin(SystemReport.class)
public class MixinSystemReport {

	/**
	 * Prevents exception logging when process details fail to collect due to an OSHI error.
	 *
	 * @author SimSool
	 */
	@Inject(method = "putProcessDetails", at = @At("HEAD"), cancellable = true)
	private void cancelPutProcessDetails(CallbackInfo ci) {
		ci.cancel();
	}

}