package silence.simsool.lucentclient.features.universalshortcuts;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.input.KeyEvent;

public abstract class UniversalShortcutsUtil {

	private UniversalShortcutsUtil() {
	}

	public static boolean isKey(int keyCode, String keyName) {
		if (keyCode == GLFW.GLFW_KEY_UNKNOWN) {
			return false;
		}
		return keyName.equalsIgnoreCase(GLFW.glfwGetKeyName(keyCode, 0));
	}

	public static int remapKeyCodeToQWERTY(int localKeyCode) {
		if (localKeyCode == GLFW.GLFW_KEY_UNKNOWN) {
			return GLFW.GLFW_KEY_UNKNOWN;
		}
		String keyName = GLFW.glfwGetKeyName(localKeyCode, 0);
		if (keyName == null) {
			return localKeyCode;
		}
		return switch (keyName) {
			case "0" -> GLFW.GLFW_KEY_0;
			case "1" -> GLFW.GLFW_KEY_1;
			case "2" -> GLFW.GLFW_KEY_2;
			case "3" -> GLFW.GLFW_KEY_3;
			case "4" -> GLFW.GLFW_KEY_4;
			case "5" -> GLFW.GLFW_KEY_5;
			case "6" -> GLFW.GLFW_KEY_6;
			case "7" -> GLFW.GLFW_KEY_7;
			case "8" -> GLFW.GLFW_KEY_8;
			case "9" -> GLFW.GLFW_KEY_9;
			case "A", "a" -> GLFW.GLFW_KEY_A;
			case "B", "b" -> GLFW.GLFW_KEY_B;
			case "C", "c" -> GLFW.GLFW_KEY_C;
			case "D", "d" -> GLFW.GLFW_KEY_D;
			case "E", "e" -> GLFW.GLFW_KEY_E;
			case "F", "f" -> GLFW.GLFW_KEY_F;
			case "G", "g" -> GLFW.GLFW_KEY_G;
			case "H", "h" -> GLFW.GLFW_KEY_H;
			case "I", "i" -> GLFW.GLFW_KEY_I;
			case "J", "j" -> GLFW.GLFW_KEY_J;
			case "K", "k" -> GLFW.GLFW_KEY_K;
			case "L", "l" -> GLFW.GLFW_KEY_L;
			case "M", "m" -> GLFW.GLFW_KEY_M;
			case "N", "n" -> GLFW.GLFW_KEY_N;
			case "O", "o" -> GLFW.GLFW_KEY_O;
			case "P", "p" -> GLFW.GLFW_KEY_P;
			case "Q", "q" -> GLFW.GLFW_KEY_Q;
			case "R", "r" -> GLFW.GLFW_KEY_R;
			case "S", "s" -> GLFW.GLFW_KEY_S;
			case "T", "t" -> GLFW.GLFW_KEY_T;
			case "U", "u" -> GLFW.GLFW_KEY_U;
			case "V", "v" -> GLFW.GLFW_KEY_V;
			case "W", "w" -> GLFW.GLFW_KEY_W;
			case "X", "x" -> GLFW.GLFW_KEY_X;
			case "Y", "y" -> GLFW.GLFW_KEY_Y;
			case "Z", "z" -> GLFW.GLFW_KEY_Z;
			default -> localKeyCode;
		};
	}

	public static KeyEvent remapKeyEventToQWERTY(KeyEvent localKeyEvent) {
		return new KeyEvent(remapKeyCodeToQWERTY(localKeyEvent.key()), localKeyEvent.scancode(), localKeyEvent.modifiers());
	}
}