package silence.simsool.lucentclient.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.fabricmc.fabric.impl.client.rendering.world.WorldExtractionContextImpl;
import net.minecraft.client.renderer.LevelRenderer;

@Mixin(LevelRenderer.class)
public interface LevelRendererAccessor {

	@Accessor(value = "extractionContext", remap = false)
	WorldExtractionContextImpl lucent$getExtractionContext();

}