package silence.simsool.lucentclient.mixin.mixins;

import static silence.simsool.lucent.Lucent.mc;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.client.gui.components.DebugScreenOverlay;
import silence.simsool.lucentclient.mods.impl.performance.EntityCullingMod;

@Mixin(DebugScreenOverlay.class)
public abstract class MixinDebugScreenOverlay {

//	@Inject(method = "extractLines", at = @At("HEAD"), remap = false)
//	private void onExtractLines(GuiGraphicsExtractor guiGraphics, List<String> list, boolean left, CallbackInfo ci) {
//		if (EntityCullingMod.isEnabled() && EntityCullingMod.ShowDebugInfo && left && mc.debugEntries.isOverlayVisible() && mc.level != null) {
//			list.removeIf(s -> s.startsWith("Culled Entities: ")); // need for sodium bug
//			list.addFirst(EntityCullingMod.getCulledEntitiesInfo());
//		}
//	}
//

	@ModifyArg(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/DebugScreenOverlay;extractLines(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Ljava/util/List;Z)V", ordinal = 0), index = 1)
	private List<String> lucent$addDebugInfo(List<String> list) {
		if (EntityCullingMod.isEnabled() && EntityCullingMod.ShowDebugInfo && mc.debugEntries.isOverlayVisible() && mc.level != null) {
			list.removeIf(s -> s.startsWith("Culled Entities: "));
			list.addFirst(EntityCullingMod.getCulledEntitiesInfo());
		}

		return list;
	}

}