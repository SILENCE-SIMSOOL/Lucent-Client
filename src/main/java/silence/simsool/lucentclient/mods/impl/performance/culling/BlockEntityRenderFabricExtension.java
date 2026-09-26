package silence.simsool.lucentclient.mods.impl.performance.culling;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

public interface BlockEntityRenderFabricExtension<T extends BlockEntity> {

	default AABB getRenderBoundingBox(T blockEntity) {
		return new AABB(blockEntity.getBlockPos());
	}

}