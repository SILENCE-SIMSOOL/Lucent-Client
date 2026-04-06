package silence.simsool.lucentclient.mods.impl.hud;

import static silence.simsool.lucent.Lucent.mc;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;

@ModConfig.CategoryPriority(name = "General", priority = 1000)
public class KeystrokesMod extends Mod {

	public static boolean isEnabled() {
		return Lucent.config.getModule(KeystrokesMod.class).isEnabled;
	}

	public KeystrokesMod() {
		super("Keystrokes", "Shows your keyboard and mouse clicks.", "HUD", "keys, keyboard, mouse", "lucid:keys");
	}

}