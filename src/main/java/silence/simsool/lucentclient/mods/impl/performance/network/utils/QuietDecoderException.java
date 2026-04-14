package silence.simsool.lucentclient.mods.impl.performance.network.utils;

import io.netty.handler.codec.DecoderException;

public class QuietDecoderException extends DecoderException {

	private static final long serialVersionUID = -060414L;

	public QuietDecoderException(String message) {
		super(message);
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}

}