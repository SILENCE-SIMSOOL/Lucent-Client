package silence.simsool.lucentclient.mixin.mixins.fastserverping;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.ChatFormatting;
import net.minecraft.DetectedVersion;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerStatusPinger;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.EventLoopGroupHolder;
import net.minecraft.server.players.NameAndId;
import silence.simsool.lucentclient.features.fastserverping.CacheEntry;
import silence.simsool.lucentclient.features.fastserverping.FastPing;
import silence.simsool.lucentclient.features.fastserverping.FastPingCache;
import silence.simsool.lucentclient.features.fastserverping.Status;
import silence.simsool.lucentclient.features.fastserverping.StatusType;

@Mixin(ServerStatusPinger.class)
public abstract class MixinServerStatusPinger {

	@Unique
	private static final ConcurrentHashMap<String, CompletableFuture<Status>> ACTIVE_PINGS = new ConcurrentHashMap<>();

	@Unique
	private static final MethodHandle TRANSLATING_VERSION = fastping$findTranslatingVersionSetter();

	@Unique
	private static MethodHandle fastping$findTranslatingVersionSetter() {
		try {
			Class<?> protocolVersion = Class.forName("com.viaversion.viaversion.api.protocol.version.ProtocolVersion");
			return MethodHandles.lookup().findVirtual(ServerData.class, "viaFabricPlus$setTranslatingVersion", MethodType.methodType(void.class, protocolVersion));
		} catch (Throwable e) {
			return null;
		}
	}

	@Unique
	private static void fastping$setTranslatingVersion(ServerData entry) {
		if (TRANSLATING_VERSION == null) return;
		try {
			Class<?> vfpClass = Class.forName("com.viaversion.viafabricplus.ViaFabricPlus");
			Method getImpl = vfpClass.getMethod("getImpl");
			Object impl = getImpl.invoke(null);
			Method getTargetVersion = impl.getClass().getMethod("getTargetVersion");
			Object targetVersion = getTargetVersion.invoke(impl);
			TRANSLATING_VERSION.invoke(entry, targetVersion);
		} catch (Throwable ignored) {
		}
	}

	@Unique
	private static Integer fastping$getViaFabricPlusTargetVersion() {
		try {
			Class<?> vfpClass = Class.forName("com.viaversion.viafabricplus.ViaFabricPlus");
			Method getImpl = vfpClass.getMethod("getImpl");
			Object impl = getImpl.invoke(null);
			Method getTargetVersion = impl.getClass().getMethod("getTargetVersion");
			Object targetVersion = getTargetVersion.invoke(impl);
			Method getVersion = targetVersion.getClass().getMethod("getVersion");
			return (Integer) getVersion.invoke(targetVersion);
		} catch (Throwable ignored) {
			return null;
		}
	}

	@Unique
	private static List<Component> fastping$buildPlayerListSummary(Status s) {
		List<Component> list = new ArrayList<>(s.sample().size() + 1);
		for (NameAndId p : s.sample()) {
			list.add(p.equals(MinecraftServer.ANONYMOUS_PLAYER_PROFILE)
				? Component.translatable("multiplayer.status.anonymous_player")
				: Component.literal(p.name()));
		}
		if (s.sample().size() < s.online()) {
			list.add(Component.translatable("multiplayer.status.and_more", s.online() - s.sample().size()));
		}
		return list;
	}

	@Unique
	private static void fastping$applyStatus(ServerData entry, Status s) {
		entry.motd = s.motd();

		if (s.playersPresent()) {
			entry.status = ServerStatusPinger.formatPlayerCount(s.online(), s.max());
			entry.players = new ServerStatus.Players(
				s.max(),
				s.online(),
				s.sample()
			);
			if (!s.sample().isEmpty()) entry.playerList = fastping$buildPlayerListSummary(s);
			else entry.playerList = List.of();
		} else {
			entry.status = Component.translatable("multiplayer.status.unknown").withStyle(ChatFormatting.DARK_GRAY);
		}

		if (s.version().isEmpty()) {
			entry.version = Component.translatable("multiplayer.status.old");
			entry.protocol = 0;
		} else {
			entry.version = Component.literal(s.version());
			try {
				Integer targetProtocol = fastping$getViaFabricPlusTargetVersion();
				if (targetProtocol != null && targetProtocol == s.protocol()) {
					entry.protocol = DetectedVersion.tryDetectVersion().protocolVersion();
				} else {
					entry.protocol = s.protocol();
				}
			} catch (Throwable t) {
				entry.protocol = s.protocol();
			}
		}
		fastping$setTranslatingVersion(entry);
		if (s.favicon() != null) {
			entry.setIconBytes(ServerData.validateIcon(s.favicon().iconBytes()));
		}
	}

