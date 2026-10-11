package silence.simsool.lucentclient.mods.impl.graphics;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucent.general.utils.LucentCategory;

public class DropItemMod extends Mod {

	public DropItemMod() {
		super(
				"lucent.config.lucentclient.dropitemmod.general.name",
				"lucent.config.lucentclient.dropitemmod.general.description",
				LucentCategory.GRAPHICS,
				"drop, item, dropped, tweak, scale",
				null
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(DropItemMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.dropitemmod.property.staticitems.name",
		description = "lucent.config.lucentclient.dropitemmod.property.staticitems.description"
	)
	public static boolean StaticItems = false;

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "lucent.config.lucentclient.dropitemmod.property.dropstackcount.name",
		description = "lucent.config.lucentclient.dropitemmod.property.dropstackcount.description",
		min = 0, max = 64, step = 1
	)
	public static int DropStackCount = 0;

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "lucent.config.lucentclient.dropitemmod.property.itemscale.name",
		description = "lucent.config.lucentclient.dropitemmod.property.itemscale.description",
		min = 0.1, max = 3.0, step = 0.1
	)
	public static double ItemScale = 1.0;

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "lucent.config.lucentclient.dropitemmod.property.uhcoverlay.name",
		description = "lucent.config.lucentclient.dropitemmod.property.uhcoverlay.description",
		min = 0.0, max = 3.0, step = 0.1
	)
	public static double UHCOverlay = 0.0;
}