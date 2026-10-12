package silence.simsool.lucentclient.features.fastserverping;

import java.util.concurrent.ConcurrentHashMap;

public final class FastPingCache {

	public static final long TTL_MS = 15_000L;
	private static final ConcurrentHashMap<String, CacheEntry> MAP = new ConcurrentHashMap<>();

	public static CacheEntry get(String key) {
		return MAP.get(key);
	}

	public static void put(String key, Status status) {
		MAP.put(key, new CacheEntry(status, System.currentTimeMillis()));
	}

	public static boolean isFresh(CacheEntry e) {
		return System.currentTimeMillis() - e.timeMs() < TTL_MS;
	}

}