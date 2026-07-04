package silence.simsool.lucentclient.mixin.mixins.memoryleak.fastmap;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import silence.simsool.lucentclient.mods.impl.performance.memory.MemoryLeakFixMod;
import silence.simsool.lucentclient.mixin.accessors.memoryleak.StateHolderAccess;
import silence.simsool.lucentclient.mods.impl.performance.memory.impl.FastMapStateHolderImpl;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Mixin(StateDefinition.class)
public class MixinStateDefinition {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Redirect(method = "createMultiPropertyStates", at = @At(value = "INVOKE", target = "Ljava/util/Map;forEach(Ljava/util/function/BiConsumer;)V"))
	private static <O, S extends StateHolder<O, S>>
	void initializeNeighborsMultiProp(Map<List<Comparable<?>>, S> states, BiConsumer<?, ?> v) {
		if (MemoryLeakFixMod.isEnabled() && MemoryLeakFixMod.ReplaceNeighborLookup) {
			FastMapStateHolderImpl.initializeFastMap(states.values());
		} else {
			((Map) states).forEach((BiConsumer) v);
		}
	}

	@Redirect(method = "createSinglePropertyStates(Ljava/lang/Object;Lnet/minecraft/world/level/block/state/StateDefinition$Factory;Lnet/minecraft/world/level/block/state/properties/Property;)Lcom/google/common/collect/ImmutableList;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/StateHolder;initializeNeighbors([[Ljava/lang/Object;)V"))
	private static <S> void skipSetNeighbors(StateHolder<?, ?> instance, S[][] neighbors) {
		if (MemoryLeakFixMod.isEnabled() && MemoryLeakFixMod.ReplaceNeighborLookup) {
			// Do nothing
		} else {
			((StateHolderAccess) instance).callInitializeNeighbors((Object[][]) neighbors);
		}
	}

	@WrapOperation(method = "createSinglePropertyStates(Ljava/lang/Object;Lnet/minecraft/world/level/block/state/StateDefinition$Factory;Lnet/minecraft/world/level/block/state/properties/Property;)Lcom/google/common/collect/ImmutableList;", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList$Builder;build()Lcom/google/common/collect/ImmutableList;"))
	private static <O, S extends StateHolder<O, S>, T extends Comparable<T>> ImmutableList<S> initSinglePropertyFastMap(ImmutableList.Builder<S> instance, Operation<ImmutableList<S>> original, O owner, StateDefinition.Factory<O, S> factory, Property<T> property) {
		final var states = original.call(instance);
		if (MemoryLeakFixMod.isEnabled() && MemoryLeakFixMod.ReplaceNeighborLookup) {
			FastMapStateHolderImpl.initializeFastMap(states);
		}
		return states;
	}

}