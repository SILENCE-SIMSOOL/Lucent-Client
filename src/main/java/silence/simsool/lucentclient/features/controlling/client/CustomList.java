package silence.simsool.lucentclient.features.controlling.client;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;

public class CustomList extends KeyBindsList {

	public List<Entry> allEntries;

	public CustomList(KeyBindsScreen keyBindsScreen, Minecraft minecraft) {
		super(keyBindsScreen, minecraft);
	}

	public List<Entry> getAllEntries() {
		return allEntries;
	}

	@Override
	public void clearEntries() {
		super.clearEntries();
	}

	@Override
	public void sort(Comparator<Entry> comparator) {
		super.sort(comparator);
	}

	@Override
	protected int addEntry(Entry entry) {
		if (allEntries == null) {
			allEntries = new ArrayList<>();
		}
		allEntries.add(entry);
		return addEntryInternal(entry);
	}

	public int addEntryInternal(Entry entry) {
		return super.addEntry(entry);
	}

}