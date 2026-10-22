package net.minecraft.client.gui.components;

import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.navigation.ScreenRectangle;

@Environment(EnvType.CLIENT)
public abstract class AbstractSelectionList$Entry<E extends AbstractSelectionList$Entry<E>> implements LayoutElement, GuiEventListener {
   public static final int CONTENT_PADDING = 2;
   private int x = 0;
   private int y = 0;
   private int width = 0;
   private int height;
   @Deprecated
   private AbstractSelectionList<E> list;

   protected AbstractSelectionList$Entry() {
   }

   @Override
   public void setFocused(final boolean focused) {
   }

   @Override
   public boolean isFocused() {
      return this.list.getFocused() == this;
   }

   public abstract void extractContent(final GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a);

   @Override
   public boolean isMouseOver(final double mx, final double my) {
      return this.getRectangle().containsPoint((int)mx, (int)my);
   }

   @Override
   public void setX(final int x) {
      this.x = x;
   }

   @Override
   public void setY(final int y) {
      this.y = y;
   }

   public void setWidth(final int width) {
      this.width = width;
   }

   public void setHeight(final int height) {
      this.height = height;
   }

   public int getContentX() {
      return this.getX() + 2;
   }

   public int getContentY() {
      return this.getY() + 2;
   }

   public int getContentHeight() {
      return this.getHeight() - 4;
   }

   public int getContentYMiddle() {
      return this.getContentY() + this.getContentHeight() / 2;
   }

   public int getContentBottom() {
      return this.getContentY() + this.getContentHeight();
   }

   public int getContentWidth() {
      return this.getWidth() - 4;
   }

   public int getContentXMiddle() {
      return this.getContentX() + this.getContentWidth() / 2;
   }

   public int getContentRight() {
      return this.getContentX() + this.getContentWidth();
   }

   @Override
   public int getX() {
      return this.x;
   }

   @Override
   public int getY() {
      return this.y;
   }

   @Override
   public int getWidth() {
      return this.width;
   }

   @Override
   public int getHeight() {
      return this.height;
   }

   @Override
   public void visitWidgets(final Consumer<AbstractWidget> widgetVisitor) {
   }

   @Override
   public ScreenRectangle getRectangle() {
      return LayoutElement.super.getRectangle();
   }
}
