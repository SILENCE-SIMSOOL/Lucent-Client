package silence.simsool.lucentclient.mixin.mixins.asyncparticles.conditional;

import silence.simsool.lucentclient.features.asyncparticles.addon.LightCachedParticleAddon;
import net.minecraft.client.particle.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

@Pseudo
@Mixin(value = {
	HugeExplosionParticle.class,
	AttackSweepParticle.class,
	FireflyParticle.class,
	SimpleAnimatedParticle.class,
	GustParticle.class,
	TrailParticle.class,
}) // Will be replaced with the actual targets
public abstract class MixinParticles_LightCacheNoRefresh implements LightCachedParticleAddon {

	@Override
	public boolean asyncparticles$isStaticLight() {
		return true;
	}

	@Override
	public void asyncparticles$refresh() {
		asyncparticles$setLight(asyncparticles$invoke_getLightCoords(0f));
	}

}