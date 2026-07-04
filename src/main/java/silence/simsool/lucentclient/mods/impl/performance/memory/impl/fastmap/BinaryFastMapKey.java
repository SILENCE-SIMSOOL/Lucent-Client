package silence.simsool.lucentclient.mods.impl.performance.memory.impl.fastmap;

import com.google.common.base.Preconditions;
import net.minecraft.util.Mth;

/**
 * A bitmask-based implementation of a FastMapKey. This reduces the density of data in the value matrix, but allows
 * accessing values with only some bitwise operations, which are much faster than integer division
 */

public record BinaryFastMapKey(int numValues, byte firstBitInValue, byte firstBitAfterValue) implements FastMapKey {
	public BinaryFastMapKey(int numValues, byte firstBitInValue, byte firstBitAfterValue) {
		this.numValues = numValues;
		this.firstBitInValue = firstBitInValue;
		this.firstBitAfterValue = firstBitAfterValue;
	}

	public static BinaryFastMapKey create(int mapFactor, int numValues) {
		Preconditions.checkArgument(Mth.isPowerOfTwo(mapFactor));
		int addedFactor = Mth.smallestEncompassingPowerOfTwo(numValues);
		Preconditions.checkState(numValues <= addedFactor);
		Preconditions.checkState(addedFactor < 2 * numValues);
		int setBitInBaseFactor = Mth.log2(mapFactor);
		int setBitInAddedFactor = Mth.log2(addedFactor);
		Preconditions.checkState(setBitInBaseFactor + setBitInAddedFactor <= 31);
		return new BinaryFastMapKey(numValues, (byte) setBitInBaseFactor, (byte) (setBitInBaseFactor + setBitInAddedFactor));
	}

	public int replaceIn(int mapIndex, int valueIndex) {
		if (valueIndex >= this.numValues) return -1;
		else {
			int keepMask = ~lowestNBits(this.firstBitAfterValue) | lowestNBits(this.firstBitInValue);
			return keepMask & mapIndex | this.toPartialMapIndex(valueIndex);
		}
	}

	public int toPartialMapIndex(int internalIndex) {
		return internalIndex << this.firstBitInValue;
	}

	public int getFactorToNext() {
		return 1 << this.firstBitAfterValue - this.firstBitInValue;
	}

	public int getIndexIn(int mapIndex) {
		return mapIndex >> this.firstBitInValue & lowestNBits((byte) (this.firstBitAfterValue - this.firstBitInValue));
	}

	private static int lowestNBits(byte n) {
		return n >= 32 ? -1 : (1 << n) - 1;
	}

	public int numValues() {
		return this.numValues;
	}

	public byte firstBitInValue() {
		return this.firstBitInValue;
	}

	public byte firstBitAfterValue() {
		return this.firstBitAfterValue;
	}

}