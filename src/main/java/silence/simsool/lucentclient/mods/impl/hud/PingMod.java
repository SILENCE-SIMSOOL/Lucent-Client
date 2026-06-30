package silence.simsool.lucentclient.mods.impl.hud;

import silence.simsool.lucent.general.utils.LucentCategory;

import static silence.simsool.lucent.Lucent.mc;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

@ModConfig.CategoryPriority(name = "Text Style", priority = 500)
@ModConfig.CategoryPriority(name = "Background", priority = 100)
public class PingMod extends Mod {

	public PingMod() {
		super(
				"lucent.config.lucentclient.pingmod.general.name", "lucent.config.lucentclient.pingmod.general.description",
				LucentCategory.HUB,
				"ping, latency, server",
				LucentClientUtils.getModIcon("ping")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(PingMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "lucent.config.lucentclient.pingmod.property.showbrackets.name", 
		description = "lucent.config.lucentclient.pingmod.property.showbrackets.description",
		priority = 2
	)
	public static boolean ShowBrackets = true;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "lucent.config.lucentclient.pingmod.property.reverseorder.name", 
		description = "lucent.config.lucentclient.pingmod.property.reverseorder.description",
		priority = 1
	)
	public static boolean ReverseOrder = true;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "lucent.config.lucentclient.pingmod.property.textshadow.name", 
		description = "lucent.config.lucentclient.pingmod.property.textshadow.description",
		category = "Text Style",
		priority = 2
	)
	public static boolean TextShadow = true;

	@ModConfig(
		type = ConfigType.COLOR, 
		name = "lucent.config.lucentclient.pingmod.property.textcolor.name", 
		description = "lucent.config.lucentclient.pingmod.property.textcolor.description",
		category = "Text Style",
		priority = 1
	)
	public static int TextColor = 0xFFFFFFFF;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "lucent.config.lucentclient.pingmod.property.showbackground.name", 
		description = "lucent.config.lucentclient.pingmod.property.showbackground.description",
		category = "Background",
		priority = 2
	)
	public static boolean ShowBackground = false;

	@ModConfig(
		type = ConfigType.COLOR, 
		name = "lucent.config.lucentclient.pingmod.property.backgroundcolor.name", 
		description = "lucent.config.lucentclient.pingmod.property.backgroundcolor.description",
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