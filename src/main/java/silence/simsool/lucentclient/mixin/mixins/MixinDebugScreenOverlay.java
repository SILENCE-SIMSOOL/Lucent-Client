package silence.simsool.lucentclient.mixin.mixins;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import silence.simsool.lucentclient.mods.impl.performance.EntityCullingMod;

@Mixin(DebugScreenOverlay.class)
public class MixinDebugScreenOverlay {

	@Inject(method = "extractLines", at = @At("HEAD"), remap = false)
	private void onExtractLines(GuiGraphicsExtractor guiGraphics, List<String> list, boolean left, CallbackInfo ci) {
		if (left) {
			if (EntityCullingMod.isEnabled() && EntityCullingMod.ShowDebugInfo) {
				list.addFirst(EntityCullingMod.getCulledEntitiesInfo());
			}
		}
	}

}