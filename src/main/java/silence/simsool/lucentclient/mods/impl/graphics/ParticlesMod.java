package silence.simsool.lucentclient.mods.impl.graphics;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.mods.impl.utility.AlwaysSprintMod;
import silence.simsool.lucentclient.utils.LucentClientUtils;

@ModConfig.CategoryPriority(name = "General", priority = 1000)
public class ParticlesMod extends Mod {

	public ParticlesMod() {
		super(
				"Particles", "Manage particle rendering.",
				"Graphics",
				"break, block, explode, explosion",
				LucentClientUtils.getModIcon("particles")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(AlwaysSprintMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Disable Breaking",
		description = "Disables block breaking particles.",
		category = "General",
		priority = 1000
	)
	public static boolean DisableBreakingParticles = false;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Disable Explosion",
		description = "Disables explosion particles.",
		category = "General",
		priority = 990
	)
	public static boolean DisableExplosionParticles = false;

}
