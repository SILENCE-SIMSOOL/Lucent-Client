package silence.simsool.lucentclient.mods.impl.performance.culling;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;

public interface Cullable {

	boolean isCulled();

	void setCulled(boolean culled);

	AABB getCullingBox();

	void setCullingBox(AABB box);

	double getDistanceSq();

	void setDistanceSq(double distSq);

	boolean isForcedVisible();

	void setTimeout();

	boolean isOutOfCamera();

	void setOutOfCamera(boolean outOfCamera);

	boolean isShouldEntityAppearGlowing();

	void setShouldEntityAppearGlowing(boolean glowing);

	BlockPos getCullingBlockPos();

	void setCullingBlockPos(BlockPos pos);

}