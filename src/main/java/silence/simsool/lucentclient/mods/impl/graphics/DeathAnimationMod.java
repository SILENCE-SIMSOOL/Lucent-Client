package silence.simsool.lucentclient.mods.impl.graphics;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class DeathAnimationMod extends Mod {

	public DeathAnimationMod() {
		super(
				"Death Animation", "Hides death animation of entities",
				"Graphics",
				"entity, hide",
				LucentClientUtils.getModIcon("death_animation")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(DeathAnimationMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Hide Entity",
		description = "Hides death animation of entities.",
		category = "General"
	)
	public static boolean HideEntityDeathAnimation = false;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Hide Armor Stands",
		description = "Hides death animation of armor stands.",
		category = "General"
	)
	public static boolean HideArmorStandDeathAnimation = false;

}
