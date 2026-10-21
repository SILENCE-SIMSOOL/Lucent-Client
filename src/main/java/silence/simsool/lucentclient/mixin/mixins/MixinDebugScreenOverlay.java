package silence.simsool.lucentclient.mixin.mixins;

import static silence.simsool.lucent.Lucent.mc;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.client.gui.components.DebugScreenOverlay;
import silence.simsool.lucentclient.mods.impl.performance.EntityCullingMod;

@Mixin(DebugScreenOverlay.class)
public abstract class MixinDebugScreenOverlay {

	@Shadow
	public abstract boolean showDebugScreen();

	@ModifyVariable(method = "render", at = @At(value = "STORE"), ordinal = 0)
	private List<String> onRenderDebugScreen(List<String> list) {
		if (showDebugScreen() && mc.level != null && EntityCullingMod.isEnabled() && EntityCullingMod.ShowDebugInfo) {
			//list.add("[Lucent] Occlusion Culled Entities: " + EntityCullingMod.lastCulledEntities);
			list.addFirst(EntityCullingMod.getCulledEntitiesInfo());
		}
		return list;
	}

}