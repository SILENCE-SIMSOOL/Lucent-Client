package silence.simsool.lucentclient.mixin.mixins.optimization.compat.iris;

import net.minecraft.client.renderer.rendertype.RenderType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import silence.simsool.lucentclient.features.fastrender.IIris_OuterWrappedRenderType;

@Pseudo
@Mixin(targets = "net.irisshaders.iris.layer.OuterWrappedRenderType")
public abstract class MixinIris_OuterWrappedRenderType implements IIris_OuterWrappedRenderType {

	@Shadow
	@Final
	private RenderType wrapped;

	@Override
	public RenderType immediatelyFast$getWrapped() {
		return this.wrapped;
	}

}