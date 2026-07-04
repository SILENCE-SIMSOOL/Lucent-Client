package silence.simsool.lucentclient.mods.impl.performance.memory.impl.fastmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import net.minecraft.world.level.block.state.properties.Property;

/**
 * Maps a Property->Value assignment to a value, while allowing fast access to "neighbor" states
 */
public class FastMap<Value> {

	private final Property<?>[] properties;
	private final List<FastMapKey> keys;
	private final List<Value> valueMatrix;

	public FastMap(Property<?>[] properties, boolean compact) {
		this.properties = properties;
		for (Property<?> prop : properties) {
			for (int j = 0; j < prop.getPossibleValues().size(); j++) {
				Preconditions.checkState(getIndexUnsafe(prop, (Comparable<?>) prop.getPossibleValues().get(j)) == j);
			}
		}

		List<FastMapKey> keysList = new ArrayList<>(properties.length);
		int factorUpTo = 1;
		for (int propIndex = 0; propIndex < properties.length; propIndex++) {
			FastMapKey nextKey;
			if (compact) {
				nextKey = new CompactFastMapKey(factorUpTo, properties[propIndex].getPossibleValues().size());
			} else {
				nextKey = BinaryFastMapKey.create(factorUpTo, properties[propIndex].getPossibleValues().size());
			}
			keysList.add(nextKey);
			factorUpTo *= nextKey.getFactorToNext();
		}

		this.keys = ImmutableList.copyOf(keysList);
		List<Value> valuesList = new ArrayList<>(factorUpTo);
		for (int i = 0; i < factorUpTo; i++) {
			valuesList.add(null);
		}
		this.valueMatrix = valuesList;
	}

	@Nullable
	public Value with(int oldIndex, int propertyIndex, int valueIndex) {
		int newIndex = this.keys.get(propertyIndex).replaceIn(oldIndex, valueIndex);
		if (newIndex < 0 || this.valueMatrix.get(newIndex) == null) {
			throw new RuntimeException("Invalid access to FastMap: Replacing " + propertyIndex + " to " + valueIndex + " in " + oldIndex + " with properties " + Arrays.toString(this.properties));
		}
		return this.valueMatrix.get(newIndex);
	}

	public int getIndexOf(Value state, PropertyValueGetter<Value> getValue) {
		int id = 0;
		for (int i = 0; i < this.properties.length; i++) {
			int valueIndex = getIndexUnsafe(this.properties[i], getValue.getValue(state, this.properties[i]));
			id += this.keys.get(i).toPartialMapIndex(valueIndex);
		}
		return id;
	}

	public int insertAtIndex(Value state, PropertyValueGetter<Value> getValue) {
		int index = this.getIndexOf(state, getValue);
		Preconditions.checkState(this.valueMatrix.get(index) == null);
		this.valueMatrix.set(index, state);
		return index;
	}

	public int getValueIndex(int stateIndex, int propertyIndex) {
		return this.keys.get(propertyIndex).getIndexIn(stateIndex);
	}

	public Property<?>[] getProperties() {
		return this.properties;
	}

	@SuppressWarnings("unchecked")
	private static <T extends Comparable<T>> int getIndexUnsafe(Property<T> property, Comparable<?> value) {
		return property.getInternalIndex((T) value);
	}

	public interface PropertyValueGetter<Owner> {
		Comparable<?> getValue(Owner state, Property<?> property);
	}

}