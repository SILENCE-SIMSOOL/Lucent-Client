package silence.simsool.lucentclient.mods.impl.utility;

import silence.simsool.lucent.general.utils.LucentCategory;

import static silence.simsool.lucent.Lucent.mc;

import java.awt.Color;

import org.lwjgl.glfw.GLFW;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.data.KeyBind;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucent.general.utils.useful.UChat;
import silence.simsool.lucentclient.ducks.IChatComponent;

@ModConfig.CategoryPriority(name = "General", priority = 1000)
@ModConfig.CategoryPriority(name = "Appearance", priority = 500)
public class ChattingMod extends Mod {

	public ChattingMod() {
		super(
				"lucent.config.lucentclient.chattingmod.general.name", "lucent.config.lucentclient.chattingmod.general.description",
				LucentCategory.UTILITY,
				"",
				"/assets/lucentclient/textures/modicons/chatting.png"
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(ChattingMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.chattingmod.property.chatbackground.name",
		description = "lucent.config.lucentclient.chattingmod.property.chatbackground.description",
		category = "Appearance",
		priority = 100
	)
	public static boolean ChatBackground = true;

	@ModConfig(
		type = ConfigType.COLOR,
		name = "lucent.config.lucentclient.chattingmod.property.backgroundcolor.name",
		description = "lucent.config.lucentclient.chattingmod.property.backgroundcolor.description",
		category = "Appearance",
		priority = 90
	)
	public static Color BackgroundColor = new Color(0, 0, 0, 100);

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.chattingmod.property.chatcopy.name",
		description = "lucent.config.lucentclient.chattingmod.property.chatcopy.description",
		priority = 80
	)
	public static boolean ChatCopy = true;

	@ModConfig(
		type = ConfigType.KEYBIND,
		name = "lucent.config.lucentclient.chattingmod.property.copykey.name",
		description = "lucent.config.lucentclient.chattingmod.property.copykey.description",
		priority = 70
	)
	public static KeyBind CopyKey = KeyBind.ofMouse(GLFW.GLFW_MOUSE_BUTTON_RIGHT, 0);

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.chattingmod.property.chattextshadow.name",
		description = "lucent.config.lucentclient.chattingmod.property.chattextshadow.description",
		category = "Appearance",
		priority = 60
	)
	public static boolean ChatTextShadow = true;

	public static boolean isRenderingChat = false;

	public static boolean copyAt(double mouseX, double mouseY) {
		if (mc.gui.hud.getChat() instanceof IChatComponent chat) {
			GuiMessage msg = chat.getMessageAt(mouseX, mouseY);
			if (msg != null) {
				String plainText = UChat.cleanColor(msg.content().getString());
				mc.keyboardHandler.setClipboard(plainText);
				UChat.chat(Component.translatable("lucent.chat.lucentclient.chattingmod.copied").withStyle(ChatFormatting.GREEN));
				return true;
			}
		}
		return false;
	}

}