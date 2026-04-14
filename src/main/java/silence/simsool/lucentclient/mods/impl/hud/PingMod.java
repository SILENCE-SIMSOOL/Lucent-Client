package silence.simsool.lucentclient.mods.impl.hud;

import static silence.simsool.lucent.Lucent.mc;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

@ModConfig.CategoryPriority(name = "General", priority = 1000)
@ModConfig.CategoryPriority(name = "Text Style", priority = 500)
@ModConfig.CategoryPriority(name = "Background", priority = 100)
public class PingMod extends Mod {

	public PingMod() {
		super(
				"Ping", "Displays your latency to the server.",
				"HUD",
				"ping, latency",
				LucentClientUtils.getModIcon("ping")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(PingMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "Show Brackets", 
		description = "Encloses the ping value in brackets on the screen.",
		category = "General",
		priority = 2
	)
	public static boolean ShowBrackets = true;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "Reverse Order", 
		description = "Swaps the display order of the ping label and value on the screen.",
		category = "General",
		priority = 1
	)
	public static boolean ReverseOrder = true;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "Text Shadow", 
		description = "Adds a shadow effect to the ping text on the screen.",
		category = "Text Style",
		priority = 2
	)
	public static boolean TextShadow = true;

	@ModConfig(
		type = ConfigType.COLOR, 
		name = "Text Color", 
		description = "Sets the color of the ping text displayed on the screen.",
		category = "Text Style",
		priority = 1
	)
	public static int TextColor = 0xFFFFFFFF;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "Show Background", 
		description = "Displays a background box behind the ping text on the screen.",
		category = "Background",
		priority = 2
	)
	public static boolean ShowBackground = false;

	@ModConfig(
		type = ConfigType.COLOR, 
		name = "Background Color", 
		description = "Sets the color of the background box displayed on the screen.",
		category = "Background",
		priority = 1
	)
	public static int BackgroundColor = 0x80000000;

	public static int getPing() {
		ClientPacketListener connection = mc.getConnection();
		LocalPlayer player = mc.player;
		if (connection != null && player != null) {
			PlayerInfo playerInfo = connection.getPlayerInfo(player.getUUID());
			if (playerInfo != null) return playerInfo.getLatency();
		}
		return -1;
	}

}
