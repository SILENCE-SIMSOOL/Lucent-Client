package silence.simsool.lucentclient.mods.impl.utility;

import silence.simsool.lucent.general.utils.LucentCategory;

import static silence.simsool.lucent.Lucent.mc;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.PrimedTnt;
import silence.simsool.lucent.Lucent;
import silence.simsool.lucent.events.impl.LucentEvent.RenderWorldEvent;
import silence.simsool.lucent.general.enums.ConfigType;
import silence.simsool.lucent.general.models.abstracts.Mod;
import silence.simsool.lucent.general.models.interfaces.annotations.ModConfig;
import silence.simsool.lucent.general.utils.render.Render3D;
import silence.simsool.lucentclient.utils.LucentClientUtils;

@ModConfig.CategoryPriority(name = "Text Style", priority = 500)
@ModConfig.CategoryPriority(name = "Background", priority = 100)
public class TntTimerMod extends Mod {

	public TntTimerMod() {
		super(
				"lucent.config.lucentclient.tnttimermod.general.name", "lucent.config.lucentclient.tnttimermod.general.description",
				LucentCategory.UTILITY,
				"tnt, time, explode",
				LucentClientUtils.getModIcon("tnt_timer")
		);
	}

	public static boolean isEnabled() {
		return Lucent.config.isModuleEnabled(TntTimerMod.class);
	}

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "lucent.config.lucentclient.tnttimermod.property.showbackground.name", 
		description = "lucent.config.lucentclient.tnttimermod.property.showbackground.description",
		category = "Background",
		priority = 2
	)
	public static boolean ShowBackground = true;

	@ModConfig(
		type = ConfigType.SWITCH, 
		name = "lucent.config.lucentclient.tnttimermod.property.textshadow.name", 
		description = "lucent.config.lucentclient.tnttimermod.property.textshadow.description",
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
				Render3D.drawText(text, entity.getPosition(event.partialTick).add(0, 1.44f, 0), 1.0f, true);
			}
		}
	}

}