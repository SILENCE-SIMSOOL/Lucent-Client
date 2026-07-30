package silence.simsool.lucentclient.mixin.mixins;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.SafetyScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import silence.simsool.lucentclient.LucentClient;
import silence.simsool.lucentclient.ui.GameMenuButton;

@Mixin(TitleScreen.class)
public abstract class MixinTitleScreen extends Screen {

	@Shadow @Nullable private SplashRenderer splash;

	private static final Identifier LOGO_LOCATION = Identifier.parse("lucentclient:textures/logo.png");
	private static final int LOGO_SIZE = 114;

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
		this.splash = null;

		int btnWidth = 180;
		int btnHeight = 24;
		int startY = getLogoY() + LOGO_SIZE - 18;
		int spacing = 28;
		int gap = 6;
		int halfWidth = (btnWidth - gap) / 2;

		// Singleplayer
		this.addRenderableWidget(
			new GameMenuButton(this.width / 2 - btnWidth / 2, startY, btnWidth, btnHeight, Component.translatable("menu.singleplayer"), b -> {
				if (this.minecraft != null) {
					this.minecraft.setScreen(new SelectWorldScreen(this));
				}
			})
		);

		// Multiplayer
		this.addRenderableWidget(
			new GameMenuButton(this.width / 2 - btnWidth / 2, startY + spacing, btnWidth, btnHeight, Component.translatable("menu.multiplayer"), b -> {
				if (this.minecraft != null) {
					Screen nextScreen = this.minecraft.options.skipMultiplayerWarning
						? new JoinMultiplayerScreen(this)
						: new SafetyScreen(this);
					this.minecraft.setScreen(nextScreen);
				}
			})
		);

		// Options
		this.addRenderableWidget(
			new GameMenuButton(this.width / 2 - btnWidth / 2, startY + spacing * 2, halfWidth, btnHeight, Component.translatable("menu.options"), b -> {
				if (this.minecraft != null) {
					this.minecraft.setScreen(new OptionsScreen(this, this.minecraft.options));
				}
			})
		);

		// Quit Game
		this.addRenderableWidget(
			new GameMenuButton(this.width / 2 - btnWidth / 2 + halfWidth + gap, startY + spacing * 2, halfWidth, btnHeight, Component.translatable("menu.quit"), b -> {
				if (this.minecraft != null) {
					this.minecraft.stop();
				}
			})
		);
	}

	@WrapOperation(
		method = "render",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/LogoRenderer;renderLogo(Lnet/minecraft/client/gui/GuiGraphics;IF)V"),
		require = 0
	)
	private void cancelLogoRender(LogoRenderer instance, GuiGraphics graphics, int width, float alpha, Operation<Void> original) {
		// Suppress Minecraft Java Edition Logo
	}

	@WrapOperation(
		method = "render",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/SplashRenderer;render(Lnet/minecraft/client/gui/GuiGraphics;ILnet/minecraft/client/gui/Font;F)V"),
		require = 0
	)
	private void cancelSplashRender(SplashRenderer instance, GuiGraphics graphics, int width, Font font, float alpha, Operation<Void> original) {
		// Suppress Splash Text
	}

	@WrapOperation(
		method = "render",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)I"),
		require = 0
	)
	private int wrapVersionTextInt5(GuiGraphics graphics, Font font, String text, int x, int y, int color, Operation<Integer> original) {
		return original.call(graphics, font, "LucentClient v" + LucentClient.VERSION, x, y, color);
	}

	@WrapOperation(
		method = "render",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I"),
		require = 0
	)
	private int wrapVersionTextInt6(GuiGraphics graphics, Font font, String text, int x, int y, int color, boolean dropShadow, Operation<Integer> original) {
		return original.call(graphics, font, "LucentClient v" + LucentClient.VERSION, x, y, color, dropShadow);
	}

	@WrapOperation(
		method = "render",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)V"),
		require = 0
	)
	private void wrapVersionTextVoid5(GuiGraphics graphics, Font font, String text, int x, int y, int color, Operation<Void> original) {
		original.call(graphics, font, "LucentClient v" + LucentClient.VERSION, x, y, color);
	}

	@WrapOperation(
		method = "render",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)V"),
		require = 0
	)
	private void wrapVersionTextVoid6(GuiGraphics graphics, Font font, String text, int x, int y, int color, boolean dropShadow, Operation<Void> original) {
		original.call(graphics, font, "LucentClient v" + LucentClient.VERSION, x, y, 0xCCCCCCCC, dropShadow);
	}

	@Inject(method = "render", at = @At("TAIL"))
	private void onRenderTail(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
		int logoX = getLogoX();
		int logoY = getLogoY();
		graphics.blit(RenderPipelines.GUI_TEXTURED, LOGO_LOCATION, logoX, logoY, 0.0F, 0.0F, LOGO_SIZE, LOGO_SIZE, LOGO_SIZE, LOGO_SIZE);
	}

	private int getLogoX() {
		return this.width / 2 - LOGO_SIZE / 2;
	}

	private int getLogoY() {
		return this.height / 2 - 104;
	}

}