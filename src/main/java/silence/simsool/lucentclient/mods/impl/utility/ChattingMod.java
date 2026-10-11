package silence.simsool.lucentclient.mods.impl.utility;

import static silence.simsool.lucent.Lucent.mc;

import java.awt.Color;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.multiplayer.chat.GuiMessage;
import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.data.KeyBind;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucent.general.utils.L10n;
import silence.simsool.lucent.general.utils.LucentCategory;
import silence.simsool.lucent.general.utils.useful.UChat;
import silence.simsool.lucent.general.utils.useful.UDesktop;
import silence.simsool.lucentclient.ducks.IChatComponent;
import silence.simsool.lucentclient.utils.LucentClientUtils;

@ModConfig.CategoryPriority(name = "Appearance", priority = 500)
@ModConfig.CategoryPriority(name = "Animation", priority = 400)
@ModConfig.CategoryPriority(name = "Chat Peek", priority = 300)
public class ChattingMod extends Mod {

	public ChattingMod() {
		super(
				"lucent.config.lucentclient.chattingmod.general.name", "lucent.config.lucentclient.chattingmod.general.description",
				LucentCategory.UTILITY,
				"chat, copy, smooth, peek",
				LucentClientUtils.getModIcon("chatting")
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
	public static KeyBind CopyKey = KeyBind.ofMouse(InputConstants.MOUSE_BUTTON_RIGHT, 0);

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.chattingmod.property.chattextshadow.name",
		description = "lucent.config.lucentclient.chattingmod.property.chattextshadow.description",
		category = "Appearance",
		priority = 60
	)
	public static boolean ChatTextShadow = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.chattingmod.property.smoothchat.name",
		description = "lucent.config.lucentclient.chattingmod.property.smoothchat.description",
		category = "Animation",
		priority = 50
	)
	public static boolean SmoothChat = true;

	@ModConfig(
		type = ConfigType.SLIDER,
		name = "lucent.config.lucentclient.chattingmod.property.smoothchatduration.name",
		description = "lucent.config.lucentclient.chattingmod.property.smoothchatduration.description",
		category = "Animation",
		min = 50.0f, max = 1000.0f, step = 50.0f,
		priority = 40
	)
	public static float SmoothChatDuration = 300.0f;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.chattingmod.property.smoothscrolling.name",
		description = "lucent.config.lucentclient.chattingmod.property.smoothscrolling.description",
		category = "Animation",
		priority = 30
	)
	public static boolean SmoothScrolling = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.chattingmod.property.chatpeek.name",
		description = "lucent.config.lucentclient.chattingmod.property.chatpeek.description",
		category = "Chat Peek",
		priority = 20
	)
	public static boolean ChatPeek = true;

	@ModConfig(
		type = ConfigType.KEYBIND,
		name = "lucent.config.lucentclient.chattingmod.property.peekkey.name",
		description = "lucent.config.lucentclient.chattingmod.property.peekkey.description",
		category = "Chat Peek",
		priority = 10
	)
	public static KeyBind PeekKey = KeyBind.ofKey(InputConstants.KEY_LALT, 0);

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "lucent.config.lucentclient.chattingmod.property.peekscrolling.name",
		description = "lucent.config.lucentclient.chattingmod.property.peekscrolling.description",
		category = "Chat Peek",
		priority = 5
	)
	public static boolean PeekScrolling = true;

	public static boolean isRenderingChat = false;
	private static boolean wasPeeking = false;

	public static boolean isPeeking() {
		return isEnabled() && ChatPeek && PeekKey.isKeyDown() && mc.gui != null && mc.gui.screen() == null;
	}

	public static void checkPeeking() {
		boolean peeking = isPeeking();
		if (wasPeeking && !peeking) {
			if (mc.gui != null && mc.gui.hud != null && mc.gui.hud.getChat() != null) {
				mc.gui.hud.getChat().resetChatScroll();
			}
		}
		wasPeeking = peeking;
	}

	public static boolean handlePeekScroll(double yoffset) {
		if (isPeeking() && PeekScrolling && yoffset != 0.0) {
			int amount = (int) Math.signum(yoffset);
			boolean shiftDown = InputConstants.isKeyDown(InputConstants.KEY_LSHIFT)
					|| InputConstants.isKeyDown(InputConstants.KEY_RSHIFT);
			if (!shiftDown) {
				amount *= 7;
			}
			if (mc.gui != null && mc.gui.hud != null && mc.gui.hud.getChat() != null) {
				mc.gui.hud.getChat().scrollChat(amount);
			}
			return true;
		}
		return false;
	}

	public static boolean copyAt(double mouseX, double mouseY) {
		if (mc.gui.hud.getChat() instanceof IChatComponent chat) {
			GuiMessage msg = chat.getMessageAt(mouseX, mouseY);
			if (msg != null) {
				String plainText = UChat.cleanColor(msg.content().getString());
				UDesktop.setClipboard(plainText);
				UChat.chat("§a" + L10n.translate("lucent.chat.lucentclient.chattingmod.copied"));
				return true;
			}
		}
		return false;
	}
}