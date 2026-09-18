package silence.simsool.lucentclient.mixin.mixins.optimization;

import com.mojang.blaze3d.systems.ScissorState;
import java.util.Objects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import silence.simsool.lucentclient.mods.impl.performance.FastRenderMod;

@Mixin(ScissorState.class)
public abstract class MixinScissorState {

	@Shadow
	private boolean enabled;

	@Shadow
	private int x;

	@Shadow
	private int y;

	@Shadow
	private int width;

	@Shadow
	private int height;

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!FastRenderMod.isEnabled() || !FastRenderMod.EnhancedBatching) {
			return super.equals(o);
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final MixinScissorState that = (MixinScissorState) o;
		return enabled == that.enabled && x == that.x && y == that.y && width == that.width && height == that.height;
	}

	@Override
	public int hashCode() {
		if (!FastRenderMod.isEnabled() || !FastRenderMod.EnhancedBatching) {
			return super.hashCode();
		}
		return Objects.hash(enabled, x, y, width, height);
	}

}