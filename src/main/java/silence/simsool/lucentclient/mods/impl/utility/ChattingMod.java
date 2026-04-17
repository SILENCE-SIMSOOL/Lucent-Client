package silence.simsool.lucentclient.mods.impl.utility;

import static silence.simsool.lucent.Lucent.mc;

import java.awt.Color;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.GuiMessage;
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
				"Chatting Mod", "Enhances your chat experience with various features.",
				"Utility",
				"chat, ui",
				"/assets/lucentclient/textures/modicons/chatting.png"
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(ChattingMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Chat Background",
		description = "Toggles the chat background visibility.",
		category = "Appearance",
		priority = 100
	)
	public static boolean ChatBackground = true;

	@ModConfig(
		type = ConfigType.COLOR,
		name = "Background Color",
		description = "Sets the color of the chat background.",
		category = "Appearance",
		priority = 90
	)
	public static Color BackgroundColor = new Color(0, 0, 0, 100);

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Chat Copy",
		description = "Enables the ability to copy chat messages.",
		category = "General",
		priority = 80
	)
	public static boolean ChatCopy = true;

	@ModConfig(
		type = ConfigType.KEYBIND,
		name = "Copy Keybind",
		description = "Keybind to copy the chat message under the mouse cursor.",
		category = "General",
		priority = 70
	)
	public static KeyBind CopyKey = KeyBind.ofMouse(GLFW.GLFW_MOUSE_BUTTON_RIGHT, 0);

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Chat Text Shadow",
		description = "Toggles the shadow on chat text.",
		category = "Appearance",
		priority = 60
	)
	public static boolean ChatTextShadow = true;

	public static boolean isRenderingChat = false;

	public static boolean copyAt(double mouseX, double mouseY) {
		if (mc.gui.getChat() instanceof IChatComponent chat) {
			GuiMessage msg = chat.getMessageAt(mouseX, mouseY);
			if (msg != null) {
				String plainText = UChat.cleanColor(msg.content().getString());
				mc.keyboardHandler.setClipboard(plainText);
				UChat.chat("§aChat has been copied!");
				return true;
			}
		}
		return false;
	}

}