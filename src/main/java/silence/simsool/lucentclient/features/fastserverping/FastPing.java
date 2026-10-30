package silence.simsool.lucentclient.features.fastserverping;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.ScheduledFuture;
import net.minecraft.DetectedVersion;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.server.players.NameAndId;

public final class FastPing {

	public static final EventLoopGroup GROUP = new MultiThreadIoEventLoopGroup(
		4,
		new ThreadFactoryBuilder()
			.setNameFormat("FastPing-Netty #%d")
			.setDaemon(true)
			.build(),
		NioIoHandler.newFactory()
	);

	private static final ThreadPoolExecutor PINGER = new ThreadPoolExecutor(
		32,
		32,
		0L,
		TimeUnit.MILLISECONDS,
		new LinkedBlockingQueue<>(256),
		new ThreadFactoryBuilder()
			.setNameFormat("FastPing #%d")
			.setDaemon(true)
			.build(),
		new ThreadPoolExecutor.AbortPolicy()
	);

	public static CompletableFuture<Status> ping(String host, int port, Consumer<Status> listener) {
		CompletableFuture<Status> future = new CompletableFuture<>();
		Resolved resolved = SrvResolver.resolve(host, port);

		if (GROUP.isShutdown()) {
			return CompletableFuture.failedFuture(new IllegalStateException("EventLoopGroup has been shut down"));
		}

		Bootstrap b = new Bootstrap()
			.group(GROUP)
			.channel(NioSocketChannel.class)
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
			.option(ChannelOption.TCP_NODELAY, true)
			.handler(new ChannelInitializer<>() {
				@Override
				protected void initChannel(Channel ch) {
					ch.pipeline().addLast(new PingHandler(future, resolved.host(), resolved.port(), listener));
				}
			});

		var channel = b.connect(InetSocketAddress.createUnresolved(resolved.host(), resolved.port()))
			.addListener((ChannelFutureListener) f -> {
				if (!f.isSuccess()) {
					future.completeExceptionally(f.cause());
				}
			});

		var deadline = GROUP.schedule(() -> {
			if (!future.isDone()) {
				future.completeExceptionally(new TimeoutException("Ping deadline exceeded"));
				channel.channel().close();
			}
		}, 10, TimeUnit.SECONDS);
		future.whenComplete((s, e) -> {
			deadline.cancel(false);
			channel.channel().close();
		});

		return future;
	}

	public static ThreadPoolExecutor pinger() {
		return PINGER;
	}

	public static EventLoopGroup eventLoopGroup() {
		return GROUP;
	}

	private static Status parse(String json, long ping, StatusType type) {
		JsonObject root = JsonParser.parseString(json).getAsJsonObject();
		Component motd = parseMotd(root);

		JsonObject versionObj = root.getAsJsonObject("version");
		String version = versionObj != null && versionObj.has("name")
			? versionObj.get("name").getAsString()
			: "";

		int protocol = versionObj != null && versionObj.has("protocol")
			? versionObj.get("protocol").getAsInt()
			: 0;

		JsonObject players = root.getAsJsonObject("players");
		int online = players != null && players.has("online")
			? players.get("online").getAsInt()
			: 0;

		int max = players != null && players.has("max")
			? players.get("max").getAsInt()
			: 0;

		List<NameAndId> sample = new ArrayList<>();
		if (players != null && players.has("sample")) {
			for (JsonElement element : players.getAsJsonArray("sample")) {
				JsonObject playerObj = element.getAsJsonObject();
				UUID id;
				try {
					id = playerObj.has("id") ? UUID.fromString(playerObj.get("id").getAsString()) : UUID.randomUUID();
				} catch (IllegalArgumentException e) {
					id = UUID.randomUUID();
				}
				sample.add(new NameAndId(id, playerObj.has("name") ? playerObj.get("name").getAsString() : ""));
			}
		}

		ServerStatus.Favicon favicon = null;
		if (root.has("favicon")) {
			favicon = ServerStatus.Favicon.CODEC
				.parse(JsonOps.INSTANCE, root.get("favicon"))
				.result()
				.orElse(null);
		}

		return new Status(
			motd,
			online,
			max,
			version,
			protocol,
			ping,
			favicon,
			sample,
			players != null,
			type
		);
	}

	private static Component parseMotd(JsonObject root) {
		try {
			JsonElement desc = root.get("description");
			if (desc == null) return Component.literal("");
			return ComponentSerialization.CODEC
				.parse(JsonOps.INSTANCE, desc)
				.result()
				.orElse(Component.empty());
		} catch (Exception e) {
			return Component.literal("");
		}
	}

