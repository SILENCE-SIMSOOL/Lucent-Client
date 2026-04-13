package silence.simsool.lucentclient.huds.impl;

import static silence.simsool.lucent.Lucent.mc;

import org.joml.Matrix3x2fStack;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import silence.simsool.lucent.general.enums.HUDAlignment;
import silence.simsool.lucent.general.enums.RenderType;
import silence.simsool.lucent.general.models.abstracts.LucentHUD;
import silence.simsool.lucent.general.utils.UDisplay;
import silence.simsool.lucent.ui.utils.nvg.NVGRenderer;
import silence.simsool.lucentclient.mods.impl.hud.ArmorStatusMod;

public class ArmorStatusHUD extends LucentHUD {

	public ArmorStatusHUD() {
		super("hud_lucentclient_armorstatus", ArmorStatusMod.class, 0.95f, 0.4f, 1.0f, HUDAlignment.LEFT);
	}

	@Override
	public RenderType getRenderType() {
		return RenderType.MINECRAFT;
	}

	@Override
	public float getPreviewWidth() {
		return 16 * ((float) UDisplay.getGuiScale() / NVGRenderer.getStandardGuiScale());
	}

	@Override
	public float getPreviewHeight() {
		int activeSlots = 0;
		if (ArmorStatusMod.DisplayHelmet) activeSlots++;
		if (ArmorStatusMod.DisplayChestplate) activeSlots++;
		if (ArmorStatusMod.DisplayLeggings) activeSlots++;
		if (ArmorStatusMod.DisplayBoots) activeSlots++;
		if (ArmorStatusMod.DisplayMainHand) activeSlots++;
		if (ArmorStatusMod.DisplayOffHand) activeSlots++;
		return 18 * activeSlots * ((float) UDisplay.getGuiScale() / NVGRenderer.getStandardGuiScale());
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

	private void render(GuiGraphics guiGraphics, boolean preview) {
		if (!preview && mc.player == null) return;

		int sw = mc.getWindow().getGuiScaledWidth();
		int sh = mc.getWindow().getGuiScaledHeight();

		float rx = x * sw;
		float ry = y * sh;

		if (alignment == HUDAlignment.CENTER) rx -= (getScaledWidth() / 2f);
		else if (alignment == HUDAlignment.RIGHT) rx -= getScaledWidth();

		int offset = 0;
		
		// 설정값과 슬롯 매핑 순회
		offset = renderSlot(guiGraphics, EquipmentSlot.HEAD, ArmorStatusMod.DisplayHelmet, rx, ry, offset, preview);
		offset = renderSlot(guiGraphics, EquipmentSlot.CHEST, ArmorStatusMod.DisplayChestplate, rx, ry, offset, preview);
		offset = renderSlot(guiGraphics, EquipmentSlot.LEGS, ArmorStatusMod.DisplayLeggings, rx, ry, offset, preview);
		offset = renderSlot(guiGraphics, EquipmentSlot.FEET, ArmorStatusMod.DisplayBoots, rx, ry, offset, preview);
		offset = renderSlot(guiGraphics, EquipmentSlot.MAINHAND, ArmorStatusMod.DisplayMainHand, rx, ry, offset, preview);
		offset = renderSlot(guiGraphics, EquipmentSlot.OFFHAND, ArmorStatusMod.DisplayOffHand, rx, ry, offset, preview);
	}

	private int renderSlot(GuiGraphics guiGraphics, EquipmentSlot slot, boolean isEnabled, float rx, float ry, int offset, boolean preview) {
		if (!isEnabled) return offset;

		ItemStack stack = preview ? getPlaceholder(slot) : mc.player.getItemBySlot(slot);
		if (stack.isEmpty()) return offset;

		Matrix3x2fStack pose = guiGraphics.pose();
		pose.pushMatrix();
		pose.translate(Math.round(rx), Math.round(ry + offset));
		pose.scale(scale, scale);

		guiGraphics.renderItem(stack, 0, 0);
		guiGraphics.renderItemDecorations(mc.font, stack, 0, 0);

		pose.popMatrix();
		return offset + (int)(18 * scale);
	}

	private ItemStack getPlaceholder(EquipmentSlot slot) {
		return switch (slot) {
			case HEAD -> Items.DIAMOND_HELMET.getDefaultInstance();
			case CHEST -> Items.DIAMOND_CHESTPLATE.getDefaultInstance();
			case LEGS -> Items.DIAMOND_LEGGINGS.getDefaultInstance();
			case FEET -> Items.DIAMOND_BOOTS.getDefaultInstance();
			case MAINHAND -> Items.DIAMOND_SWORD.getDefaultInstance();
			default -> Items.SHIELD.getDefaultInstance();
		};
	}
}