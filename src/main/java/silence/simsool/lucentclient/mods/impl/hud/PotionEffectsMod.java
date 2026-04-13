package silence.simsool.lucentclient.mods.impl.hud;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

@ModConfig.CategoryPriority(name = "General", priority = 1000)
public class PotionEffectsMod extends Mod {

	public PotionEffectsMod() {
		super("Potion Effects", "Customizable potion effect HUD.", "HUD", "potion, effect, status", LucentClientUtils.getModIcon("potion_effects"));
	}

	public static boolean isEnabled() {
		return Lucent.config.getModule(PotionEffectsMod.class).isEnabled;
	}

}