package silence.simsool.lucentclient.mixin.mixins.entityculling;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import silence.simsool.lucentclient.mods.impl.performance.EntityCullingMod;
import silence.simsool.lucentclient.mods.impl.performance.culling.Cullable;

@Mixin(value = { Entity.class, BlockEntity.class })
public abstract class MixinCullableEntity implements Cullable {

	@Unique
	private boolean lucent$culled = false;

	@Unique
	private boolean lucent$outOfCamera = false;

	@Unique
	private boolean lucent$glowing = false;

	@Unique
	private long lucent$forcedVisibleUntil = 0L;

	@Unique
	private AABB lucent$cullingBox;

	@Unique
	private BlockPos lucent$cullingBlockPos;

	@Unique
	private double lucent$distanceSq;

	@Override
	public boolean isCulled() {
		if (!EntityCullingMod.isEnabled()) {
			return false;
		}
		return this.lucent$culled;
	}

	@Override
	public void setCulled(boolean culled) {
		this.lucent$culled = culled;
		if (!culled) {
			setTimeout();
		}
	}

	@Override
	public AABB getCullingBox() {
		return this.lucent$cullingBox;
	}

	@Override
	public void setCullingBox(AABB box) {
		this.lucent$cullingBox = box;
	}

	@Override
	public double getDistanceSq() {
		return this.lucent$distanceSq;
	}

	@Override
	public void setDistanceSq(double distSq) {
		this.lucent$distanceSq = distSq;
	}

	@Override
	public boolean isForcedVisible() {
		return this.lucent$forcedVisibleUntil > System.currentTimeMillis();
	}

	@Override
	public void setTimeout() {
		this.lucent$forcedVisibleUntil = System.currentTimeMillis() + 1000L;
	}

	@Override
	public boolean isOutOfCamera() {
		if (!EntityCullingMod.isEnabled()) {
			return false;
		}
		return this.lucent$outOfCamera;
	}

	@Override
	public void setOutOfCamera(boolean outOfCamera) {
		this.lucent$outOfCamera = outOfCamera;
	}

	@Override
	public boolean isShouldEntityAppearGlowing() {
		return this.lucent$glowing;
	}

	@Override
	public void setShouldEntityAppearGlowing(boolean glowing) {
		this.lucent$glowing = glowing;
	}

	@Override
	public BlockPos getCullingBlockPos() {
		return this.lucent$cullingBlockPos;
	}

	@Override
	public void setCullingBlockPos(BlockPos pos) {
		this.lucent$cullingBlockPos = pos;
	}

}