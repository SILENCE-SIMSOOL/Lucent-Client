package silence.simsool.lucentclient.features.controlling.client;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import com.google.common.base.Suppliers;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;
import silence.simsool.lucentclient.features.controlling.ControllingConstants;
import silence.simsool.lucentclient.features.controlling.api.DisplayMode;
import silence.simsool.lucentclient.features.controlling.api.SortOrder;
import silence.simsool.lucentclient.features.controlling.api.entries.ICategoryEntry;
import silence.simsool.lucentclient.features.controlling.api.entries.IInputEntry;
import silence.simsool.lucentclient.features.controlling.api.entries.IKeyEntry;
import silence.simsool.lucentclient.mixin.mixins.controlling.AccessAbstractSelectionList;
import silence.simsool.lucentclient.mixin.mixins.controlling.AccessKeyBindsScreen;

public class NewKeyBindsScreen extends KeyBindsScreen {
	private EditBox search;
	private DisplayMode displayMode;
	private SortOrder sortOrder = SortOrder.NONE;
	private Button buttonNone;
	private Button buttonConflicting;
	private Button buttonSort;
	private final DisplayableBoolean confirmingReset = new DisplayableBoolean(false, ControllingConstants.COMPONENT_OPTIONS_CONFIRM_RESET, ControllingConstants.COMPONENT_CONTROLS_RESET_ALL);
	private boolean showFree;
	private Supplier<NewKeyBindsList> newKeyList;
	private Supplier<FreeKeysList> freeKeyList;

	public NewKeyBindsScreen(Screen screen, Options settings) {
		super(screen, settings);
		this.layout.setHeaderHeight(48);
		this.layout.setFooterHeight(56);
	}

	@Override
	protected void init() {
		super.init();
		this.search.moveCursor(0, false);
	}

	@Override
	protected void addTitle() {
		int searchX = 340;
		int centerX = this.width / 2;
		this.search = new EditBox(this.font, centerX - searchX / 2, 20, searchX, Button.DEFAULT_HEIGHT, Component.translatable("selectWorld.search"));
		this.search.setHint(Component.translatable("selectWorld.search"));
		this.search.setResponder(this::filterKeys);

		LinearLayout header = this.layout.addToHeader(LinearLayout.vertical(), layoutSettings -> layoutSettings.paddingVertical(8));
		header.addChild(new StringWidget(this.title, this.font), LayoutSettings::alignHorizontallyCenter);
		header.addChild(this.search, layoutSettings -> layoutSettings.paddingVertical(4));
		setInitialFocus(this.search);
	}

	@Override
	protected void addContents() {
		this.newKeyList = Suppliers.memoize(() -> new NewKeyBindsList(this, this.minecraft));
		this.freeKeyList = () -> new FreeKeysList(this, this.minecraft);
		getAccess().lucent$setKeyBindsList(showFree ? this.freeKeyList.get() : this.newKeyList.get());
		this.layout.addToContents(getKeyBindsList());
		displayMode = DisplayMode.ALL;
	}

	@Override
	protected void addFooter() {
		int btnWidth = Button.DEFAULT_WIDTH / 2 - 1;
		this.resetButton(Button.builder(confirmingReset.currentDisplay(), PRESS_RESET).build());
		resetButton().active = canReset();

		Button toggleFreeButton = Button.builder(ControllingConstants.COMPONENT_OPTIONS_TOGGLE_FREE, PRESS_FREE)
			.size(btnWidth, Button.DEFAULT_HEIGHT)
			.build();
		this.buttonSort = Button.builder(sortOrder.getDisplay(), PRESS_SORT)
			.size(btnWidth, Button.DEFAULT_HEIGHT)
			.build();
		this.buttonNone = Button.builder(ControllingConstants.COMPONENT_OPTIONS_SHOW_NONE, PRESS_NONE)
			.size(btnWidth, Button.DEFAULT_HEIGHT)
			.build();
		this.buttonConflicting = Button.builder(ControllingConstants.COMPONENT_OPTIONS_SHOW_CONFLICTS, PRESS_CONFLICTING)
			.size(btnWidth, Button.DEFAULT_HEIGHT)
			.build();

		GridLayout grid = this.layout.addToFooter(new GridLayout());
		grid.rowSpacing(4);
		grid.columnSpacing(8);
		GridLayout.RowHelper rowHelper = grid.createRowHelper(2);
		LinearLayout topLeft = rowHelper.addChild(LinearLayout.horizontal());
		topLeft.spacing(4);
		topLeft.addChild(toggleFreeButton);
		topLeft.addChild(this.buttonSort);

		LinearLayout topRight = rowHelper.addChild(LinearLayout.horizontal());
		topRight.spacing(4);
		topRight.addChild(this.buttonNone);
		topRight.addChild(this.buttonConflicting);

		rowHelper.addChild(resetButton());
		rowHelper.addChild(Button.builder(CommonComponents.GUI_DONE, btn -> this.onClose()).build());
	}

