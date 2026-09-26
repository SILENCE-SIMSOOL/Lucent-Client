package silence.simsool.lucentclient.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.entity.Display;

@Mixin(Display.class)
public interface DisplayAccessor {

	@Invoker
	void invokeSetWidth(float width);

	@Invoker
	void invokeSetHeight(float height);

}