package silence.simsool.lucentclient.ui;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.screens.Screen;
import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.config.LucentConfig;
import silence.simsool.lucent.config.api.LucentAPI;
import silence.simsool.lucent.general.utils.useful.UDesktop;
import silence.simsool.lucent.general.utils.useful.UMouse;
import silence.simsool.lucent.general.utils.useful.UScreen;
import silence.simsool.lucent.ui.manager.LucentResourceManager;
import silence.simsool.lucent.ui.utils.skija.Fonts;
import silence.simsool.lucent.ui.utils.skija.SkijaRenderer;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class TitleBottomBarWidget {

	private static final float BTN_SIZE = 38.0f;
	private static final float BTN_GAP = 7.0f;
	private static final float CORNER_RADIUS = 8.0f;

	private static final String ICON_HOME     = "\uE88A";
	private static final String ICON_SETTINGS = "\uE8B8";
	private static final String ICON_GLOBAL   = "\uE894";
	private static final String ICON_CHAT     = "\uE0B7";
	private static final String ICON_REPLAY   = "\uE045";

	private record ButtonDef(String tooltip, String icon, boolean isDiscord, Runnable action) {}

	private static List<ButtonDef> getButtons(Screen screen) {
		List<ButtonDef> list = new ArrayList<>();

		// 1. Mod Settings (Home icon -> EditHUDScreen)
		list.add(new ButtonDef("Mod Settings", ICON_HOME, false, () -> {
			UScreen.setScreenMC(LucentAPI.createEditHUDScreen(Lucent.config));
		}));

		// 2. Preferences (Settings gear icon -> Preferences page)
		list.add(new ButtonDef("Preferences", ICON_SETTINGS, false, () -> {
			Screen cfg = LucentAPI.createConfigScreen(Lucent.config);
			try {
				Field f = cfg.getClass().getDeclaredField("currentSidebarPage");
				f.setAccessible(true);
				f.set(cfg, "Preferences");
			} catch (Throwable ignored) {}
			UScreen.setScreenMC(cfg);
		}));

		// 3. Homepage (Globe icon -> Browser)
		list.add(new ButtonDef("Homepage", ICON_GLOBAL, false, () -> {
			UDesktop.openBrowse("https://silencedev.kro.kr");
		}));

		// 4. Discord (Discord icon -> Browser)
		list.add(new ButtonDef("Discord", ICON_CHAT, true, () -> {
			UDesktop.openBrowse(LucentConfig.DISCORD_LINK);
		}));

		// 5. Replay (Flashback mod is loaded)
		if (LucentClientUtils.loadedFlashback) {
			list.add(new ButtonDef("Replay", ICON_REPLAY, false, () -> {
				LucentClientUtils.openFlashbackReplays(screen);
			}));
		}

		return list;
	}

	public static void render(Screen screen, float canvasW, float canvasH, float titleUiScale) {
		List<ButtonDef> buttons = getButtons(screen);
		int count = buttons.size();
		if (count == 0) return;

		float totalW = count * BTN_SIZE + (count - 1) * BTN_GAP;
		float startX = (canvasW - totalW) / 2.0f;
		float y = canvasH - 54.0f;

		float mx = UMouse.getNvgScaledX(titleUiScale);
		float my = UMouse.getNvgScaledY(titleUiScale);

		int hoveredIndex = -1;

		for (int i = 0; i < count; i++) {
			ButtonDef btn = buttons.get(i);
			float bx = startX + i * (BTN_SIZE + BTN_GAP);
			boolean hover = mx >= bx && mx <= bx + BTN_SIZE && my >= y && my <= y + BTN_SIZE;
			if (hover) hoveredIndex = i;

			int bg = hover ? 0xEE222634 : 0xDD14161E;
			int border = hover ? 0x775C8FFF : 0x22FFFFFF;
			int iconColor = hover ? 0xFFFFFFFF : 0xBBA6ADC0;

			SkijaRenderer.rect(bx, y, BTN_SIZE, BTN_SIZE, bg, CORNER_RADIUS);
			SkijaRenderer.outlineRect(bx, y, BTN_SIZE, BTN_SIZE, 1.0f, border, CORNER_RADIUS);

			float cx = bx + BTN_SIZE / 2.0f;
			float cy = y + BTN_SIZE / 2.0f;

			if (btn.isDiscord && LucentResourceManager.iconDiscord != null) {
				float is = hover ? 20.0f : 18.0f;
				float alpha = hover ? 1.0f : 0.55f;
				SkijaRenderer.image(LucentResourceManager.iconDiscord, cx - is / 2.0f, cy - is / 2.0f, is, is, 0.0f, alpha);
			} else {
				drawCenterIcon(btn.icon, cx, cy, iconColor, 20.0f);
			}
		}

		if (hoveredIndex != -1) {
			String text = buttons.get(hoveredIndex).tooltip;
			float textW = SkijaRenderer.textWidth(text, Fonts.PRETENDARD_MEDIUM, 12.0f);
			float tooltipW = textW + 16.0f;
			float tooltipH = 24.0f;

			float bx = startX + hoveredIndex * (BTN_SIZE + BTN_GAP);
			float tx = bx + (BTN_SIZE - tooltipW) / 2.0f;
			float ty = y - tooltipH - 7.0f;

			SkijaRenderer.rect(tx, ty, tooltipW, tooltipH, 0xF514161E, 6.0f);
			SkijaRenderer.outlineRect(tx, ty, tooltipW, tooltipH, 1.0f, 0x33FFFFFF, 6.0f);
			SkijaRenderer.text(text, tx + 8.0f, ty + 6.0f, Fonts.PRETENDARD_MEDIUM, 0xFFFFFFFF, 12.0f);
		}
	}

	public static boolean handleMouseClick(Screen screen, float canvasW, float canvasH, float titleUiScale, int button) {
		if (button != 0) return false;

		List<ButtonDef> buttons = getButtons(screen);
		int count = buttons.size();
		if (count == 0) return false;

		float totalW = count * BTN_SIZE + (count - 1) * BTN_GAP;
		float startX = (canvasW - totalW) / 2.0f;
		float y = canvasH - 54.0f;

		float mx = UMouse.getNvgScaledX(titleUiScale);
		float my = UMouse.getNvgScaledY(titleUiScale);

		for (int i = 0; i < count; i++) {
			float bx = startX + i * (BTN_SIZE + BTN_GAP);
			if (mx >= bx && mx <= bx + BTN_SIZE && my >= y && my <= y + BTN_SIZE) {
				buttons.get(i).action.run();
				return true;
			}
		}

		return false;
	}

	private static void drawCenterIcon(String icon, float cx, float cy, int color, float size) {
		float w = SkijaRenderer.textWidth(icon, Fonts.MATERIAL_ICONS_ROUND, size);
		SkijaRenderer.text(icon, cx - w / 2.0f, cy - size / 2.0f + 1.0f, Fonts.MATERIAL_ICONS_ROUND, color, size);
	}

}