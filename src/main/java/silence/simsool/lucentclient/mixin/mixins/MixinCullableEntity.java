package silence.simsool.lucentclient.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import silence.simsool.lucentclient.mods.impl.performance.culling.Cullable;

@Mixin(Entity.class)
public abstract class MixinCullableEntity implements Cullable {

	@Unique
	private boolean lucent$culled = false;

	@Unique
	private AABB lucent$cullingBox;

	@Unique
	private double lucent$distanceSq;

	@Override
	public boolean isCulled() {
		return this.lucent$culled;
	}

	@Override
	public void setCulled(boolean culled) {
		this.lucent$culled = culled;
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

}