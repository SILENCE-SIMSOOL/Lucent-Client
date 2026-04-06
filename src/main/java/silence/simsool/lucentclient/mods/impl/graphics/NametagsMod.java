package silence.simsool.lucentclient.mods.impl.graphics;

import java.awt.Color;

import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;

@ModConfig.CategoryPriority(name = "General", priority = 1000)
@ModConfig.CategoryPriority(name = "Background", priority = 900)
public class NametagsMod extends Mod {

	public NametagsMod() {
		super("Nametags", "Customizes player nametags.", "Graphics", "name, tag, nametag", "lucid:tag");
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Custom Nametags",
		description = "Enable custom nametags rendering.",
		category = "General",
		priority = 1000
	)
	public static boolean CustomNametags = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Text Shadow",
		description = "Renders shadow under the nametag text.",
		category = "General",
		parent = "customNametags",
		priority = 990
	)
	public static boolean TextShadow = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Show Background",
		description = "Whether to draw a background behind the nametag.",
		category = "Background",
		parent = "customNametags",
		priority = 900
	)
	public static boolean ShowBackground = true;

	@ModConfig(
		type = ConfigType.COLOR,
		name = "Background Color",
		description = "The color of the nametag background.",
		category = "Background",
		parent = "showBackground",
		priority = 890
	)
	public static Color BackgroundColor = new Color(0, 0, 0, 64);

}