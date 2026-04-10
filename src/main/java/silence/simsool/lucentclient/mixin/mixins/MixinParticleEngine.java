package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.TerrainParticle;
import silence.simsool.lucentclient.mods.impl.graphics.ParticlesMod;

@Mixin(ParticleEngine.class)
public class MixinParticleEngine {

	@Inject(method = "add", at = @At("HEAD"), cancellable = true)
	private void onAddParticle(Particle particle, CallbackInfo ci) {
		if (ParticlesMod.isEnabled()) {

			if (ParticlesMod.DisableBreakingParticles) {
				if (particle instanceof TerrainParticle) {
					ci.cancel();
					return;
				}
			}

			if (ParticlesMod.DisableExplosionParticles) {
				if (particle.getClass().getSimpleName().contains("Explosion")) {
					ci.cancel();
					return;
				}
			}

		}
	}

}