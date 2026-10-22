package net.minecraft.client.gui.components;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.narration.NarratableEntry$NarrationPriority;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.ScreenDirection;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public abstract class AbstractSelectionList<E extends AbstractSelectionList$Entry<E>> extends AbstractContainerWidget {
   private static final Identifier MENU_LIST_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/menu_list_background.png");
   public static final Identifier INWORLD_MENU_LIST_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/inworld_menu_list_background.png");
   private static final int SEPARATOR_HEIGHT = 2;
   protected final Minecraft minecraft;
   protected final int defaultEntryHeight;
   private final List<E> children = new AbstractSelectionList$TrackedList(this);
   protected boolean centerListVertically = true;
   @Nullable
   private E selected;
   @Nullable
   private E hovered;

   public AbstractSelectionList(final Minecraft minecraft, final int width, final int height, final int y, final int defaultEntryHeight) {
      super(0, y, width, height, CommonComponents.EMPTY, AbstractScrollArea.defaultSettings(defaultEntryHeight / 2));
      this.minecraft = minecraft;
      this.defaultEntryHeight = defaultEntryHeight;
   }

   @Nullable
   public E getSelected() {
      return this.selected;
   }

   public void setSelected(@Nullable final E selected) {
      this.selected = selected;
      if (selected != null) {
         boolean topClipped = selected.getContentY() < this.getY();
         boolean bottomClipped = selected.getContentBottom() > this.getBottom();
         if (this.minecraft.getLastInputType().isKeyboard() || topClipped || bottomClipped) {
            this.scrollToEntry(selected);
         }
      }
   }

   @Nullable
   public E getFocused() {
      return (E)super.getFocused();
   }

   @Override
   public final List<E> children() {
      return Collections.unmodifiableList(this.children);
   }

   protected void sort(final Comparator<E> comparator) {
      this.children.sort(comparator);
      this.repositionEntries();
   }

   protected void swap(final int firstIndex, final int secondIndex) {
      Collections.swap(this.children, firstIndex, secondIndex);
      this.repositionEntries();
      this.scrollToEntry(this.children.get(secondIndex));
   }

   protected void clearEntries() {
      this.children.clear();
      this.selected = null;
   }

   protected void clearEntriesExcept(final E exception) {
      this.children.removeIf(entry -> entry != exception);
      if (this.selected != exception) {
         this.setSelected(null);
      }
   }

   public void replaceEntries(final Collection<E> newChildren) {
      this.clearEntries();

      for (E newChild : newChildren) {
         this.addEntry(newChild);
      }
   }

   private int getFirstEntryY() {
      return this.getY() + 2;
   }

   public int getNextY() {
      int y = this.getFirstEntryY() - (int)this.scrollAmount();

      for (E child : this.children) {
         y += child.getHeight();
      }

      return y;
   }

   protected int addEntry(final E entry) {
      return this.addEntry(entry, this.defaultEntryHeight);
   }

   protected int addEntry(final E entry, final int height) {
      entry.setX(this.getRowLeft());
      entry.setWidth(this.getRowWidth());
      entry.setY(this.getNextY());
      entry.setHeight(height);
      this.children.add(entry);
      return this.children.size() - 1;
   }

   protected void addEntryToTop(final E entry) {
      this.addEntryToTop(entry, this.defaultEntryHeight);
   }

   protected void addEntryToTop(final E entry, final int height) {
      double scrollFromBottom = this.maxScrollAmount() - this.scrollAmount();
      entry.setHeight(height);
      this.children.addFirst(entry);
      this.repositionEntries();
      this.setScrollAmount(this.maxScrollAmount() - scrollFromBottom);
   }

   private void repositionEntries() {
      int y = this.getFirstEntryY() - (int)this.scrollAmount();

      for (E child : this.children) {
         child.setY(y);
         y += child.getHeight();
         child.setX(this.getRowLeft());
         child.setWidth(this.getRowWidth());
      }
   }

   protected void removeEntryFromTop(final E entry) {
      double scrollFromBottom = this.maxScrollAmount() - this.scrollAmount();
      this.removeEntry(entry);
      this.setScrollAmount(this.maxScrollAmount() - scrollFromBottom);
   }

   protected int getItemCount() {
      return this.children().size();
   }

   protected boolean entriesCanBeSelected() {
      return true;
   }

   @Nullable
   protected final E getEntryAtPosition(final double posX, final double posY) {
      for (E child : this.children) {
         if (child.isMouseOver(posX, posY)) {
            return child;
         }
      }

      return null;
   }

   public void updateSize(final int width, final HeaderAndFooterLayout layout) {
      this.updateSizeAndPosition(width, layout.getContentHeight(), layout.getHeaderHeight());
   }

   public void updateSizeAndPosition(final int width, final int height, final int y) {
      this.updateSizeAndPosition(width, height, 0, y);
   }

   public void updateSizeAndPosition(final int width, final int height, final int x, final int y) {
      this.setSize(width, height);
      this.setPosition(x, y);
      this.repositionEntries();
      if (this.getSelected() != null) {
         this.scrollToEntry(this.getSelected());
      }

      this.refreshScrollAmount();
   }

   @Override
   protected int contentHeight() {
      int totalHeight = 0;

      for (E child : this.children) {
         totalHeight += child.getHeight();
      }

      return totalHeight + 4;
   }

   @Override
   public void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
      this.hovered = this.isMouseOver(mouseX, mouseY) ? this.getEntryAtPosition(mouseX, mouseY) : null;
      this.extractListBackground(graphics);
      this.enableScissor(graphics);
      this.extractListItems(graphics, mouseX, mouseY, a);
      graphics.disableScissor();
      this.extractListSeparators(graphics);
      this.extractScrollbar(graphics, mouseX, mouseY);
   }

   protected void extractListSeparators(final GuiGraphicsExtractor graphics) {
      Identifier headerSeparator = this.minecraft.level == null ? Screen.HEADER_SEPARATOR : Screen.INWORLD_HEADER_SEPARATOR;
      Identifier footerSeparator = this.minecraft.level == null ? Screen.FOOTER_SEPARATOR : Screen.INWORLD_FOOTER_SEPARATOR;
      graphics.blit(RenderPipelines.GUI_TEXTURED, headerSeparator, this.getX(), this.getY() - 2, 0.0F, 0.0F, this.getWidth(), 2, 32, 2);
      graphics.blit(RenderPipelines.GUI_TEXTURED, footerSeparator, this.getX(), this.getBottom(), 0.0F, 0.0F, this.getWidth(), 2, 32, 2);
   }

   protected void extractListBackground(final GuiGraphicsExtractor graphics) {
      Identifier menuListBackground = this.minecraft.level == null ? MENU_LIST_BACKGROUND : INWORLD_MENU_LIST_BACKGROUND;
      graphics.blit(
         RenderPipelines.GUI_TEXTURED,
         menuListBackground,
         this.getX(),
         this.getY(),
         (float)this.getRight(),
         (float)(this.getBottom() + (int)this.scrollAmount()),
         this.getWidth(),
         this.getHeight(),
         32,
         32
      );
   }

   protected void enableScissor(final GuiGraphicsExtractor graphics) {
      graphics.enableScissor(
         Mth.clamp(this.getX(), 0, graphics.guiWidth()),
         Mth.clamp(this.getY(), 0, graphics.guiHeight()),
         Mth.clamp(this.getRight(), 0, graphics.guiWidth()),
         Mth.clamp(this.getBottom(), 0, graphics.guiHeight())
      );
   }

   protected void scrollToEntry(final E entry) {
      int topDelta = entry.getY() - this.getY() - 2;
      if (topDelta < 0) {
         this.scroll(topDelta);
      }

      int bottomDelta = this.getBottom() - entry.getY() - entry.getHeight() - 2;
      if (bottomDelta < 0) {
         this.scroll(-bottomDelta);
      }
   }

   protected void centerScrollOn(final E entry) {
      int y = 0;

      for (E child : this.children) {
         if (child == entry) {
            y += child.getHeight() / 2;
            break;
         }

         y += child.getHeight();
      }

      this.setScrollAmount(y - this.height / 2.0);
   }

   private void scroll(final int amount) {
      this.setScrollAmount(this.scrollAmount() + amount);
   }

   @Override
   public void setScrollAmount(final double scrollAmount) {
      super.setScrollAmount(scrollAmount);
      this.repositionEntries();
   }

   @Override
   protected int scrollBarX() {
      return this.getRowRight() + this.scrollbarWidth() + 2;
   }

   @Override
   public Optional<GuiEventListener> getChildAt(final double x, final double y) {
      return Optional.ofNullable(this.getEntryAtPosition(x, y));
   }

   @Override
   public void setFocused(@Nullable final GuiEventListener focused) {
      E oldFocus = this.getFocused();
      if (oldFocus != focused && oldFocus instanceof ContainerEventHandler oldFocusContainer) {
         oldFocusContainer.setFocused(null);
      }

      super.setFocused(focused);
      int index = this.children.indexOf(focused);
      if (index >= 0) {
         E magicallyCastEntry = this.children.get(index);
         this.setSelected(magicallyCastEntry);
      }
   }

   @Nullable
   protected E nextEntry(final ScreenDirection dir) {
      return this.nextEntry(dir, entry -> true);
   }

   @Nullable
   protected E nextEntry(final ScreenDirection dir, final Predicate<E> canSelect) {
      return this.nextEntry(dir, canSelect, this.getSelected());
   }

   @Nullable
   protected E nextEntry(final ScreenDirection dir, final Predicate<E> canSelect, @Nullable final E startEntry) {
      int delta = switch (dir) {
         case RIGHT, LEFT -> 0;
         case UP -> -1;
         case DOWN -> 1;
      };
      if (!this.children().isEmpty() && delta != 0) {
         int index;
         if (startEntry == null) {
            index = delta > 0 ? 0 : this.children().size() - 1;
         } else {
            index = this.children().indexOf(startEntry) + delta;
         }

         for (int i = index; i >= 0 && i < this.children.size(); i += delta) {
            E selected = this.children().get(i);
            if (canSelect.test(selected)) {
               return selected;
            }
         }
      }

      return null;
   }

   protected void extractListItems(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
      for (E child : this.children) {
         if (child.getY() + child.getHeight() >= this.getY() && child.getY() <= this.getBottom()) {
            this.extractItem(graphics, mouseX, mouseY, a, child);
         }
      }
   }

   protected void extractItem(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a, final E entry) {
      if (this.entriesCanBeSelected() && this.getSelected() == entry) {
         int outlineColor = this.isFocused() ? -1 : -8355712;
         this.extractSelection(graphics, entry, outlineColor);
      }

      entry.extractContent(graphics, mouseX, mouseY, Objects.equals(this.hovered, entry), a);
   }

   protected void extractSelection(final GuiGraphicsExtractor graphics, final E entry, final int outlineColor) {
      int outlineX0 = entry.getX();
      int outlineY0 = entry.getY();
      int outlineX1 = outlineX0 + entry.getWidth();
      int outlineY1 = outlineY0 + entry.getHeight();
      graphics.fill(outlineX0, outlineY0, outlineX1, outlineY1, outlineColor);
      graphics.fill(outlineX0 + 1, outlineY0 + 1, outlineX1 - 1, outlineY1 - 1, -16777216);
   }

   public int getRowLeft() {
      return this.getX() + this.width / 2 - this.getRowWidth() / 2;
   }

   public int getRowRight() {
      return this.getRowLeft() + this.getRowWidth();
   }

   public int getRowTop(final int row) {
      return this.children.get(row).getY();
   }

   public int getRowBottom(final int row) {
      E child = this.children.get(row);
      return child.getY() + child.getHeight();
   }

   public int getRowWidth() {
      return 220;
   }

   @Override
   public NarratableEntry$NarrationPriority narrationPriority() {
      if (this.isFocused()) {
         return NarratableEntry$NarrationPriority.FOCUSED;
      } else {
         return this.hovered != null ? NarratableEntry$NarrationPriority.HOVERED : NarratableEntry$NarrationPriority.NONE;
      }
   }

   protected void removeEntries(final List<E> entries) {
      entries.forEach(this::removeEntry);
   }

   protected void removeEntry(final E entry) {
      boolean removed = this.children.remove(entry);
      if (removed) {
         this.repositionEntries();
         if (entry == this.getSelected()) {
            this.setSelected(null);
         }
      }
   }

   @Nullable
   protected E getHovered() {
      return this.hovered;
   }

   private void bindEntryToSelf(final AbstractSelectionList$Entry<E> entry) {
      entry.list = this;
   }

   protected void narrateListElementPosition(final NarrationElementOutput output, final E element) {
      List<E> children = this.children();
      if (children.size() > 1) {
         int index = children.indexOf(element);
         if (index != -1) {
            output.add(NarratedElementType.POSITION, Component.translatable("narrator.position.list", index + 1, children.size()));
         }
      }
   }
}
