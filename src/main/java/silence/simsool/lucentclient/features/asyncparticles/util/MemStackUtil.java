package silence.simsool.lucentclient.features.asyncparticles.util;

import org.lwjgl.system.MemoryStack;

public class MemStackUtil {
	private static final ParticleThreadLocal<MemoryStack> MEMORY_STACKS = ParticleThreadLocal.withInitial(ThreadUtil::isOnMainThread, MemoryStack::create);

	public static MemoryStack stackPush() {
		return stackGet().push();
	}

	public static MemoryStack stackGet() {
		return MEMORY_STACKS.getSafe(MemoryStack::stackGet);
	}
}