package silence.simsool.lucentclient.mods.impl.graphics;

import silence.simsool.lucent.general.utils.LucentCategory;

import java.awt.Color;

import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

@ModConfig.CategoryPriority(name = "Background", priority = 900)
public class NametagsMod extends Mod {

	public NametagsMod() {
		super(
				"lucent.config.lucentclient.nametagsmod.general.name", "lucent.config.lucentclient.nametagsmod.general.description",
				LucentCategory.GRAPHICS,
				"name, tag",
				LucentClientUtils.getModIcon("nametags")
		);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.nametagsmod.property.customnametags.name",
		description = "lucent.config.lucentclient.nametagsmod.property.customnametags.description",
		priority = 1000
	)
	public static boolean CustomNametags = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.nametagsmod.property.textshadow.name",
		description = "lucent.config.lucentclient.nametagsmod.property.textshadow.description",
		parent = "CustomNametags",
		priority = 990
	)
	public static boolean TextShadow = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.nametagsmod.property.showbackground.name",
		description = "lucent.config.lucentclient.nametagsmod.property.showbackground.description",
		category = "Background",
		parent = "CustomNametags",
		priority = 900
	)
	public static boolean ShowBackground = true;

	@ModConfig(
		type = ConfigType.COLOR,
		name = "lucent.config.lucentclient.nametagsmod.property.backgroundcolor.name",
		description = "lucent.config.lucentclient.nametagsmod.property.backgroundcolor.description",
		category = "Background",
		parent = "ShowBackground",
		priority = 890
	)
	public static Color BackgroundColor = new Color(0, 0, 0, 64);

}