package silence.simsool.lucentclient.mixin.mixins.asyncparticles.fabric.core.particle.render;

import silence.simsool.lucentclient.features.asyncparticles.addon.ParticleEngineAddon;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(ParticleEngine.class)
public class MixinParticleEngine implements ParticleEngineAddon {

	@Mutable
	@Shadow
	@Final
	private static List<ParticleRenderType> RENDER_ORDER;

	@Override
	public void asyncparticle$addRenderType(ParticleRenderType particleRenderType) {
		if (RENDER_ORDER.contains(particleRenderType)) {
			return;
		}
		List<ParticleRenderType> renderOrder = RENDER_ORDER;
		if (!(renderOrder instanceof ArrayList<ParticleRenderType>)) {
			renderOrder = RENDER_ORDER = new ArrayList<>(RENDER_ORDER);
		}
		renderOrder.add(particleRenderType);
	}

}