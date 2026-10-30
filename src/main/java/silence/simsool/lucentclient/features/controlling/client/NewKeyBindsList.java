package silence.simsool.lucentclient.features.controlling.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.FocusableTextWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.util.CommonColors;
import silence.simsool.lucentclient.features.controlling.ControllingConstants;
import silence.simsool.lucentclient.features.controlling.api.entries.ICategoryEntry;
import silence.simsool.lucentclient.features.controlling.api.entries.IKeyEntry;

public class NewKeyBindsList extends CustomList {

	private final KeyBindsScreen controlsScreen;
	private final Minecraft mc;
	private int maxListLabelWidth;

	public NewKeyBindsList(KeyBindsScreen keyBindsScreen, Minecraft minecraft) {
		super(keyBindsScreen, minecraft);
		this.height -= 52;
		this.setY(48);
		this.controlsScreen = keyBindsScreen;
		this.mc = minecraft;
		this.clearEntries();
		this.allEntries = new ArrayList<>();
		KeyMapping[] bindings = ArrayUtils.clone(minecraft.options.keyMappings);
		Arrays.sort(bindings);
		KeyMapping.Category lastCategory = null;

		for (KeyMapping keybinding : bindings) {
			KeyMapping.Category category = keybinding.getCategory();
			if (!category.equals(lastCategory)) {
				lastCategory = category;
				if (shouldShow(category.label())) {
					addEntry(new CategoryEntry(category));
				}
			}

			Component component = Component.translatable(keybinding.getName());
			int width = minecraft.font.width(component);
			if (width > this.maxListLabelWidth) {
				this.maxListLabelWidth = width;
			}
			if (shouldShow(category.label())) {
				addEntry(new KeyEntry(keybinding, component));
			}
		}
	}

	private boolean shouldShow(Component component) {
		if (component.getContents() instanceof TranslatableContents tc) {
			return !tc.getKey().endsWith(".hidden");
		}
		return true;
	}

	@Override
	public int getBottom() {
		return this.controlsScreen.height - 56;
	}

	public class CategoryEntry extends Entry implements ICategoryEntry {

		private final KeyMapping.Category category;
		private final FocusableTextWidget categoryName;

		public CategoryEntry(KeyMapping.Category category) {
			this.category = category;
			this.categoryName = FocusableTextWidget.builder(category.label(), NewKeyBindsList.this.minecraft.font)
				.alwaysShowBorder(false)
				.backgroundFill(FocusableTextWidget.BackgroundFill.ON_FOCUS)
				.build();
		}

		@Override
		public void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY, boolean hovered, float partialTicks) {
			this.categoryName.setPosition(NewKeyBindsList.this.width / 2 - this.categoryName.getWidth() / 2, this.getContentBottom() - 9 - 1);
			this.categoryName.render(guiGraphics, mouseX, mouseY, partialTicks);
		}

		@Override
		public List<? extends NarratableEntry> narratables() {
			return List.of(this.categoryName);
		}

		@Override
		public List<? extends GuiEventListener> children() {
			return List.of(this.categoryName);
		}

		@Override
		public void refreshEntry() {
		}

		public FocusableTextWidget categoryName() {
			return categoryName;
		}

