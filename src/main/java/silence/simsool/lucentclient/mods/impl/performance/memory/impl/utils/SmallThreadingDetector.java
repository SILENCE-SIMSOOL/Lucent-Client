package silence.simsool.lucentclient.mods.impl.performance.memory.impl.utils;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BooleanSupplier;

import net.minecraft.util.ThreadingDetector;
import silence.simsool.lucentclient.ducks.memoryleak.SmallThreadDetectable;

public class SmallThreadingDetector {

	public static void acquire(SmallThreadDetectable obj, String name) {
		byte oldState;

		synchronized (obj) {
			oldState = obj.lucentclient$getState();
			if (oldState == SmallThreadDetectable.UNLOCKED) {
				obj.lucentclient$setState(SmallThreadDetectable.LOCKED);
				return;
			}
			else if (oldState == SmallThreadDetectable.LOCKED) {
				GlobalCrashHandler.startCrash(obj, name);
				obj.lucentclient$setState(SmallThreadDetectable.CRASHING);
			}
		}

		if (oldState == SmallThreadDetectable.LOCKED) GlobalCrashHandler.crashAcquire(obj);
		else GlobalCrashHandler.crashBystander(obj);
	}

	public static void release(SmallThreadDetectable obj) {
		byte oldState;

		synchronized (obj) {
			oldState = obj.lucentclient$getState();
			if (oldState == SmallThreadDetectable.LOCKED) {
				obj.lucentclient$setState(SmallThreadDetectable.UNLOCKED);
				return;
			}
		}

		if (oldState == SmallThreadDetectable.CRASHING) GlobalCrashHandler.crashRelease(obj);
	}

	/**
	 * This code only runs when preparing a threading crash, so none of it needs to
	 * be remotely fast
	 */
	private static class GlobalCrashHandler {

		private static final Object MONITOR = new Object();
		private static final Map<SmallThreadDetectable, CrashingState> ACTIVE_CRASHES = new IdentityHashMap<>();

		private static void startCrash(SmallThreadDetectable owner, String name) {
			synchronized (MONITOR) {
				ACTIVE_CRASHES.put(owner, new CrashingState(name, owner));
			}
		}

		private static void crashAcquire(SmallThreadDetectable owner) {
			var state = getAndWait(owner, ThreadRole.ACQUIRE);
			throw state.mainException;
		}

		private static void crashRelease(SmallThreadDetectable owner) {
			var state = getAndWait(owner, ThreadRole.RELEASE);
			throw state.mainException;
		}

		private static void crashBystander(SmallThreadDetectable owner) {
			var state = getAndWait(owner, ThreadRole.BYSTANDER);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException x) {
				Thread.currentThread().interrupt();
			}
			throw new RuntimeException("Bystander to crash of type" + state.name + "on threads " + state.releaseThread + ", " + state.acquireThread);
		}

		private static CrashingState getAndWait(SmallThreadDetectable owner, ThreadRole role) {
			CrashingState result;
			synchronized (MONITOR) {
				result = Objects.requireNonNull(ACTIVE_CRASHES.get(owner));
			}
			result.waitUntilReady(role);
			return result;
		}
	}

	/**
	 * Data needed to produce the proper crash for race on a single
	 * SmallThreadDetectable
	 */
	private static class CrashingState {
		final String name;
		final SmallThreadDetectable owner;
		Thread acquireThread;
		Thread releaseThread;
		RuntimeException mainException;

		private CrashingState(String name, SmallThreadDetectable owner) {
			this.name = name;
			this.owner = owner;
		}

		public synchronized void waitUntilReady(ThreadRole role) {
			if (role == ThreadRole.ACQUIRE) acquireThread = Thread.currentThread();
			else if (role == ThreadRole.RELEASE) releaseThread = Thread.currentThread();

			notifyAll();
			try {
				waitUntilOrCrash(() -> acquireThread != null && releaseThread != null);
				if (role == ThreadRole.ACQUIRE) {
					mainException = ThreadingDetector.makeThreadingException(name, releaseThread);
					notifyAll();
				}
				else waitUntilOrCrash(() -> mainException != null);
			} catch (InterruptedException x) {
				Thread.currentThread().interrupt();
			}
		}

		private synchronized void waitUntilOrCrash(BooleanSupplier isReady) throws InterruptedException {
			final long maxTotalTime = 10_000;
			final var start = System.currentTimeMillis();
			while (!isReady.getAsBoolean()) {
				if (System.currentTimeMillis() - start > 6 * maxTotalTime) {
					throw new RuntimeException(
							"Threading detector crash did not find other thread, missing release call?" + " Owner: " +
							this.owner + " (ID hash: " + System.identityHashCode(this.owner) + ")" + ", time: " + System.currentTimeMillis()
					);
				}
				this.wait(maxTotalTime);
			}
		}
	}

	private enum ThreadRole {
		ACQUIRE, RELEASE, BYSTANDER
	}

}