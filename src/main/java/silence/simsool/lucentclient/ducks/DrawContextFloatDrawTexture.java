package silence.simsool.lucentclient.ducks;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.resources.Identifier;

public interface DrawContextFloatDrawTexture {
	default void centered_crosshair$blitSprite(RenderPipeline renderPipeline, Identifier location, float x, float y, int width, int height) {}
}