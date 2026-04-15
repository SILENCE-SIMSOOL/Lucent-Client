package silence.simsool.lucentclient.mods.impl.performance;

import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucentclient.utils.LucentClientUtils;

public class EntityCullingMod extends Mod {

	public static int culledEntities = 0;
	public static int lastCulledEntities = 0;

	public EntityCullingMod() {
		super("Entity Culling", "Improves performance by not rendering hidden entities.", "Performance", "culling, entity, performance", LucentClientUtils.getModIcon("entity_culling"));
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(EntityCullingMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Cull Players",
		description = "Enables culling for player entities when they are not visible.",
		category = "General"
	)
	public static boolean CullPlayers = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Cull Entities",
		description = "Enables culling for general entities (mobs, items, etc.) when they are not visible.",
		category = "General"
	)
	public static boolean CullEntities = true;

	@ModConfig(
		type = ConfigType.SWITCH,
		name = "Show Debug Info",
		description = "Displays the number of culled entities on the F3 debug screen.",
		category = "Debug"
	)
	public static boolean ShowDebugInfo = true;

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