	@Override
	protected void repositionElements() {
		super.repositionElements();
		resetButton().active = canReset();
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
		super.extractRenderState(graphics, mouseX, mouseY, partialTicks);
	}

	public Button resetButton() {
		return this.getAccess().lucent$getResetButton();
	}

	public void resetButton(Button button) {
		this.getAccess().lucent$setResetButton(button);
	}

	public void filterKeys() {
		filterKeys(search.getValue());
	}

	public void filterKeys(String lastSearch) {
		CustomList list = getCustomList();
		list.clearEntries();
		getKeyBindsList().setScrollAmount(0);

		if (lastSearch.isEmpty() && displayMode == DisplayMode.ALL && sortOrder == SortOrder.NONE) {
			for (KeyBindsList.Entry allEntry : getCustomList().getAllEntries()) {
				list.addEntryInternal(allEntry);
			}
			return;
		}

		Predicate<KeyBindsList.Entry> extraPredicate = entry -> true;
		Consumer<List<IKeyEntry>> postConsumer = entries -> {};

		if (list instanceof NewKeyBindsList) {
			extraPredicate = displayMode.getPredicate();
			postConsumer = entries -> {
				entries.removeIf(entry -> !(entry instanceof IKeyEntry));
				list.sort(sortOrder);
			};
		}

		String query = lastSearch.toLowerCase().trim();
		for (KeyBindsList.Entry entry : list.getAllEntries()) {
			if (!extraPredicate.test(entry)) continue;
			if (matchesSearch(entry, query)) {
				list.addEntryInternal(entry);
			}
		}

		postConsumer.accept(getAbstractSelectionList().lucent$getChildren());
	}

	private boolean matchesSearch(KeyBindsList.Entry entry, String query) {
		if (query.isEmpty()) return true;
		if (entry instanceof ICategoryEntry cat) {
			return cat.category().label().getString().toLowerCase().contains(query);
		} else if (entry instanceof IKeyEntry key) {
			String name = key.getName().getString().toLowerCase();
			String keyName = key.getKey().getTranslatedKeyMessage().getString().toLowerCase();
			String category = key.categoryName().getString().toLowerCase();
			if (query.startsWith("category:")) {
				return category.contains(query.substring("category:".length()).trim());
			} else if (query.startsWith("key:")) {
				return keyName.contains(query.substring("key:".length()).trim());
			} else if (query.startsWith("name:")) {
				return name.contains(query.substring("name:".length()).trim());
			}
			return name.contains(query) || keyName.contains(query) || category.contains(query);
		} else if (entry instanceof IInputEntry input) {
			return input.getInput().getName().toLowerCase().contains(query);
		}
		return false;
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		boolean b = super.mouseClicked(event, doubleClick);
		if (!b && search.isFocused()) {
			this.setFocused(null);
			clearFocus();
			b = true;
		}
		return b;
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		if (!search.isFocused() && this.selectedKey == null) {
			if (event.hasControlDown() && event.key() == InputConstants.KEY_F) {
				search.setFocused(true);
				return true;
			}
		}
		if (search.isFocused()) {
			if (event.isEscape()) {
				search.setFocused(false);
				return true;
			}
		}
		if (this.selectedKey != null) {
			if (event.isEscape()) {
				this.selectedKey.setKey(InputConstants.UNKNOWN);
			} else {
				this.selectedKey.setKey(InputConstants.getKey(event));
			}
			this.selectedKey = null;
			this.lastKeySelection = Util.getMillis();
			this.getKeyBindsList().resetMappingAndUpdateButtons();
			return true;
		} else {
			return super.keyPressed(event);
		}
	}

