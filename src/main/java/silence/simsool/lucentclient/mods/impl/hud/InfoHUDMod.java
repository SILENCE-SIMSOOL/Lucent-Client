package silence.simsool.lucentclient.mods.impl.hud;

import static silence.simsool.lucent.Lucent.mc;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.core.BlockPos;
import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.mods.impl.utility.ToggleSprintMod;

@ModConfig.CategoryPriority(name = "General", priority = 1000)
public class InfoHUDMod extends Mod {

	public InfoHUDMod() {
		super("Info HUD", "Displays various client and server information.", "HUD", "fps, cps, ping, tps, coord", "lucid:info");
	}

	public static boolean isEnabled() {
		return Lucent.config.getModule(InfoHUDMod.class).isEnabled;
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Coordinates",
		description = "Show your current world coordinates.",
		category = "General",
		priority = 1000
	)
	public static boolean ShowCoordinates = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "FPS",
		description = "Show your current frames per second.",
		category = "General",
		priority = 990
	)
	public static boolean ShowFps = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "CPS",
		description = "Show your current clicks per second.",
		category = "General",
		priority = 980
	)
	public static boolean ShowCps = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "TPS",
		description = "Show server ticks per second.",
		category = "General",
		priority = 970
	)
	public static boolean ShowTps = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Ping",
		description = "Show your latency to the server.",
		category = "General",
		priority = 960
	)
	public static boolean ShowPing = true;

}