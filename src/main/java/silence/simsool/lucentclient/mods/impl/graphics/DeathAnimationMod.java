package silence.simsool.lucentclient.mods.impl.graphics;

import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;

@ModConfig.CategoryPriority(name = "General", priority = 1000)
public class DeathAnimationMod extends Mod {

	public DeathAnimationMod() {
		super("Death Animation", "Modify or disabled entity death animations.", "Graphics", "death, entity, animation", "lucid:skull");
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Hide Entity",
		description = "Hide death animations (red flash + fall) for regular entities.",
		category = "General",
		priority = 1000
	)
	public static boolean HideEntityDeathAnimation = false;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Hide Armor Stands",
		description = "Hide death animations when armor stands are destroyed.",
		category = "General",
		priority = 990
	)
	public static boolean HideArmorStandDeathAnimation = false;

}