	private CustomList getCustomList() {
		if (this.getKeyBindsList() instanceof CustomList cl) {
			return cl;
		}
		throw new IllegalStateException("keyBindsList was not an instance of CustomList!");
	}

	public KeyBindsList getKeyBindsList() {
		return getAccess().lucent$getKeyBindsList();
	}

	public AccessAbstractSelectionList getAbstractSelectionList() {
		return (AccessAbstractSelectionList) this.getKeyBindsList();
	}

	private void setKeyBindsList(KeyBindsList newList) {
		getAccess().lucent$setKeyBindsList(newList);
		repositionElements();
	}

	private AccessKeyBindsScreen getAccess() {
		return ((AccessKeyBindsScreen) this);
	}

	private boolean canReset() {
		for (KeyMapping key : this.options.keyMappings) {
			if (!key.isDefault()) {
				return true;
			}
		}
		return false;
	}

	private final Button.OnPress PRESS_RESET = btn -> {
		NewKeyBindsScreen screen = NewKeyBindsScreen.this;
		Minecraft minecraft = Objects.requireNonNull(screen.minecraft);

		if (!confirmingReset.toggle()) {
			for (KeyMapping keybinding : minecraft.options.keyMappings) {
				keybinding.setKey(keybinding.getDefaultKey());
			}
			getKeyBindsList().resetMappingAndUpdateButtons();
		}
		btn.setMessage(confirmingReset.currentDisplay());
	};

	private final Button.OnPress PRESS_NONE = btn -> {
		if (displayMode == DisplayMode.NONE) {
			buttonNone.setMessage(ControllingConstants.COMPONENT_OPTIONS_SHOW_NONE);
			displayMode = DisplayMode.ALL;
		} else {
			displayMode = DisplayMode.NONE;
			buttonNone.setMessage(ControllingConstants.COMPONENT_OPTIONS_SHOW_ALL);
			buttonConflicting.setMessage(ControllingConstants.COMPONENT_OPTIONS_SHOW_CONFLICTS);
		}
		filterKeys();
	};

	private final Button.OnPress PRESS_SORT = btn -> {
		sortOrder = sortOrder.cycle();
		btn.setMessage(sortOrder.getDisplay());
		filterKeys();
	};

	private final Button.OnPress PRESS_CONFLICTING = btn -> {
		if (displayMode == DisplayMode.CONFLICTING) {
			buttonConflicting.setMessage(ControllingConstants.COMPONENT_OPTIONS_SHOW_CONFLICTS);
			displayMode = DisplayMode.ALL;
		} else {
			displayMode = DisplayMode.CONFLICTING;
			buttonConflicting.setMessage(ControllingConstants.COMPONENT_OPTIONS_SHOW_ALL);
			buttonNone.setMessage(ControllingConstants.COMPONENT_OPTIONS_SHOW_NONE);
		}
		filterKeys();
	};

	private final Button.OnPress PRESS_FREE = btn -> {
		removeWidget(getKeyBindsList());
		if (showFree) {
			buttonSort.active = true;
			buttonNone.active = true;
			buttonConflicting.active = true;
			resetButton().active = canReset();
			setKeyBindsList(newKeyList.get());
		} else {
			freeKeyList.get().recalculate();
			buttonSort.active = false;
			buttonNone.active = false;
			buttonConflicting.active = false;
			resetButton().active = false;
			setKeyBindsList(freeKeyList.get());
		}
		filterKeys();
		addRenderableWidget(getKeyBindsList());
		setFocused(getKeyBindsList());
		showFree = !showFree;
	};
}