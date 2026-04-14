package silence.simsool.lucentclient.mixin.mixins.scrollabletooltips;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.resources.Identifier;
import silence.simsool.lucentclient.mods.impl.utility.scrollabletooltips.ScrollableTooltipsMod;
import silence.simsool.lucentclient.mods.impl.utility.scrollabletooltips.utils.ScrollTracker;

@Mixin(value = GuiGraphics.class, priority = 1001)
public abstract class MixinGuiGraphics_ScrollableTooltips {

	@Shadow
	@Final
	private Matrix3x2fStack pose;

	@Inject(method = "renderTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;positionTooltip(IIIIII)Lorg/joml/Vector2ic;"))
	public void applyTracker(Font font, List<ClientTooltipComponent> lines, int xo, int yo, ClientTooltipPositioner positioner, @Nullable Identifier style, CallbackInfo ci) {
		ScrollTracker.unlock();
		ScrollTracker.update();
		ScrollTracker.setItem(lines);
	}

	@Inject(method = "renderTooltip", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix3x2fStack;pushMatrix()Lorg/joml/Matrix3x2fStack;"))
	private void editXY(Font font, List<ClientTooltipComponent> lines, int xo, int yo, ClientTooltipPositioner positioner, @Nullable Identifier style, CallbackInfo info, @Local(ordinal = 2) LocalIntRef x, @Local(ordinal = 3) LocalIntRef y) {
		if (!ScrollableTooltipsMod.MatrixMode) {
			x.set(x.get() + ScrollTracker.getXOffset());
			y.set(y.get() + ScrollTracker.getYOffset());

			if (ScrollableTooltipsMod.StartOnTop && !ScrollTracker.hasMoved()) {
				int originalY = y.get();
				if (y.get() < 4) {
					y.set(4);
					ScrollTracker.setInitialYOffset(4 - originalY);
				}
			}
		}
	}

	@Inject(method = "renderTooltip", at = @At("HEAD"))
	private void headMatrices(Font font, List<ClientTooltipComponent> lines, int xo, int yo, ClientTooltipPositioner positioner, @Nullable Identifier style, CallbackInfo info) {
		if (ScrollableTooltipsMod.MatrixMode) {
			this.pose.pushMatrix();
			this.pose.translate(ScrollTracker.getXOffset(), ScrollTracker.getYOffset());
		}
	}

	@Inject(method = "renderTooltip", at = @At("TAIL"))
	private void tailMatrices(Font font, List<ClientTooltipComponent> lines, int xo, int yo, ClientTooltipPositioner positioner, @Nullable Identifier style, CallbackInfo info) {
		if (ScrollableTooltipsMod.MatrixMode) this.pose.popMatrix();
	}

}