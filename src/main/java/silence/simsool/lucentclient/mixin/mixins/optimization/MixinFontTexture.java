package silence.simsool.lucentclient.mixin.mixins.optimization;

import net.minecraft.client.gui.font.FontTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import silence.simsool.lucentclient.mods.impl.performance.FastRenderMod;

@Mixin(FontTexture.class)
public abstract class MixinFontTexture {

	@ModifyConstant(method = "*", constant = @Constant(intValue = 256))
	private int modifyTextureSize(final int original) {
		return (FastRenderMod.isEnabled() && FastRenderMod.FontAtlasResizing) ? 1024 : original;
	}

	@ModifyConstant(method = "*", constant = @Constant(floatValue = 256.0F))
	private float modifyTextureSize(final float original) {
		return (FastRenderMod.isEnabled() && FastRenderMod.FontAtlasResizing) ? 1024.0F : original;
	}

}