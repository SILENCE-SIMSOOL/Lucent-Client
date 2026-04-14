package silence.simsool.lucentclient.mods.impl.graphics;

import java.awt.Color;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

@ModConfig.CategoryPriority(name = "Outline", priority = 1000)
@ModConfig.CategoryPriority(name = "Fill", priority = 900)
public class BlockOverlayMod extends Mod {

	public BlockOverlayMod() {
		super(
				"Block Overlay", "Customizes the block selection overlay.",
				"Graphics",
				"",
				LucentClientUtils.getModIcon("block_overlay")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(BlockOverlayMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Custom Outline",
		description = "Enables custom outline rendering for selected blocks.",
		category = "Outline",
		priority = 1000
	)
	public static boolean CustomOutline = true;

	@ModConfig(
		type = ConfigType.COLOR,
		name = "Outline Color",
		description = "Color of the block outline.",
		category = "Outline",
		parent = "CustomOutline",
		priority = 990
	)
	public static Color OutlineColor = new Color(255, 255, 255, 100);

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "Outline Thickness",
		description = "Width of the block selection outline.",
		category = "Outline",
		parent = "CustomOutline",
		min = 0.5,
		max = 5.0,
		step = 0.1,
		priority = 980
	)
	public static float OutlineThickness = 2.0f;

//	@ModConfig(
//		type = ConfigType.SWITCH,
//		name = "Custom Fill",
//		description = "Enables custom fill rendering for selected blocks.",
//		category = "Fill",
//		priority = 900
//	)
//	public static boolean CustomFill = false;
//
//	@ModConfig(
//		type = ConfigType.COLOR,
//		name = "Fill Color",
//		description = "Color of the block faces.",
//		category = "Fill",
//		parent = "CustomFill",
//		priority = 890
//	)
//	public static Color FillColor = new Color(255, 255, 255, 50);

}
