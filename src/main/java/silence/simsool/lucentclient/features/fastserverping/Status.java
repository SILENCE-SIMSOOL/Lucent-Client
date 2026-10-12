package silence.simsool.lucentclient.features.fastserverping;

import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.server.players.NameAndId;

public record Status(Component motd, int online, int max, String version, int protocol, long ping, ServerStatus.Favicon favicon, List<NameAndId> sample, boolean playersPresent, StatusType type) {}