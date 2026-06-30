package silence.simsool.lucentclient.mods.impl.graphics;

import silence.simsool.lucent.general.utils.LucentCategory;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class HideFallingBlockMod extends Mod {

	public HideFallingBlockMod() {
		super(
				"lucent.config.lucentclient.hidefallingblockmod.general.name", "lucent.config.lucentclient.hidefallingblockmod.general.description",
				LucentCategory.GRAPHICS,
				"hide, falling, block, sand, gravel",
				LucentClientUtils.getModIcon("hidefallingblock")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(HideFallingBlockMod.class);
	}

}