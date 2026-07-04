package silence.simsool.lucentclient.mods.impl.performance.memory.impl.hash;

import java.util.Objects;

import it.unimi.dsi.fastutil.Hash;
import silence.simsool.lucentclient.mixin.accessors.memoryleak.ArrayVSAccess;

public class ArrayVoxelShapeHash implements Hash.Strategy<ArrayVSAccess> {

	public static final ArrayVoxelShapeHash INSTANCE = new ArrayVoxelShapeHash();

	@Override
	public int hashCode(ArrayVSAccess o) {
		return 31 * Objects.hash(o.getXPoints(), o.getYPoints(), o.getZPoints()) + DiscreteVSHash.INSTANCE.hashCode(o.getShape());
	}

	@Override
	public boolean equals(ArrayVSAccess a, ArrayVSAccess b) {
		if (a == b) return true;
		else if (a == null || b == null) return false;
		return Objects.equals(
				a.getXPoints(), b.getXPoints()) && Objects.equals(a.getYPoints(), b.getYPoints())
				&& Objects.equals(a.getZPoints(), b.getZPoints())
				&& DiscreteVSHash.INSTANCE.equals(a.getShape(), b.getShape()
		);
	}

}