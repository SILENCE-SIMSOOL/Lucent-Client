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
import silence.simsool.lucentclient.mods.impl.hud.ArmorStatusMod;

public class ArmorStatusHUD extends LucentHUD {

	public ArmorStatusHUD() {
		super("armor_status", ArmorStatusMod.class, 0.95f, 0.4f, 1.0f, HUDAlignment.RIGHT);
	}

	@Override
	public RenderType getRenderType() {
		return RenderType.MINECRAFT;
	}

	@Override
	public void draw(GuiGraphics guiGraphics) {
		if (mc.player == null) return;
		render(guiGraphics, false);
	}

	@Override
	public void preview(GuiGraphics guiGraphics) {
		render(guiGraphics, true);
	}

	private void render(GuiGraphics guiGraphics, boolean preview) {
		float rx = getRenderX();
		float ry = getRenderY();
		float gs = scale;
		
		EquipmentSlot[] slots = { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND };
		
		int offset = 0;
		for (EquipmentSlot slot : slots) {
			ItemStack stack = preview ? getPlaceholder(slot) : mc.player.getItemBySlot(slot);
			if (stack.isEmpty()) continue;

			
			// Items are 16x16. We scale the GuiGraphics matrix.
			Matrix3x2fStack pose = guiGraphics.pose();
			pose.pushMatrix();
			pose.translate(rx, ry + offset);
			pose.scale(gs, gs);
			guiGraphics.renderItem(stack, 0, 0);
			guiGraphics.renderItemDecorations(mc.font, stack, 0, 0);
			pose.popMatrix();
			offset += 18 * gs;
		}
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

	@Override
	public float getPreviewWidth() {
		return 16;
	}

	@Override
	public float getPreviewHeight() {
		return 18 * 6;
	}
}