	@Unique
	private static CompletableFuture<Status> fastping$pingWithRetry(
		String host,
		int port,
		int attempts,
		Runnable onRetry,
		Consumer<Status> listener
	) {
		String key = host + ":" + port;
		CompletableFuture<Status> future = new CompletableFuture<>();
		CompletableFuture<Status> existing = ACTIVE_PINGS.putIfAbsent(key, future);
		if (existing != null) {
			return existing.thenApply(status -> {
				listener.accept(status);
				return status;
			});
		}
		future.whenComplete((result, error) -> ACTIVE_PINGS.remove(key, future));
		try {
			FastPing.pinger().execute(() -> {
				Throwable last = null;
				for (int i = 0; i < attempts; i++) {
					if (future.isDone()) return;
					try {
						CompletableFuture<Status> ping = FastPing.ping(host, port, status -> {
							if (!future.isDone()) listener.accept(status);
						});
						future.whenComplete((result, error) -> {
							if (future.isCancelled()) ping.cancel(true);
						});
						future.complete(ping.join());
						return;
					} catch (Throwable error) {
						last = error;
					}
					if (future.isDone()) return;
					if (i + 1 < attempts) {
						try {
							onRetry.run();
						} catch (Throwable error) {
							future.completeExceptionally(error);
							return;
						}
					}
				}
				future.completeExceptionally(new CompletionException(last));
			});
		} catch (RejectedExecutionException error) {
			future.completeExceptionally(error);
		}
		return future;
	}

	@Inject(
		method = "pingServer",
		at = @At("HEAD"),
		cancellable = true
	)
	private void fastping$add(
		ServerData entry,
		Runnable saver,
		Runnable pingCallback,
		EventLoopGroupHolder backend,
		CallbackInfo ci
	) {
		ci.cancel();

		String key = entry.ip;
		CacheEntry cached = FastPingCache.get(key);

		if (cached != null && FastPingCache.isFresh(cached)) {
			Status s = cached.status();
			fastping$applyStatus(entry, s);
			entry.ping = s.ping();
		} else {
			entry.motd = Component.translatable("multiplayer.status.pinging");
			entry.ping = -1;
			entry.playerList = Collections.emptyList();
		}

		try {
			String addr = entry.ip;
			String host;
			int port;

			int idx = addr.indexOf(':');
			if (idx == -1) {
				host = addr;
				port = 25565;
			} else {
				host = addr.substring(0, idx);
				port = Integer.parseInt(addr.substring(idx + 1));
			}

			fastping$pingWithRetry(host, port, 3, () -> {
				entry.motd = Component.literal("Failed to ping server, retrying...").withStyle(ChatFormatting.YELLOW);
				entry.ping = -1;
			}, s -> {
				if (s.type() == StatusType.EARLY) {
					fastping$applyStatus(entry, s);
					pingCallback.run();
				} else if (s.type() == StatusType.COMPLETE) {
					fastping$applyStatus(entry, s);
					FastPingCache.put(key, s);
					entry.ping = s.ping();
					pingCallback.run();
				}
			}).exceptionally(e -> {
				entry.motd = Component.translatable("multiplayer.status.cannot_connect").withColor(-65536);
				entry.status = CommonComponents.EMPTY;
				entry.ping = -1;
				return null;
			});
		} catch (Throwable t) {
			entry.motd = Component.translatable("multiplayer.status.cannot_connect").withColor(-65536);
			entry.status = CommonComponents.EMPTY;
			entry.ping = -1;
		}
	}

}