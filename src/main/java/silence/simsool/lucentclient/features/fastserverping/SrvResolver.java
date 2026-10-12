package silence.simsool.lucentclient.features.fastserverping;

import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.Context;
import javax.naming.directory.Attribute;
import javax.naming.directory.InitialDirContext;

public final class SrvResolver {

	public static final long TTL_MS = 300_000L;
	private static final ConcurrentHashMap<String, Cached> CACHE = new ConcurrentHashMap<>();

	public static Resolved resolve(String host, int port) {
		if (port != 25565) {
			return new Resolved(host, port);
		}

		Cached hit = CACHE.get(host);
		if (hit != null && System.currentTimeMillis() - hit.timeMs() < TTL_MS) {
			return hit.resolved();
		}

		try {
			Hashtable<String, String> env = new Hashtable<>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
			Attribute srv = new InitialDirContext(env).getAttributes("_minecraft._tcp." + host, new String[]{"SRV"}).get("SRV");

			Resolved resolved;
			if (srv == null) {
				resolved = new Resolved(host, port);
			} else {
				String[] p = srv.get().toString().split(" ");
				String h = p[3];
				resolved = new Resolved(
					h.charAt(h.length() - 1) == '.' ? h.substring(0, h.length() - 1) : h,
					Integer.parseInt(p[2])
				);
			}

			CACHE.put(host, new Cached(resolved, System.currentTimeMillis()));
			return resolved;
		} catch (Exception e) {
			return new Resolved(host, port);
		}
	}

	private record Cached(Resolved resolved, long timeMs) {}

}