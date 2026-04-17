package silence.simsool.lucentclient.mods.impl.hud;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class PotionEffectsMod extends Mod {

	public PotionEffectsMod() {
		super(
				"Potion Effects", "Customizable potion effect HUD.",
				"HUD",
				"potion, effect, status",
				LucentClientUtils.getModIcon("potion_effects")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(PotionEffectsMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Show Icons",
		description = "Displays the icons of active potion effects on the screen.",
		category = "General",
		priority = 2
	)
	public static boolean ShowIcons = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Show Duration",
		description = "Displays the remaining duration of potion effects on the screen.",
		category = "General",
		priority = 1
	)
	public static boolean ShowDuration = true;

}