package silence.simsool.lucentclient.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;

@Mixin(EntityRenderer.class)
public interface EntityRendererAccessor {

	@Invoker("affectedByCulling")
	boolean invokeAffectedByCulling(Entity entity);

}