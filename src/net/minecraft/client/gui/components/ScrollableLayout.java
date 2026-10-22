package net.minecraft.client.gui.components;

import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.layouts.LayoutElement;

@Environment(EnvType.CLIENT)
public class ScrollableLayout implements Layout {
   private static final int DEFAULT_SCROLLBAR_SPACING = 4;
   private final Layout content;
   private final ScrollableLayout$Container container;
   private final ScrollableLayout$ReserveStrategy reserveStrategy;
   private int scrollbarSpacing = 4;
   private int minWidth;
   private int minHeight;
   private int maxHeight;

   public ScrollableLayout(final Minecraft minecraft, final Layout content, final int maxHeight) {
      this(minecraft, content, maxHeight, ScrollableLayout$ReserveStrategy.BOTH);
   }

   public ScrollableLayout(final Minecraft minecraft, final Layout content, final int maxHeight, final ScrollableLayout$ReserveStrategy reserveStrategy) {
      this.content = content;
      this.maxHeight = maxHeight;
      this.reserveStrategy = reserveStrategy;
      this.container = new ScrollableLayout$Container(this, minecraft, 0, maxHeight, AbstractScrollArea.defaultSettings(10));
   }

   public void setScrollbarSpacing(final int scrollbarSpacing) {
      this.scrollbarSpacing = scrollbarSpacing;
   }

   public void setMinWidth(final int minWidth) {
      this.minWidth = minWidth;
      this.container.setWidth(Math.max(this.content.getWidth(), minWidth));
   }

   public void setMinHeight(final int minHeight) {
      this.minHeight = minHeight;
      this.container.setHeight(Math.max(this.content.getHeight(), minHeight));
   }

   public void setMaxHeight(final int maxHeight) {
      this.maxHeight = maxHeight;
      this.container.setHeight(Math.min(this.content.getHeight(), maxHeight));
      this.container.refreshScrollAmount();
   }

   @Override
   public void arrangeElements() {
      this.content.arrangeElements();
      int contentWidth = this.content.getWidth();

      int scrollbarReserve = switch (this.reserveStrategy) {
         case RIGHT -> this.container.scrollbarReserve();
         case BOTH -> 2 * this.container.scrollbarReserve();
      };
      this.container.setWidth(Math.max(contentWidth, this.minWidth) + scrollbarReserve);
      this.container.setHeight(Math.clamp((long)this.container.getHeight(), this.minHeight, this.maxHeight));
      this.container.refreshChildren();
      this.container.refreshScrollAmount();
   }

   @Override
   public void visitChildren(final Consumer<LayoutElement> layoutElementVisitor) {
      layoutElementVisitor.accept(this.container);
   }

   @Override
   public void removeChildren() {
      this.container.children().clear();
      this.content.removeChildren();
   }

   @Override
   public void setX(final int x) {
      this.container.setX(x);
   }

   @Override
   public void setY(final int y) {
      this.container.setY(y);
   }

   @Override
   public int getX() {
      return this.container.getX();
   }

   @Override
   public int getY() {
      return this.container.getY();
   }

   @Override
   public int getWidth() {
      return this.container.getWidth();
   }

   @Override
   public int getHeight() {
      return this.container.getHeight();
   }

   public double getScrollAmount() {
      return this.container.scrollAmount();
   }
}