	private static int getTargetProtocol() {
		try {
			Class<?> vfpClass = Class.forName("com.viaversion.viafabricplus.ViaFabricPlus");
			Method getImpl = vfpClass.getMethod("getImpl");
			Object impl = getImpl.invoke(null);
			Method getTargetVersion = impl.getClass().getMethod("getTargetVersion");
			Object targetVersion = getTargetVersion.invoke(impl);
			Method getVersion = targetVersion.getClass().getMethod("getVersion");
			return (int) getVersion.invoke(targetVersion);
		} catch (Throwable ignored) {
			return DetectedVersion.tryDetectVersion().protocolVersion();
		}
	}

	private static final class PingHandler extends ChannelInboundHandlerAdapter {

		private final CompletableFuture<Status> future;
		private final String host;
		private final int port;
		private final Consumer<Status> listener;

		private final ByteBuf cumulation = Unpooled.buffer();
		private long pingStart;
		private String statusJson;
		private ScheduledFuture<?> timeout;

		PingHandler(CompletableFuture<Status> future, String host, int port, Consumer<Status> listener) {
			this.future = future;
			this.host = host;
			this.port = port;
			this.listener = listener;
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) {
			timeout = ctx.executor().schedule(() -> {
				if (!future.isDone()) {
					future.completeExceptionally(new TimeoutException("Ping timeout"));
					ctx.close();
				}
			}, 7, TimeUnit.SECONDS);

			ByteBuf handshake = handshakeBuf();
			ByteBuf statusReq = statusRequestBuf();

			ctx.write(handshake);
			ctx.writeAndFlush(statusReq);
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) {
			ByteBuf in = (ByteBuf) msg;
			try {
				cumulation.writeBytes(in);
			} finally {
				in.release();
			}

			while (true) {
				cumulation.markReaderIndex();
				try {
					int packetLen = FSPVarIntUtils.readVarInt(cumulation);
					if (cumulation.readableBytes() < packetLen) {
						cumulation.resetReaderIndex();
						return;
					}

					ByteBuf packet = cumulation.readSlice(packetLen);
					handlePacket(ctx, packet);
					if (ctx.isRemoved()) {
						return;
					}
				} catch (IndexOutOfBoundsException e) {
					cumulation.resetReaderIndex();
					return;
				}
			}
		}

		@Override
		public void channelInactive(ChannelHandlerContext ctx) {
			if (!future.isDone()) {
				future.completeExceptionally(new IOException("Channel closed"));
			}
		}

		@Override
		public void handlerRemoved(ChannelHandlerContext ctx) {
			if (timeout != null) {
				timeout.cancel(false);
			}
			cumulation.release();
		}

		private void handlePacket(ChannelHandlerContext ctx, ByteBuf packet) {
			int id = FSPVarIntUtils.readVarInt(packet);

			if (id == 0) {
				int len = FSPVarIntUtils.readVarInt(packet);
				byte[] arr = new byte[len];
				packet.readBytes(arr);
				statusJson = new String(arr, StandardCharsets.UTF_8);

				listener.accept(parse(statusJson, -1L, StatusType.EARLY));

				pingStart = System.nanoTime();
				ctx.writeAndFlush(pingPacket(pingStart));
			} else if (id == 1) {
				packet.readLong();
				long ping = (System.nanoTime() - pingStart) / 1_000_000;

				Status s = parse(statusJson, ping, StatusType.COMPLETE);
				if (timeout != null) {
					timeout.cancel(false);
				}
				listener.accept(s);
				future.complete(s);
				ctx.close();
			}
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
			future.completeExceptionally(cause);
			ctx.close();
		}

		private ByteBuf handshakeBuf() {
			ByteBuf inner = Unpooled.buffer();
			FSPVarIntUtils.writeVarInt(inner, 0);
			FSPVarIntUtils.writeVarInt(inner, getTargetProtocol());
			FSPVarIntUtils.writeVarInt(inner, host.length());
			inner.writeCharSequence(host, StandardCharsets.UTF_8);
			inner.writeShort(port);
			FSPVarIntUtils.writeVarInt(inner, 1);
			return frame(inner);
		}

		private ByteBuf statusRequestBuf() {
			ByteBuf inner = Unpooled.buffer();
			FSPVarIntUtils.writeVarInt(inner, 0);
			return frame(inner);
		}

		private ByteBuf pingPacket(long time) {
			ByteBuf inner = Unpooled.buffer();
			FSPVarIntUtils.writeVarInt(inner, 1);
			inner.writeLong(time);
			return frame(inner);
		}

		private ByteBuf frame(ByteBuf inner) {
			ByteBuf out = Unpooled.buffer();
			try {
				FSPVarIntUtils.writeVarInt(out, inner.readableBytes());
				out.writeBytes(inner);
				return out;
			} catch (RuntimeException | Error e) {
				out.release();
				throw e;
			} finally {
				inner.release();
			}
		}

	}

}