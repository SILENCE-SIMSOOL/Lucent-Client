package silence.simsool.lucentclient.features.betterscreens;

import java.util.AbstractMap.SimpleImmutableEntry;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.Minecraft;
import silence.simsool.lucentclient.mods.impl.utility.BetterScreensMod;

public class ScreenMouseHook {
	private static SimpleImmutableEntry<Double, Double> savedPositionedP1 = null;
	private static SavedPosition savedPosition = null;

	public static void saveCursorOriginal(double xPos, double yPos) {
		savedPositionedP1 = new SimpleImmutableEntry<>(xPos, yPos);
	}

	public static void saveCursorMiddle(double middleX, double middleY) {
		if (!BetterScreensMod.isEnabled() || !BetterScreensMod.DontResetCursor) return;
		if (savedPositionedP1 == null) return;
		SimpleImmutableEntry<Double, Double> cursorPos = savedPositionedP1;
		savedPosition = new SavedPosition(new SimpleImmutableEntry<>(middleX, middleY), cursorPos);
	}

	public static SimpleImmutableEntry<Double, Double> loadCursor(double middleX, double middleY) {
		if (!BetterScreensMod.isEnabled() || !BetterScreensMod.DontResetCursor) return null;
		long timer = BetterScreensMod.CursorResetTimer;
		SavedPosition lastPosition = (savedPosition != null && (timer == 0L || savedPosition.savedAt.passedTimeMillis() < timer)) ? savedPosition : null;
		savedPosition = null;

		if (lastPosition != null && Math.abs(lastPosition.middle.getKey() - middleX) < 1 && Math.abs(lastPosition.middle.getValue() - middleY) < 1) {
			InputConstants.releaseMouse(Minecraft.getInstance().getWindow(), lastPosition.cursor.getKey(), lastPosition.cursor.getValue());
			return lastPosition.cursor;
		}

		return null;
	}

	public static class SavedPosition {
		public final SimpleImmutableEntry<Double, Double> middle;
		public final SimpleImmutableEntry<Double, Double> cursor;
		public final TimeMark savedAt;

		public SavedPosition(SimpleImmutableEntry<Double, Double> middle, SimpleImmutableEntry<Double, Double> cursor) {
			this.middle = middle;
			this.cursor = cursor;
			this.savedAt = TimeMark.now();
		}
	}

	public record TimeMark(long timeMark) {
		public long passedTimeMillis() {
			return (timeMark == 0L) ? Long.MAX_VALUE : System.currentTimeMillis() - timeMark;
		}

		public static TimeMark now() {
			return new TimeMark(System.currentTimeMillis());
		}
	}
}