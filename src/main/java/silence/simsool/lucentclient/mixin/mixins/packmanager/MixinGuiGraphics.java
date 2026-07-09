package silence.simsool.lucentclient.mixin.mixins.packmanager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;
import silence.simsool.lucentclient.mods.impl.utility.packmanager.PackManagerMod;

@Mixin(GuiGraphics.class)
public class MixinGuiGraphics {

	@ModifyVariable(method = "renderTooltip", at = @At("HEAD"), argsOnly = true)
	private Identifier modifyStyle(Identifier style) {
		if (PackManagerMod.isEnabled() && PackManagerMod.DisablePackOverride) {
			if (style != null && "hypixel_skyblock".equals(style.getNamespace())) {
				return null;
			}
		}
		return style;
	}

}