		@Override
		public KeyMapping.Category category() {
			return this.category;
		}

	}

	public class KeyEntry extends KeyBindsList.Entry implements IKeyEntry {

		private final KeyMapping key;
		private final Component name;
		private final Button btnChangeKeyBinding;
		private final Button btnResetKeyBinding;
		private boolean hasCollision;
		private final Component categoryName;

		public KeyEntry(final KeyMapping key, final Component name) {
			this.key = key;
			this.name = name;
			this.btnChangeKeyBinding = Button.builder(this.name, btn -> {
					NewKeyBindsList.this.controlsScreen.selectedKey = key;
					NewKeyBindsList.this.resetMappingAndUpdateButtons();
				})
				.bounds(0, 0, 75, 20)
				.createNarration(supp -> key.isUnbound() ? Component.translatable("narrator.controls.unbound", name) : Component.translatable("narrator.controls.bound", name, supp.get()))
				.build();

			this.btnResetKeyBinding = Button.builder(ControllingConstants.COMPONENT_CONTROLS_RESET, btn -> {
					key.setKey(key.getDefaultKey());
					NewKeyBindsList.this.resetMappingAndUpdateButtons();
				}).bounds(0, 0, 50, 20)
				.createNarration(supp -> Component.translatable("narrator.controls.reset", name))
				.build();

			this.categoryName = this.key.getCategory().label();
			refreshEntry();
		}

		@Override
		public void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY, boolean hovered, float partialTicks) {
			int resetKeyX = NewKeyBindsList.this.scrollBarX() - this.btnResetKeyBinding.getWidth() - 10;
			int top = this.getContentY() - 2;
			this.btnResetKeyBinding.setPosition(resetKeyX, top);
			this.btnResetKeyBinding.render(guiGraphics, mouseX, mouseY, partialTicks);

			this.btnChangeKeyBinding.setPosition(resetKeyX - 5 - this.btnChangeKeyBinding.getWidth(), top);
			this.btnChangeKeyBinding.render(guiGraphics, mouseX, mouseY, partialTicks);
			guiGraphics.drawString(NewKeyBindsList.this.mc.font, this.name, this.getContentX(), this.getContentYMiddle() - 9 / 2, -1);

			if (this.hasCollision) {
				int markerWidth = 3;
				int minX = this.btnChangeKeyBinding.getX() - 6;
				guiGraphics.fill(minX, this.getContentY() - 1, minX + markerWidth, this.getContentBottom(), CommonColors.YELLOW);
			}
		}

		@Override
		public List<GuiEventListener> children() {
			return List.of(this.btnChangeKeyBinding, this.btnResetKeyBinding);
		}

		@Override
		public List<? extends NarratableEntry> narratables() {
			return List.of(this.btnChangeKeyBinding, this.btnResetKeyBinding);
		}

		@Override
		public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
			if (this.btnChangeKeyBinding.mouseClicked(event, doubleClick)) {
				return true;
			}
			if (this.btnResetKeyBinding.mouseClicked(event, doubleClick)) {
				return true;
			}
			return super.mouseClicked(event, doubleClick);
		}

		@Override
		public boolean mouseReleased(MouseButtonEvent event) {
			if (this.btnChangeKeyBinding.mouseReleased(event)) {
				return true;
			}
			if (this.btnResetKeyBinding.mouseReleased(event)) {
				return true;
			}
			return super.mouseReleased(event);
		}

		@Override
		public KeyMapping getKey() {
			return key;
		}

		@Override
		public Component getName() {
			return name;
		}

		@Override
		public Component categoryName() {
			return categoryName;
		}

		@Override
		public Button getBtnResetKeyBinding() {
			return btnResetKeyBinding;
		}

		@Override
		public Button getBtnChangeKeyBinding() {
			return btnChangeKeyBinding;
		}

		@Override
		public void refreshEntry() {
			this.btnChangeKeyBinding.setMessage(this.key.getTranslatedKeyMessage());
			this.btnResetKeyBinding.active = !this.key.isDefault();
			this.hasCollision = false;
			MutableComponent duplicates = Component.empty();
			if (!this.key.isUnbound()) {
				KeyMapping[] mappings = NewKeyBindsList.this.minecraft.options.keyMappings;

				for (KeyMapping mapping : mappings) {
					if (mapping != this.key && this.key.same(mapping)) {
						if (this.hasCollision) {
							duplicates.append(", ");
						}
						this.hasCollision = true;
						duplicates.append(Component.translatable(mapping.getName()));
					}
				}
			}
			MutableComponent tooltip = categoryName.copy();
			if (this.hasCollision) {
				this.btnChangeKeyBinding.setMessage(Component.literal("[ ")
					.append(this.btnChangeKeyBinding.getMessage().copy().withStyle(ChatFormatting.WHITE))
					.append(" ]")
					.withStyle(ChatFormatting.YELLOW));
				tooltip.append(CommonComponents.NEW_LINE);
				tooltip.append(Component.translatable("controls.keybinds.duplicateKeybinds", duplicates));
			}
			this.btnChangeKeyBinding.setTooltip(Tooltip.create(tooltip));

			if (NewKeyBindsList.this.controlsScreen.selectedKey == this.key) {
				this.btnChangeKeyBinding.setMessage(Component.literal("> ")
					.append(this.btnChangeKeyBinding.getMessage()
						.copy()
						.withStyle(ChatFormatting.WHITE, ChatFormatting.UNDERLINE))
					.append(" <")
					.withStyle(ChatFormatting.YELLOW));
			}
		}

	}

}