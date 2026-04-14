package silence.simsool.lucentclient.mods.impl.utility.scrollabletooltips.utils;

import java.util.List;

import org.apache.commons.lang3.mutable.MutableDouble;
import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.util.Mth;
import silence.simsool.lucent.general.utils.UDisplay;
import silence.simsool.lucentclient.mixin.accessors.AccessorOrderedTextTooltipComponent;
import silence.simsool.lucentclient.mods.impl.utility.scrollabletooltips.ScrollableTooltipsMod;

public class ScrollTracker {
	private static List<ClientTooltipComponent> currentItem;

	private static long unlockTime = System.currentTimeMillis();
	private static final long RELOCK_AT = 100;

	private static double currentXOffset = 0;
	private static double currentYOffset = 0;

	private static double trueXOffset = 0;
	private static double trueYOffset = 0;

	public static int scrollSize = 10;
	public static int scrollSizeKeyboard = 5;
	public static double smoothnessModifier = 0.25;

	private static boolean moved = false;

	public static void update() {
		currentXOffset += (trueXOffset - currentXOffset) * smoothnessModifier;
		currentYOffset += (trueYOffset - currentYOffset) * smoothnessModifier;

		Window window = UDisplay.getWindow();

		if (ScrollableTooltipsMod.UseWASD) {
			if (InputConstants.isKeyDown(window, GLFW.GLFW_KEY_W)) ScrollTracker.scrollUp(scrollSizeKeyboard);
			else if (InputConstants.isKeyDown(window, GLFW.GLFW_KEY_S)) ScrollTracker.scrollDown(scrollSizeKeyboard);
			if (InputConstants.isKeyDown(window, GLFW.GLFW_KEY_A)) ScrollTracker.scrollLeft(scrollSizeKeyboard);
			else if (InputConstants.isKeyDown(window, GLFW.GLFW_KEY_D)) ScrollTracker.scrollRight(scrollSizeKeyboard);
		}

		if (ScrollableTooltipsMod.moveUp.isKeyDown()) {
			if (ScrollableTooltipsMod.horizontal.isKeyDown() || (ScrollableTooltipsMod.UseLShift && InputConstants.isKeyDown(window, GLFW.GLFW_KEY_LEFT_SHIFT))) ScrollTracker.scrollLeft(scrollSizeKeyboard);
			else ScrollTracker.scrollUp(scrollSizeKeyboard);
		}
		else if (ScrollableTooltipsMod.moveDown.isKeyDown()) {
			if (ScrollableTooltipsMod.horizontal.isKeyDown() || (ScrollableTooltipsMod.UseLShift && InputConstants.isKeyDown(window, GLFW.GLFW_KEY_LEFT_SHIFT))) ScrollTracker.scrollRight(scrollSizeKeyboard);
			else ScrollTracker.scrollDown(scrollSizeKeyboard);
		}
		else if (ScrollableTooltipsMod.reset.isKeyDown()) ScrollTracker.reset();
	}

	public static int getXOffset() {
		MutableDouble convenientInjectionPoint = new MutableDouble(currentXOffset);
		return Mth.floor(convenientInjectionPoint.doubleValue());
	}

	public static int getYOffset() {
		MutableDouble convenientInjectionPoint = new MutableDouble(currentYOffset);
		return Mth.floor(convenientInjectionPoint.doubleValue());
	}

	public static void setInitialYOffset(int offset) {
		trueYOffset += offset;
		currentYOffset = trueYOffset;
	}

	public static void scrollUp() {
		scrollUp(scrollSize);
		moved = true;
	}

	public static void scrollUp(int amount) {
		if (!isLocked()) trueYOffset -= amount;
		moved = true;
	}

	public static void scrollDown() {
		scrollDown(scrollSize);
		moved = true;
	}

	public static void scrollDown(int amount) {
		if (!isLocked()) trueYOffset += amount;
		moved = true;
	}

	public static void scrollLeft() {
		scrollLeft(scrollSize);
		moved = true;
	}

	public static void scrollLeft(int amount) {
		if (!isLocked()) trueXOffset -= amount;
		moved = true;
	}

	public static void scrollRight() {
		scrollRight(scrollSize);
		moved = true;
	}

	public static void scrollRight(int amount) {
		if (!isLocked()) trueXOffset += amount;
		moved = true;
	}

	private static void resetScroll() {
		currentXOffset = 0;
		currentYOffset = 0;
		trueXOffset = 0;
		trueYOffset = 0;
		moved = false;
	}

	private static boolean isEqual(List<ClientTooltipComponent> item1, List<ClientTooltipComponent> item2) {
		if (item1 == null || item2 == null || item1.size() != item2.size()) return false;

		for (int i = 0; i < item1.size(); ++i) {
			if (item1.get(i) instanceof ClientTextTooltip && !(item2.get(i) instanceof ClientTextTooltip)) return false;
			if (item2.get(i) instanceof ClientTextTooltip && !(item1.get(i) instanceof ClientTextTooltip)) return false;
			if (!(item1.get(i) instanceof ClientTextTooltip) && !(item2.get(i) instanceof ClientTextTooltip)) continue;

			AccessorOrderedTextTooltipComponent accessible1 = (AccessorOrderedTextTooltipComponent) item1.get(i);
			AccessorOrderedTextTooltipComponent accessible2 = (AccessorOrderedTextTooltipComponent) item2.get(i);

			String text1 = OrderedTextReader.read(accessible1.getText());
			String text2 = OrderedTextReader.read(accessible2.getText());
			if (!text1.equals(text2)) return false;
		}
		return true;
	}

	public static boolean isLocked() {
		long difference = System.currentTimeMillis() - unlockTime;
		return difference > RELOCK_AT;
	}

	public static void reset() {
		resetScroll();
		currentItem = null;
	}

	public static void setItem(List<ClientTooltipComponent> item) {
		if (!isEqual(currentItem, item)) {
			resetScroll();
			currentItem = item;
		}
	}

	public static void unlock() {
		if (ScrollableTooltipsMod.ResetOnUnlock && isLocked()) resetScroll();
		unlockTime = System.currentTimeMillis();
	}

	public static boolean hasMoved() {
		return moved;
	}
}