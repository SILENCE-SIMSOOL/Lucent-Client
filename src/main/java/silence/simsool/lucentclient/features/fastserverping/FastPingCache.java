package silence.simsool.lucentclient.features.fastserverping;

import java.util.LinkedHashMap;
import java.util.Map;

public final class FastPingCache {

	public static final long TTL_MS = 15_000L;
	private static final int MAX_ENTRIES = 256;
	private static final Map<String, CacheEntry> MAP = new LinkedHashMap<>(MAX_ENTRIES, 0.75f, true);

	public static synchronized CacheEntry get(String key) {
		CacheEntry entry = MAP.get(key);
		if (entry != null && !isFresh(entry)) {
			MAP.remove(key);
			return null;
		}
		return entry;
	}

	public static synchronized void put(String key, Status status) {
		MAP.put(key, new CacheEntry(status, System.currentTimeMillis()));
		if (MAP.size() > MAX_ENTRIES) MAP.remove(MAP.keySet().iterator().next());
	}

	public static boolean isFresh(CacheEntry e) {
		return System.currentTimeMillis() - e.timeMs() < TTL_MS;
	}

}