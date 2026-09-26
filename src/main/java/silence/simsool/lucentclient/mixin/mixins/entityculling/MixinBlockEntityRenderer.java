package silence.simsool.lucentclient.mixin.mixins.entityculling;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.level.block.entity.BlockEntity;
import silence.simsool.lucentclient.mods.impl.performance.culling.BlockEntityRenderFabricExtension;

@Mixin(BlockEntityRenderer.class)
public interface MixinBlockEntityRenderer<T extends BlockEntity, S extends BlockEntityRenderState> extends BlockEntityRenderFabricExtension<T> {}