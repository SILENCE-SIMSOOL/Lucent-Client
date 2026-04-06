package silence.simsool.lucentclient.mods.impl.hud;

import static silence.simsool.lucent.Lucent.mc;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffectInstance;
import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;

@ModConfig.CategoryPriority(name = "General", priority = 1000)
public class PotionEffectsMod extends Mod {

	public PotionEffectsMod() {
		super("Potion Effects", "Customizable potion effect HUD.", "HUD", "potion, effect, status", "lucid:potion");
	}

	public static boolean isEnabled() {
		return Lucent.config.getModule(PotionEffectsMod.class).isEnabled;
	}

}