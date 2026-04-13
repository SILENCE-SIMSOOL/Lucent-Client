package silence.simsool.lucentclient.mods.impl.performance;

import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class EntityCullingMod extends Mod {

	public static int culledEntities = 0;
	public static int lastCulledEntities = 0;

	public EntityCullingMod() {
		super("Entity Culling", "Improves performance by not rendering hidden entities.", "Performance", "culling, entity, performance", LucentClientUtils.getModIcon("entity_culling"));
	}

	public static boolean isEnabled() {
		return Lucent.config.getModule(EntityCullingMod.class).isEnabled;
	}

	{
		WorldRenderEvents.BEFORE_ENTITIES.register(context -> {
			lastCulledEntities = culledEntities;
			culledEntities = 0;
		});
	}

	public static String getCulledEntitiesInfo() {
		return "Culled Entities: " + lastCulledEntities;
	}

}