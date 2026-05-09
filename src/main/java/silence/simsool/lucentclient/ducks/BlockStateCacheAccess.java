package silence.simsool.lucentclient.ducks;

import net.minecraft.world.phys.shapes.VoxelShape;

public interface BlockStateCacheAccess {

	VoxelShape getCollisionShape();

	void setCollisionShape(VoxelShape newShape);

	boolean[] getFaceSturdy();

	void setFaceSturdy(boolean[] newFaceSturdyArray);

}