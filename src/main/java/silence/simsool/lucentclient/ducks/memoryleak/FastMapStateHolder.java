package silence.simsool.lucentclient.ducks.memoryleak;

import silence.simsool.lucentclient.mods.impl.performance.memory.impl.fastmap.FastMap;

public interface FastMapStateHolder<S> {
	void lucentclient_setStateMap(FastMap<S> stateMap, int tableIndex);
}