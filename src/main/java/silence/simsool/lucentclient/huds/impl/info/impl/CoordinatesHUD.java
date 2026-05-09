package silence.simsool.lucentclient.huds.impl.info.impl;

import static silence.simsool.lucent.Lucent.mc;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix3x2fStack;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import silence.simsool.lucent.general.enums.Align;
import silence.simsool.lucent.general.enums.RenderType;
import silence.simsool.lucent.general.models.abstracts.LucentHUD;
import silence.simsool.lucent.general.utils.useful.UDisplay;
import silence.simsool.lucent.ui.utils.nvg.NVGRenderer;
import silence.simsool.lucentclient.mods.impl.hud.CoordinatesMod;

public class CoordinatesHUD extends LucentHUD {

	public CoordinatesHUD() {
		super("lucentclient_coordinates", CoordinatesMod.class, 0.00625f, 0.011111111f, 1.0f, Align.LEFT);
	}

	@Override
	public RenderType getRenderType() {
		return RenderType.MINECRAFT;
	}

	@Override
	public float getPreviewWidth() {
		List<String> lines = getLines(true);
		float maxW = 0;
		for (String line : lines) maxW = Math.max(maxW, mc.font.width(line));
		if (CoordinatesMod.ShowBackground) maxW += 8;
		return maxW * ((float) UDisplay.getGuiScale() / NVGRenderer.getStandardGuiScale());
	}

	@Override
	public float getPreviewHeight() {
		List<String> lines = getLines(true);
		float h = lines.size() * 10;
		if (CoordinatesMod.ShowBackground) h = lines.size() * 9 + 9;
		return h * ((float) UDisplay.getGuiScale() / NVGRenderer.getStandardGuiScale());
	}

	@Override
	public void draw(GuiGraphics guiGraphics) {
		if (isEditHudOpen || UDisplay.isDebugScreen()) return;
		render(guiGraphics, false);
	}

	@Override
	public void preview(GuiGraphics guiGraphics) {
		render(guiGraphics, true);
	}

	private void render(GuiGraphics graphics, boolean preview) {
		List<String> lines = getLines(preview);
		Matrix3x2fStack pose = graphics.pose();
		int sw = UDisplay.getGuiScaledWidth();
		int sh = UDisplay.getGuiScaledHeight();

		float rx = x * sw;
		float ry = y * sh;

		float maxW = 0;
		for (String line : lines) maxW = Math.max(maxW, mc.font.width(line));

		float scaledW = maxW * scale;

		if (alignment == Align.CENTER) rx -= (scaledW / 2f);
		else if (alignment == Align.RIGHT) rx -= scaledW;

		if (CoordinatesMod.ShowBackground) {
			float bw = maxW * scale + 8 * scale;
			float bh = lines.size() * 9 * scale + 9 * scale;
			float bx = rx - 4 * scale;
			float by = ry - 4.5f * scale;
			graphics.fill((int)bx, (int)by, (int)(bx + bw), (int)(by + bh), CoordinatesMod.BackgroundColor);
		}

		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			float lineW = mc.font.width(line);
			float lx = rx;
			
			if (alignment == Align.CENTER) lx += (maxW - lineW) * scale / 2f;
			else if (alignment == Align.RIGHT) lx += (maxW - lineW) * scale;
			
			pose.pushMatrix();
			pose.translate(lx, ry + i * 10 * scale);
			pose.scale(scale, scale);
			graphics.drawString(mc.font, line, 0, 0, CoordinatesMod.TextColor, CoordinatesMod.TextShadow);
			pose.popMatrix();
		}
	}

	private List<String> getLines(boolean preview) {
		List<String> lines = new ArrayList<>();
		BlockPos pos = preview ? new BlockPos(121, 78, -242) : (mc.player != null ? mc.player.blockPosition() : BlockPos.ZERO);
		
		String xStr = "X: " + pos.getX();
		String yStr = "Y: " + pos.getY();
		String zStr = "Z: " + pos.getZ();
		
		String mode = CoordinatesMod.ListMode;
		
		if (mode.equals("Vertical")) {
			if (CoordinatesMod.ShowX) lines.add(xStr);
			if (CoordinatesMod.ShowY) lines.add(yStr);
			if (CoordinatesMod.ShowZ) lines.add(zStr);
			if (CoordinatesMod.ShowCCounter) lines.add("C: " + getChunkStats());
			if (CoordinatesMod.ShowDirection) lines.add("Direction: " + getShortDirection());
			if (CoordinatesMod.ShowBiome) {
				String biome = getBiomeName();
				if (biome != null) lines.add("Biome: " + biome);
			}
		} else if (mode.equals("Horizontal")) {
			StringBuilder sb = new StringBuilder("(");
			boolean first = true;
			if (CoordinatesMod.ShowX) { sb.append(xStr); first = false; }
			if (CoordinatesMod.ShowY) { if (!first) sb.append(", "); sb.append(yStr); first = false; }
			if (CoordinatesMod.ShowZ) { if (!first) sb.append(", "); sb.append(zStr); first = false; }
			if (CoordinatesMod.ShowCCounter) { if (!first) sb.append(", "); sb.append("C: ").append(getChunkStats()); first = false; }
			if (CoordinatesMod.ShowDirection) { if (!first) sb.append(", "); sb.append(getShortDirection()); first = false; }
			sb.append(")");
			lines.add(sb.toString());
		} else if (mode.equals("Simple")) {
			lines.add("XYZ: " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ());
			if (CoordinatesMod.ShowCCounter) lines.add("C: " + getChunkStats());
			if (CoordinatesMod.ShowDirection) lines.add("Direction: " + getShortDirection());
			if (CoordinatesMod.ShowBiome) {
				String biome = getBiomeName();
				if (biome != null) lines.add("Biome: " + biome);
			}
		}
		
		return lines;
	}

	private String getChunkStats() {
		if (mc.levelRenderer == null) return "0/0";
		int rendered = mc.levelRenderer.countRenderedSections();
		int total = (int) mc.levelRenderer.getTotalSections();
		return String.format("%d/%d", rendered, total);
	}

	private String getShortDirection() {
		if (mc.player == null) return "S";
		float yaw = mc.player.getYRot() % 360;
		if (yaw < 0) yaw += 360;
		if (yaw >= 337.5 || yaw < 22.5) return "S";
		if (yaw >= 22.5 && yaw < 67.5) return "SW";
		if (yaw >= 67.5 && yaw < 112.5) return "W";
		if (yaw >= 112.5 && yaw < 157.5) return "NW";
		if (yaw >= 157.5 && yaw < 202.5) return "N";
		if (yaw >= 202.5 && yaw < 247.5) return "NE";
		if (yaw >= 247.5 && yaw < 292.5) return "E";
		if (yaw >= 292.5 && yaw < 337.5) return "SE";
		return "S";
	}

	private String getBiomeName() {
		if (mc.level == null || mc.player == null) return "Unknown";

		return (mc.level.getBiome(mc.player.blockPosition())
			.unwrapKey()
			.map(key -> {
				String path = key.identifier().getPath();
				String name = path.replace('_', ' ');
				if (name.length() > 0) return name.substring(0, 1).toUpperCase() + name.substring(1);
				return name;
			})
			.orElse("Unknown")
		);
	}

}