package silence.simsool.lucentclient.mods.impl.performance.memory.impl.fastmap;

/**
 * A "compact" implementation of a FastMapKey, i.e. one which completely fills the value matrix
 */
public record CompactFastMapKey(int mapFactor, int numValues) implements FastMapKey {

	public CompactFastMapKey(int mapFactor, int numValues) {
		this.mapFactor = mapFactor;
		this.numValues = numValues;
	}

	public int replaceIn(int mapIndex, int valueIndex) {
		if (valueIndex >= this.numValues) return -1;
		else {
			int lowerData = mapIndex % this.mapFactor;
			int upperFactor = this.mapFactor * this.numValues;
			int upperData = mapIndex - mapIndex % upperFactor;
			return lowerData + this.toPartialMapIndex(valueIndex) + upperData;
		}
	}

	public int toPartialMapIndex(int internalIndex) {
		return this.mapFactor * internalIndex;
	}

	public int getFactorToNext() {
		return this.numValues;
	}

	public int getIndexIn(int mapIndex) {
		return mapIndex / this.mapFactor % this.numValues;
	}

	public int mapFactor() {
		return this.mapFactor;
	}

	public int numValues() {
		return this.numValues;
	}

}