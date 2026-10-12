package silence.simsool.lucentclient.mixin.mixins;

import java.util.AbstractMap.SimpleImmutableEntry;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import silence.simsool.lucentclient.features.betterscreens.ScreenMouseHook;
import silence.simsool.lucentclient.mods.impl.graphics.LoadingScreenMod;
import silence.simsool.lucentclient.mods.impl.utility.ChattingMod;
import silence.simsool.lucentclient.mods.impl.utility.ZoomMod;

@Mixin(MouseHandler.class)
public class MixinMouseHandler {

	@Shadow
	@Final
	private Minecraft minecraft;
	@Shadow
	private double xpos;
	@Shadow
	private double ypos;
	@Shadow
	private double accumulatedDX;
	@Shadow
	private double accumulatedDY;
	@Shadow
	private boolean mouseGrabbed;

	@Inject(method = "grabMouse", at = @At("HEAD"))
	private void onGrabMouseHead(CallbackInfo ci) {
		if (LoadingScreenMod.isEnabled() && this.minecraft.isWindowActive() && this.mouseGrabbed) {
			KeyMapping.setAll();
		}
	}

	@Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
	private void onScroll(long handle, double xoffset, double yoffset, CallbackInfo ci) {
		if (ZoomMod.handleScroll(yoffset)) {
			ci.cancel();
			return;
		}
		if (ChattingMod.handlePeekScroll(yoffset)) {
			ci.cancel();
		}
	}

	@Inject(method = "turnPlayer", at = @At("HEAD"))
	private void onTurnPlayer(CallbackInfo ci) {
		if (ZoomMod.isEnabled() && ZoomMod.isZoomKeyDown()) {
			this.accumulatedDX *= ZoomMod.ZoomSensitivity;
			this.accumulatedDY *= ZoomMod.ZoomSensitivity;
		}
	}

	@Inject(method = "grabMouse", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MouseHandler;mouseGrabbed:Z", opcode = Opcodes.PUTFIELD))
	public void onLockCursor(CallbackInfo ci) {
		ScreenMouseHook.saveCursorOriginal(this.xpos, this.ypos);
	}

	@Definition(id = "grabOrReleaseMouse", method = "Lcom/mojang/blaze3d/platform/InputConstants;grabOrReleaseMouse(Lcom/mojang/blaze3d/platform/Window;IDD)V")
	@Definition(id = "getWindow", method = "Lnet/minecraft/client/Minecraft;getWindow()Lcom/mojang/blaze3d/platform/Window;")
	@Expression("grabOrReleaseMouse(@(?.?.getWindow()), ?, ?, ?)")
	@Inject(method = "grabMouse", at = @At("MIXINEXTRAS:EXPRESSION"))
	public void onLockCursorAfter(CallbackInfo ci) {
		ScreenMouseHook.saveCursorMiddle(this.xpos, this.ypos);
	}

	@Definition(id = "grabOrReleaseMouse", method = "Lcom/mojang/blaze3d/platform/InputConstants;grabOrReleaseMouse(Lcom/mojang/blaze3d/platform/Window;IDD)V")
	@Definition(id = "getWindow", method = "Lnet/minecraft/client/Minecraft;getWindow()Lcom/mojang/blaze3d/platform/Window;")
	@Expression("grabOrReleaseMouse(@(?.?.getWindow()), ?, ?, ?)")
	@Inject(method = "releaseMouse", at = @At("MIXINEXTRAS:EXPRESSION"))
	public void onUnlockCursor(CallbackInfo ci) {
		SimpleImmutableEntry<Double, Double> cursorPosition = ScreenMouseHook.loadCursor(this.xpos, this.ypos);
		if (cursorPosition == null) return;
		this.xpos = cursorPosition.getKey();
		this.ypos = cursorPosition.getValue();
	}
}