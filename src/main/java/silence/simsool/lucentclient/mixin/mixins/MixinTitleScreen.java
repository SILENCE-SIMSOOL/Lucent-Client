package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import silence.simsool.lucent.general.utils.useful.UDisplay;
import silence.simsool.lucent.ui.utils.nvg.NVGPIPRenderer;
import silence.simsool.lucent.ui.utils.nvg.NVGRenderer;
import silence.simsool.lucentclient.hooks.TitleScreenHook;

@Mixin(TitleScreen.class)
public abstract class MixinTitleScreen extends Screen {

	private float titleUiScale = 1.0f;

	protected MixinTitleScreen(Component title) {
		super(title);
	}

	@Inject(method = "realmsNotificationsEnabled", at = @At("HEAD"), cancellable = true)
	private void onRealmsNotificationsEnabled(CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(false);
	}

	@Inject(method = "init", at = @At("RETURN"))
	private void onInit(CallbackInfo ci) {
		this.clearWidgets();
		calculateUiScale();
	}

	private void calculateUiScale() {
		float standardScale = NVGRenderer.getStandardGuiScale();
		if (standardScale <= 0.01f) standardScale = 1.0f;

		float screenW = (float) UDisplay.getScreenWidth() / standardScale;
		float screenH = (float) UDisplay.getScreenHeight() / standardScale;

		float scaleX = (screenW - 40.0f) / 1100.0f;
		float scaleY = (screenH - 40.0f) / 680.0f;

		this.titleUiScale = Math.min(1.0f, Math.min(scaleX, scaleY));
		if (this.titleUiScale < 0.95f) this.titleUiScale = 0.95f;
	}

	@WrapOperation(
		method = "extractRenderState",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/LogoRenderer;extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IF)V"),
		require = 0
	)
	private void cancelLogoExtractRenderState(LogoRenderer instance, GuiGraphicsExtractor graphics, int width, float alpha, Operation<Void> original) {
	}

	@WrapOperation(
		method = "extractRenderState",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/SplashRenderer;extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;ILnet/minecraft/client/gui/Font;F)V"),
		require = 0
	)
	private void cancelSplashExtractRenderState(SplashRenderer instance, GuiGraphicsExtractor graphics, int width, Font font, float alpha, Operation<Void> original) {
	}

	@WrapOperation(
		method = "extractRenderState",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;text(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)V"),
		require = 0
	)
	private void wrapVersionText(GuiGraphicsExtractor graphics, Font font, String text, int x, int y, int color, Operation<Void> original) {
	}

	@Inject(method = "extractRenderState", at = @At("TAIL"), remap = false)
	private void onExtractRenderStateTail(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
		calculateUiScale();
		TitleScreenHook.renderLogo(graphics, this.width, this.height);
		NVGPIPRenderer.draw(graphics, 0, 0, this.width, this.height, () -> TitleScreenHook.renderNanoVGGUI(this, this.titleUiScale));
	}

	@Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
	private void onMouseClickedHead(MouseButtonEvent event, boolean isDoubleClick, CallbackInfoReturnable<Boolean> cir) {
		calculateUiScale();
		if (TitleScreenHook.handleMouseClick(this, this.titleUiScale, event.button())) {
			cir.setReturnValue(true);
		}
	}

}