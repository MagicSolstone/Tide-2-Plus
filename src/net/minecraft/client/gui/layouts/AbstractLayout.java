package net.minecraft.client.gui.layouts;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public abstract class AbstractLayout implements Layout {
   private int x;
   private int y;
   protected int width;
   protected int height;

   public AbstractLayout(final int x, final int y, final int width, final int height) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
   }

   @Override
   public void setX(final int x) {
      this.visitChildren(child -> {
         int newChildX = child.getX() + (x - this.getX());
         child.setX(newChildX);
      });
      this.x = x;
   }

   @Override
   public void setY(final int y) {
      this.visitChildren(child -> {
         int newChildY = child.getY() + (y - this.getY());
         child.setY(newChildY);
      });
      this.y = y;
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
}
