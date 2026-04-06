package silence.simsool.lucentclient.mods.impl.performance;

import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.models.abstracts.Mod;

public class EntityCullingMod extends Mod {

	public EntityCullingMod() {
		super("Entity Culling", "Improves performance by not rendering hidden entities.", "Performance", "culling, entity, performance", "lucid:fps");
	}

	public static boolean isEnabled() {
		return Lucent.config.getModule(EntityCullingMod.class).isEnabled;
	}

}