package silence.simsool.lucentclient.huds;

import java.util.Arrays;

import silence.simsool.lucent.hud.HUDManager;
import silence.simsool.lucentclient.huds.impl.ArmorStatusHUD;
import silence.simsool.lucentclient.huds.impl.DirectionHUD;
import silence.simsool.lucentclient.huds.impl.InfoHUD;
import silence.simsool.lucentclient.huds.impl.KeystrokesHUD;
import silence.simsool.lucentclient.huds.impl.PotionEffectsHUD;

public class HUDRegister {

	public static void register(HUDManager hudManager) {

		Arrays.asList(

			new InfoHUD(),
			new KeystrokesHUD(),
			new PotionEffectsHUD(),
			new ArmorStatusHUD(),
			new DirectionHUD()

		).forEach(hudManager::register);

	}

}