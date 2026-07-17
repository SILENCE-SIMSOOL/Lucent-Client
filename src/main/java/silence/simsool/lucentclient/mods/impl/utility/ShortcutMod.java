package silence.simsool.lucentclient.mods.impl.utility;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.events.impl.LucentEvent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.data.KeyBind;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucent.general.utils.LucentCategory;
import silence.simsool.lucent.general.utils.useful.UChat;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class ShortcutMod extends Mod {

	public ShortcutMod() {
		super(
				"lucent.config.lucentclient.shortcutmod.general.name", "lucent.config.lucentclient.shortcutmod.general.description",
				LucentCategory.UTILITY,
				"shortcut, bind, auto, text",
				LucentClientUtils.getModIcon("shortcut")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(ShortcutMod.class);
	}

	@ModConfig(
		type = ConfigType.TEXT,
		name = "lucent.config.lucentclient.shortcutmod.property.text1.name",
		description = "",
		category = "Text",
		priority = 140
	)
	public static String Text1 = "";

	@ModConfig(
		type = ConfigType.TEXT,
		name = "lucent.config.lucentclient.shortcutmod.property.text2.name",
		description = "",
		category = "Text",
		priority = 130
	)
	public static String Text2 = "";

	@ModConfig(
		type = ConfigType.TEXT,
		name = "lucent.config.lucentclient.shortcutmod.property.text3.name",
		description = "",
		category = "Text",
		priority = 120
	)
	public static String Text3 = "";

	@ModConfig(
		type = ConfigType.TEXT,
		name = "lucent.config.lucentclient.shortcutmod.property.text4.name",
		description = "",
		category = "Text",
		priority = 110
	)
	public static String Text4 = "";

	@ModConfig(
		type = ConfigType.TEXT,
		name = "lucent.config.lucentclient.shortcutmod.property.text5.name",
		description = "",
		category = "Text",
		priority = 100
	)
	public static String Text5 = "";

	@ModConfig(
		type = ConfigType.TEXT,
		name = "lucent.config.lucentclient.shortcutmod.property.text6.name",
		description = "",
		category = "Text",
		priority = 90
	)
	public static String Text6 = "";

	@ModConfig(
		type = ConfigType.TEXT,
		name = "lucent.config.lucentclient.shortcutmod.property.text7.name",
		description = "",
		category = "Text",
		priority = 80
	)
	public static String Text7 = "";

	@ModConfig(
		type = ConfigType.KEYBIND,
		name = "lucent.config.lucentclient.shortcutmod.property.keybind1.name",
		description = "",
		category = "Keybind",
		priority = 70
	)
	public static KeyBind Keybind1 = KeyBind.none();

	@ModConfig(
		type = ConfigType.KEYBIND,
		name = "lucent.config.lucentclient.shortcutmod.property.keybind2.name",
		description = "",
		category = "Keybind",
		priority = 60
	)
	public static KeyBind Keybind2 = KeyBind.none();

	@ModConfig(
		type = ConfigType.KEYBIND,
		name = "lucent.config.lucentclient.shortcutmod.property.keybind3.name",
		description = "",
		category = "Keybind",
		priority = 50
	)
	public static KeyBind Keybind3 = KeyBind.none();

	@ModConfig(
		type = ConfigType.KEYBIND,
		name = "lucent.config.lucentclient.shortcutmod.property.keybind4.name",
		description = "",
		category = "Keybind",
		priority = 40
	)
	public static KeyBind Keybind4 = KeyBind.none();

	@ModConfig(
		type = ConfigType.KEYBIND,
		name = "lucent.config.lucentclient.shortcutmod.property.keybind5.name",
		description = "",
		category = "Keybind",
		priority = 30
	)
	public static KeyBind Keybind5 = KeyBind.none();

	@ModConfig(
		type = ConfigType.KEYBIND,
		name = "lucent.config.lucentclient.shortcutmod.property.keybind6.name",
		description = "",
		category = "Keybind",
		priority = 20
	)
	public static KeyBind Keybind6 = KeyBind.none();

	@ModConfig(
		type = ConfigType.KEYBIND,
		name = "lucent.config.lucentclient.shortcutmod.property.keybind7.name",
		description = "",
		category = "Keybind",
		priority = 10
	)
	public static KeyBind Keybind7 = KeyBind.none();

	@Override
	public void onKeybind(LucentEvent.KeybindEvent event) {
		if (event == null || !event.isPressed()) return;

		KeyBind pressed = event.keybind;
		if (pressed == null || !pressed.isBound()) return;

		if      (isSameKey(pressed, Keybind1)) sendText(Text1);
		else if (isSameKey(pressed, Keybind2)) sendText(Text2);
		else if (isSameKey(pressed, Keybind3)) sendText(Text3);
		else if (isSameKey(pressed, Keybind4)) sendText(Text4);
		else if (isSameKey(pressed, Keybind5)) sendText(Text5);
		else if (isSameKey(pressed, Keybind6)) sendText(Text6);
		else if (isSameKey(pressed, Keybind7)) sendText(Text7);
	}

	private boolean isSameKey(KeyBind a, KeyBind b) {
		if (a == null || b == null) return false;
		return a.keyCode == b.keyCode && a.mouseButton == b.mouseButton && a.mods == b.mods;
	}

	private void sendText(String text) {
		if (text != null && !text.isEmpty()) {
			UChat.say(text);
		}
	}

}