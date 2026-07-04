package silence.simsool.lucentclient.mods.impl.performance.memory.impl;

import java.util.Collection;

import net.minecraft.world.level.block.state.StateHolder;
import silence.simsool.lucentclient.ducks.memoryleak.FastMapStateHolder;
import silence.simsool.lucentclient.mixin.accessors.memoryleak.StateHolderAccess;
import silence.simsool.lucentclient.mods.impl.performance.memory.MemoryLeakFixMod;
import silence.simsool.lucentclient.mods.impl.performance.memory.impl.fastmap.FastMap;

public class FastMapStateHolderImpl {

	@SuppressWarnings("unchecked")
	public static <S extends StateHolder<?, S>> void initializeFastMap(Collection<S> states) {
		S stateHolder = states.iterator().next();
		FastMap<S> mainMap = new FastMap<>(((StateHolderAccess) stateHolder).getPropertyKeys(), MemoryLeakFixMod.CompactFastMap);
		for (S stateHolder1 : states) {
			final int index = mainMap.insertAtIndex(stateHolder1, StateHolder::getValue);
			((FastMapStateHolder<S>) stateHolder1).lucentclient_setStateMap(mainMap, index);
		}
	}

	public static Comparable<?> getPropertyValue(Comparable<?>[] vanillaValues, FastMap<?> fastMap, int fastMapIndex, int propertyIndex) {
		if (vanillaValues != null) return vanillaValues[propertyIndex];
		else return getPropertyValue(fastMap, fastMapIndex, propertyIndex);
	}

	public static Comparable<?> getPropertyValue(FastMap<?> fastMap, int fastMapIndex, int propertyIndex) {
		int internalIndex = fastMap.getValueIndex(fastMapIndex, propertyIndex);
		return fastMap.getProperties()[propertyIndex].getPossibleValues().get(internalIndex);
	}

}