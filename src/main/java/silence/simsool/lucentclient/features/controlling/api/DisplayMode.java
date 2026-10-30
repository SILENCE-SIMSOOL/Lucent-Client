package silence.simsool.lucentclient.features.controlling.api;

import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import silence.simsool.lucentclient.features.controlling.api.entries.IKeyEntry;
import silence.simsool.lucentclient.mixin.mixins.controlling.AccessKeyMapping;

public enum DisplayMode {

	ALL(entry -> true),
	NONE(keyEntry -> keyEntry.getKey().isUnbound()),
	CONFLICTING(keyEntry -> {
		for (KeyMapping key : Minecraft.getInstance().options.keyMappings) {
			if (!key.getName().equals(keyEntry.getKey().getName()) && !key.isUnbound()) {
				if (((AccessKeyMapping) key).lucent$getKey().getValue() == ((AccessKeyMapping) keyEntry.getKey()).lucent$getKey().getValue()) {
					return true;
				}
			}
		}
		return false;
	});

	private final Predicate<IKeyEntry> predicate;

	DisplayMode(Predicate<IKeyEntry> predicate) {
		this.predicate = predicate;
	}

	public Predicate<KeyBindsList.Entry> getPredicate() {
		return entry -> entry instanceof IKeyEntry keyEntry && predicate.test(keyEntry);
	}

}