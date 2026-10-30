package silence.simsool.lucentclient.mixin.mixins.asyncparticles.conditional;

import silence.simsool.lucentclient.features.asyncparticles.addon.SpinLockProvider;
import silence.simsool.lucentclient.features.asyncparticles.util.ReentrantSpinLock;
import silence.simsool.lucentclient.features.asyncparticles.util.SpinLock;
import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;

@Pseudo
@Mixin(Particle.class) // Will be replaced with the actual targets
public class MixinParticles_LockProvider implements SpinLockProvider {

	@Unique
	protected SpinLock asyncparticles$lock = new ReentrantSpinLock();

	@Override
	public SpinLock asyncparticles$getSpinLock() {
		return asyncparticles$lock;
	}

}