package silence.simsool.lucentclient.mods.impl.hud;

import silence.simsool.lucent.general.utils.LucentCategory;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class PotionEffectsMod extends Mod {

	public PotionEffectsMod() {
		super(
				"lucent.config.lucentclient.potioneffectsmod.general.name", "lucent.config.lucentclient.potioneffectsmod.general.description",
				LucentCategory.HUB,
				"potion, effect, status",
				LucentClientUtils.getModIcon("potion_effects")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(PotionEffectsMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.potioneffectsmod.property.showicons.name",
		description = "lucent.config.lucentclient.potioneffectsmod.property.showicons.description",
		priority = 2
	)
	public static boolean ShowIcons = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.potioneffectsmod.property.showduration.name",
		description = "lucent.config.lucentclient.potioneffectsmod.property.showduration.description",
		priority = 1
	)
	public static boolean ShowDuration = true;

}