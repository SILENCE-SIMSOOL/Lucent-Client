package silence.simsool.lucentclient.mods.impl.hud;

import static silence.simsool.lucent.Lucent.mc;

import java.util.UUID;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

@ModConfig.CategoryPriority(name = "General", priority = 1000)
public class PingMod extends Mod {

	public PingMod() {
		super("Ping", "Displays your latency to the server.", "HUD", "ping, latency", LucentClientUtils.getModIcon("ping"));
	}

	@ModConfig(type = ConfigType.SWITCH, name = "Text Shadow", priority = 1000)
	public static boolean TextShadow = true;

	@ModConfig(type = ConfigType.COLOR, name = "Text Color", priority = 990)
	public static int TextColor = 0xFFFFFFFF;

	@ModConfig(type = ConfigType.SWITCH, name = "Show Background", priority = 980)
	public static boolean ShowBackground = false;

	@ModConfig(type = ConfigType.COLOR, name = "Background Color", priority = 970)
	public static int BackgroundColor = 0x80000000;

	@ModConfig(type = ConfigType.SWITCH, name = "Reverse Order", priority = 960)
	public static boolean ReverseOrder = false;

	@ModConfig(type = ConfigType.SWITCH, name = "Show Brackets", priority = 950)
	public static boolean ShowBrackets = false;

	public static int getPing() {
		ClientPacketListener connection = mc.getConnection();
		LocalPlayer player = mc.player;
		UUID uuid = player.getUUID();
		int ping = -1;

		if (connection != null && player != null) {
			PlayerInfo playerInfo = connection.getPlayerInfo(uuid);
			if (playerInfo != null) ping = playerInfo.getLatency();
		}
		return ping;
	}

}