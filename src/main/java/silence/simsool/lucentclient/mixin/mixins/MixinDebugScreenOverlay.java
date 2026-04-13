package silence.simsool.lucentclient.mixin.mixins;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.client.gui.components.DebugScreenOverlay;
import silence.simsool.lucentclient.mods.impl.performance.EntityCullingMod;

@Mixin(DebugScreenOverlay.class)
public class MixinDebugScreenOverlay {

	@ModifyVariable(method = "render", at = @At(value = "STORE"), ordinal = 0)
	private List<String> onRenderDebugScreen(List<String> list) {
		if (EntityCullingMod.isEnabled()) {
			//list.add("[Lucent] Occlusion Culled Entities: " + EntityCullingMod.lastCulledEntities);
			list.addFirst(EntityCullingMod.getCulledEntitiesInfo());
		}
		return list;
	}

}