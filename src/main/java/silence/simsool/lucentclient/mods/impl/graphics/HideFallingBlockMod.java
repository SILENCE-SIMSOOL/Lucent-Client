package silence.simsool.lucentclient.mods.impl.graphics;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class HideFallingBlockMod extends Mod {

	public HideFallingBlockMod() {
		super(
				"Hide Falling Block",
				"Hides falling block entities like sand and gravel.",
				"Graphics",
				"sand, gravel",
				LucentClientUtils.getModIcon("hidefallingblock")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(HideFallingBlockMod.class);
	}

}