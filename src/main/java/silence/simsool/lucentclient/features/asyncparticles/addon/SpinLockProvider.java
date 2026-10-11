package silence.simsool.lucentclient.features.asyncparticles.addon;

import silence.simsool.lucentclient.features.asyncparticles.util.SpinLock;

public interface SpinLockProvider {
	/**
	 * Mark as default for calling in Mixins.
	 */
	default SpinLock asyncparticles$getSpinLock() {
		throw new UnsupportedOperationException("Missing implementation.");
	}
}