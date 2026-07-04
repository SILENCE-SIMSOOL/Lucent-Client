package silence.simsool.lucentclient.mods.impl.performance.memory.impl.hash;

import java.util.Objects;

import it.unimi.dsi.fastutil.Hash;
import silence.simsool.lucentclient.mixin.accessors.memoryleak.SliceShapeAccess;

public class SliceShapeHash implements Hash.Strategy<SliceShapeAccess> {

	public static final SliceShapeHash INSTANCE = new SliceShapeHash();

	@Override
	public int hashCode(SliceShapeAccess o) {
		int result = Objects.hashCode(o.getAxis());
		result = 31 * result + DiscreteVSHash.INSTANCE.hashCode(o.getShape());
		result = 31 * result + VoxelShapeHash.INSTANCE.hashCode(o.getDelegate());
		return result;
	}

	@Override
	public boolean equals(SliceShapeAccess a, SliceShapeAccess b) {
		if (a == b) return true;
		else if (a == null || b == null) return false;

		return Objects.equals(a.getAxis(), b.getAxis())
				&& VoxelShapeHash.INSTANCE.equals(a.getDelegate(), b.getDelegate())
				&& DiscreteVSHash.INSTANCE.equals(a.getShape(), b.getShape()
		);
	}

}