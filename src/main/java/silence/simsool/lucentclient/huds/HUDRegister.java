package silence.simsool.lucentclient.huds;

import java.util.Arrays;

import silence.simsool.lucent.hud.HUDManager;
import silence.simsool.lucentclient.huds.impl.ArmorStatusHUD;
import silence.simsool.lucentclient.huds.impl.KeystrokesHUD;
import silence.simsool.lucentclient.huds.impl.PotionEffectsHUD;
import silence.simsool.lucentclient.huds.impl.info.impl.CPSHUD;
import silence.simsool.lucentclient.huds.impl.info.impl.CoordinatesHUD;
import silence.simsool.lucentclient.huds.impl.info.impl.FPSHUD;
import silence.simsool.lucentclient.huds.impl.info.impl.PingHUD;
import silence.simsool.lucentclient.huds.impl.info.impl.TPSHUD;

public class HUDRegister {

	public static void register(HUDManager hudManager) {

		Arrays.asList(

			new FPSHUD(),
			new CPSHUD(),
			new TPSHUD(),
			new PingHUD(),
			new CoordinatesHUD(),

			new KeystrokesHUD(),
			new PotionEffectsHUD(),
			new ArmorStatusHUD()

		).forEach(hudManager::register);

	}

}