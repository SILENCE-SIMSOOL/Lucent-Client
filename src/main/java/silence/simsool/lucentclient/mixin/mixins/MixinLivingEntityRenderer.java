package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer {

	// Actually, overriding getOverlayCoords or modifying the render buffer is complex.
	// In vanilla 1.21.1, the hurt color is hardcoded to red in getOverlayCoords or similar, but
	// the actual color tint is handled in internal shaders. We can modify overlay logic if needed.
	// For now, let's leave a comment or just try to inject.
	
	// Fast approach: intercept RenderSystem.setShaderColor inside or modify the overlay U coord.
	// The problem is vanilla tint is baked into the overlay texture.
	// The overlay texture is 16x16, white for hurt, otherwise transparent.
	// Color is applied when rendering.
}