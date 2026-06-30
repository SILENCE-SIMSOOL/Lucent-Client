package silence.simsool.lucentclient.mods.impl.graphics;

import silence.simsool.lucent.general.utils.LucentCategory;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class DeathAnimationMod extends Mod {

	public DeathAnimationMod() {
		super(
				"lucent.config.lucentclient.deathanimationmod.general.name", "lucent.config.lucentclient.deathanimationmod.general.description",
				LucentCategory.GRAPHICS,
				"death, animation, entity, hide",
				LucentClientUtils.getModIcon("death_animation")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(DeathAnimationMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.deathanimationmod.property.hideentitydeathanimation.name",
		description = "lucent.config.lucentclient.deathanimationmod.property.hideentitydeathanimation.description"
	)
	public static boolean HideEntityDeathAnimation = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.deathanimationmod.property.hidearmorstanddeathanimation.name",
		description = "lucent.config.lucentclient.deathanimationmod.property.hidearmorstanddeathanimation.description"
	)
	public static boolean HideArmorStandDeathAnimation = true;

}