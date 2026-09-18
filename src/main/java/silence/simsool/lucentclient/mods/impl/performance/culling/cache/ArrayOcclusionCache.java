package silence.simsool.lucentclient.mods.impl.performance.culling.cache;

import java.util.Arrays;

public class ArrayOcclusionCache implements OcclusionCache {

	private final int reachX2;
	private final byte[] cache;
	private int positionKey;
	private int entry;
	private int offset;

	public ArrayOcclusionCache(int reach) {
		this.reachX2 = reach * 2;
		this.cache = new byte[(reachX2 * reachX2 * reachX2) / 4];
	}

	@Override
	public void resetCache() {
		Arrays.fill(this.cache, (byte) 0);
	}

	@Override
	public void setVisible(int x, int y, int z) {
		this.positionKey = x + y * this.reachX2 + z * this.reachX2 * this.reachX2;
		this.entry = this.positionKey / 4;
		this.offset = (this.positionKey % 4) * 2;
		this.cache[this.entry] |= (byte) (1 << this.offset);
	}

	@Override
	public void setHidden(int x, int y, int z) {
		this.positionKey = x + y * this.reachX2 + z * this.reachX2 * this.reachX2;
		this.entry = this.positionKey / 4;
		this.offset = (this.positionKey % 4) * 2;
		this.cache[this.entry] |= (byte) (1 << (this.offset + 1));
	}

	@Override
	public int getState(int x, int y, int z) {
		this.positionKey = x + y * this.reachX2 + z * this.reachX2 * this.reachX2;
		this.entry = this.positionKey / 4;
		this.offset = (this.positionKey % 4) * 2;
		return this.cache[this.entry] >> this.offset & 3;
	}

	@Override
	public void setLastVisible() {
		this.cache[this.entry] |= (byte) (1 << this.offset);
	}

	@Override
	public void setLastHidden() {
		this.cache[this.entry] |= (byte) (1 << (this.offset + 1));
	}

}