package silence.simsool.lucentclient.mods.impl.performance.culling;

import net.minecraft.world.phys.AABB;

public interface Cullable {

	boolean isCulled();

	void setCulled(boolean culled);

	AABB getCullingBox();

	void setCullingBox(AABB box);

	double getDistanceSq();

	void setDistanceSq(double distSq);

}