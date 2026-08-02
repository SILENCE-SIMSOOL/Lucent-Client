package silence.simsool.lucentclient.hooks;

import static silence.simsool.lucent.Lucent.mc;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.SafetyScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import silence.simsool.lucent.general.utils.useful.UDisplay;
import silence.simsool.lucent.general.utils.useful.UMouse;
import silence.simsool.lucent.general.utils.useful.UScreen;
import silence.simsool.lucent.ui.font.LucentFont;
import silence.simsool.lucent.ui.utils.nvg.Fonts;
import silence.simsool.lucent.ui.utils.nvg.NVGRenderer;
import silence.simsool.lucentclient.LucentClient;

public class TitleScreenHook {

	private static final Identifier LOGO_LOCATION = Identifier.parse("lucentclient:textures/logo.png");
	public static int LOGO_SIZE = 140;
	public static float LOGO_OFFSET_Y = -140.0f;

	public static void renderLogo(GuiGraphics graphics, int width, int height) {
		LOGO_SIZE = 120;
		LOGO_OFFSET_Y = -104f;
		int logoX = width / 2 - LOGO_SIZE / 2;
		int logoY = (int) (height / 2.0f + LOGO_OFFSET_Y);
		graphics.blit(RenderPipelines.GUI_TEXTURED, LOGO_LOCATION, logoX, logoY, 0.0F, 0.0F, LOGO_SIZE, LOGO_SIZE, LOGO_SIZE, LOGO_SIZE);
	}

	public static void renderNanoVGGUI(Screen screen, float titleUiScale) {
		NVGRenderer.push();

		float standardScale = NVGRenderer.getStandardGuiScale();
		if (standardScale <= 0.01f) standardScale = 1.0f;
		float nvgScale = standardScale * titleUiScale;
		NVGRenderer.scale(nvgScale, nvgScale);

		float screenW = (float) UDisplay.getScreenWidth() / standardScale;
		float screenH = (float) UDisplay.getScreenHeight() / standardScale;
		float canvasW = screenW / titleUiScale;
		float canvasH = screenH / titleUiScale;

		float logoY = canvasH / 2.0f - 140.0f;

		// 1. Centered Title Text
		drawDirectCenterText("LUCENT CLIENT", canvasW / 2.0f, logoY + 90.0f, Fonts.PRETENDARD_SEMIBOLD, 0xFFFFFFFF, 24.0f);

		// 2. Centered Menu Buttons
		int btnW = 340;
		int btnH = 46;
		float startY = logoY + 132.0f;
		int gap = 10;
		int halfW = (btnW - gap) / 2;

		drawNanoVGButton(screen, canvasW / 2.0f - btnW / 2.0f, startY, btnW, btnH, "Singleplayer", 18.0f, titleUiScale);
		drawNanoVGButton(screen, canvasW / 2.0f - btnW / 2.0f, startY + btnH + gap, btnW, btnH, "Multiplayer", 18.0f, titleUiScale);
		drawNanoVGButton(screen, canvasW / 2.0f - btnW / 2.0f, startY + (btnH + gap) * 2, halfW, btnH, "Options", 18.0f, titleUiScale);
		drawNanoVGButton(screen, canvasW / 2.0f - btnW / 2.0f + halfW + gap, startY + (btnH + gap) * 2, halfW, btnH, "Quit Game", 18.0f, titleUiScale);

		// 3. Bottom-Left Version Info
		NVGRenderer.text("LucentClient v" + LucentClient.VERSION, 20, canvasH - 24.0f, Fonts.PRETENDARD, 0x88FFFFFF, 16.0f);

		NVGRenderer.pop();
	}

	public static boolean handleMouseClick(Screen screen, float titleUiScale, int button) {
		if (button != 0) return false;

		float standardScale = NVGRenderer.getStandardGuiScale();
		if (standardScale <= 0.01f) standardScale = 1.0f;
		float screenH = (float) UDisplay.getScreenHeight() / standardScale;
		float canvasH = screenH / titleUiScale;
		float screenW = (float) UDisplay.getScreenWidth() / standardScale;
		float canvasW = screenW / titleUiScale;

		float logoY = canvasH / 2.0f - 140.0f;
		int btnW = 340;
		int btnH = 46;
		float startY = logoY + 130.0f;
		int gap = 10;
		int halfW = (btnW - gap) / 2;

		if (isHovered(canvasW / 2.0f - btnW / 2.0f, startY, btnW, btnH, titleUiScale)) {
			UScreen.setScreen((Screen) new SelectWorldScreen(screen));
			return true;
		}

		if (isHovered(canvasW / 2.0f - btnW / 2.0f, startY + btnH + gap, btnW, btnH, titleUiScale)) {
			Screen nextScreen = (Screen) (mc.options.skipMultiplayerWarning
				? new JoinMultiplayerScreen(screen)
				: new SafetyScreen(screen));
			UScreen.setScreen(nextScreen);
			return true;
		}

		if (isHovered(canvasW / 2.0f - btnW / 2.0f, startY + (btnH + gap) * 2, halfW, btnH, titleUiScale)) {
			UScreen.setScreen((Screen) new OptionsScreen(screen, mc.options));
			return true;
		}

		if (isHovered(canvasW / 2.0f - btnW / 2.0f + halfW + gap, startY + (btnH + gap) * 2, halfW, btnH, titleUiScale)) {
			if (mc != null) mc.stop();
			return true;
		}

		return false;
	}

	private static boolean isHovered(float x, float y, float w, float h, float titleUiScale) {
		float mx = UMouse.getNvgScaledX(titleUiScale);
		float my = UMouse.getNvgScaledY(titleUiScale);
		return mx >= x && mx <= x + w && my >= y && my <= y + h;
	}

	private static void drawDirectCenterText(String text, float centerX, float y, LucentFont font, int color, float fontSize) {
		float width = NVGRenderer.textWidth(text, font, fontSize);
		float drawX = centerX - (width / 2.0f);
		NVGRenderer.text(text, drawX, y, font, color, fontSize);
	}

	private static void drawNanoVGButton(Screen screen, float x, float y, float w, float h, String text, float fontSize, float titleUiScale) {
		boolean hover = isHovered(x, y, w, h, titleUiScale);

		int bg = hover ? 0xDD222630 : 0xAA111318;
		int border = hover ? 0x99FFFFFF : 0x33FFFFFF;
		int textColor = hover ? 0xFFFFFFFF : 0xDDCCCCCC;

		NVGRenderer.rect(x, y, w, h, bg, 8.0f);
		NVGRenderer.outlineRect(x, y, w, h, 1.0f, border, 8.0f);
		drawDirectCenterText(text, x + w / 2.0f, y + (h - fontSize) / 2.0f + 2.0f, Fonts.PRETENDARD_MEDIUM, textColor, fontSize);
	}

}