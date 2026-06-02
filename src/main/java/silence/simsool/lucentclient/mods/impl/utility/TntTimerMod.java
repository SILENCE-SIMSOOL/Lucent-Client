package silence.simsool.lucentclient.mods.impl.utility;

import static silence.simsool.lucent.Lucent.mc;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.PrimedTnt;
import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.events.impl.LucentEvent.RenderWorldEvent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucent.general.utils.render.RenderUtils;
import silence.simsool.lucentclient.utils.LucentClientUtils;

@ModConfig.CategoryPriority(name = "General", priority = 1000)
@ModConfig.CategoryPriority(name = "Text Style", priority = 500)
@ModConfig.CategoryPriority(name = "Background", priority = 100)
public class TntTimerMod extends Mod {

	public TntTimerMod() {
		super(
				"TNT Timer", "Shows the remaining time before TNT explodes.",
				"Utility",
				"tnt, time, explode",
				LucentClientUtils.getModIcon("tnt_timer")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(TntTimerMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "Show Background", 
		description = "Displays a background box behind the TNT timer text.",
		category = "Background",
		priority = 2
	)
	public static boolean ShowBackground = true;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "Text Shadow", 
		description = "Adds a shadow effect to the TNT timer text.",
		category = "Text Style",
		priority = 1
	)
	public static boolean TextShadow = true;

	@Override
	public void onRenderWorld(RenderWorldEvent event) {
		for (Entity entity : mc.level.entitiesForRendering()) {
			if (entity instanceof PrimedTnt tnt) {
				int ticks = tnt.getFuse();
				double time = ticks / 20.0;
				String text = String.format("%.2fs", time);
				RenderUtils.drawText(text, entity.getPosition(event.partialTick).add(0, 1.44f, 0), 1.0f, true);
			}
		}
	}

}