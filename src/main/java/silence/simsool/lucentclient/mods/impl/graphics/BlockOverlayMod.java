package silence.simsool.lucentclient.mods.impl.graphics;

import silence.simsool.lucent.general.utils.LucentCategory;

import static silence.simsool.lucent.Lucent.mc;

import java.awt.Color;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.events.impl.LucentEvent.RenderWorldEvent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucent.general.utils.render.Render3D;
import silence.simsool.lucentclient.utils.LucentClientUtils;

@ModConfig.CategoryPriority(name = "Outline", priority = 1000)
@ModConfig.CategoryPriority(name = "Fill", priority = 900)
public class BlockOverlayMod extends Mod {

	public BlockOverlayMod() {
		super(
				"lucent.config.lucentclient.blockoverlaymod.general.name", "lucent.config.lucentclient.blockoverlaymod.general.description",
				LucentCategory.GRAPHICS,
				"",
				LucentClientUtils.getModIcon("block_overlay")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(BlockOverlayMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.blockoverlaymod.property.customfill.name",
		description = "lucent.config.lucentclient.blockoverlaymod.property.customfill.description",
		category = "Fill",
		priority = 1000
	)
	public static boolean CustomFill = false;

	@ModConfig(
		type = ConfigType.COLOR,
		name = "lucent.config.lucentclient.blockoverlaymod.property.fillcolor.name",
		description = "lucent.config.lucentclient.blockoverlaymod.property.fillcolor.description",
		category = "Fill",
		parent = "CustomFill",
		priority = 990
	)
	public static Color FillColor = new Color(255, 255, 255, 50);

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.blockoverlaymod.property.customoutline.name",
		description = "lucent.config.lucentclient.blockoverlaymod.property.customoutline.description",
		category = "Outline",
		priority = 500
	)
	public static boolean CustomOutline = true;

	@ModConfig(
		type = ConfigType.COLOR,
		name = "lucent.config.lucentclient.blockoverlaymod.property.outlinecolor.name",
		description = "lucent.config.lucentclient.blockoverlaymod.property.outlinecolor.description",
		category = "Outline",
		parent = "CustomOutline",
		priority = 490
	)
	public static Color OutlineColor = new Color(255, 255, 255, 100);

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "lucent.config.lucentclient.blockoverlaymod.property.outlinethickness.name",
		description = "lucent.config.lucentclient.blockoverlaymod.property.outlinethickness.description",
		category = "Outline",
		parent = "CustomOutline",
		min = 0.5,
		max = 5.0,
		step = 0.1,
		priority = 480
	)
	public static float OutlineThickness = 2.0f;

	@Override
	public void onRenderWorld(RenderWorldEvent event) {
		HitResult hitResult = mc.hitResult;
		if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
			BlockHitResult blockHitResult = (BlockHitResult) hitResult;
			BlockPos pos = blockHitResult.getBlockPos();
			if (CustomFill) Render3D.drawSelectedBox(pos, FillColor);
			if (CustomOutline) Render3D.drawSelectedBoxLine(pos, OutlineColor, OutlineThickness);
		}
	}

}