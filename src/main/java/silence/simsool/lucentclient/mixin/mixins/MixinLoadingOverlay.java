package silence.simsool.lucentclient.mixin.mixins;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LoadingOverlay;
import silence.simsool.lucentclient.mods.impl.graphics.LoadingScreenMod;

@Mixin(LoadingOverlay.class)
public abstract class MixinLoadingOverlay {

	@Shadow
	@Final
	private Minecraft minecraft;

	@Shadow
	private long fadeOutStart;

	@Shadow
	private boolean fadeIn;

	@Inject(method = "render", at = @At("TAIL"))
	private void onRenderTail(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
		if (LoadingScreenMod.isEnabled() && LoadingScreenMod.FastResourcePackLoading && this.fadeOutStart != -1) {
			this.minecraft.setOverlay(null);
		}
	}

	@Redirect(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/LoadingOverlay;fadeIn:Z", opcode = Opcodes.GETFIELD))
	private boolean onFadeIn(LoadingOverlay instance) {
		if (LoadingScreenMod.isEnabled() && LoadingScreenMod.FastResourcePackLoading) {
			return false;
		}
		return this.fadeIn;
	}

	@Inject(method = "isReadyToFadeOut", at = @At("RETURN"), cancellable = true)
	private void onIsReadyToFadeOut(CallbackInfoReturnable<Boolean> cir) {
		if (LoadingScreenMod.isEnabled() && LoadingScreenMod.FastResourcePackLoading) {
			cir.setReturnValue(true);
		}
	}

}