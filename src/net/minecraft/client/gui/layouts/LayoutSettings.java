package net.minecraft.client.gui.layouts;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface LayoutSettings {
   LayoutSettings padding(int padding);

   LayoutSettings padding(int horizontal, int vertical);

   LayoutSettings padding(int left, int top, int right, int bottom);

   LayoutSettings paddingLeft(int padding);

   LayoutSettings paddingTop(int padding);

   LayoutSettings paddingRight(int padding);

   LayoutSettings paddingBottom(int padding);

   LayoutSettings paddingHorizontal(int padding);

   LayoutSettings paddingVertical(int padding);

   LayoutSettings align(float xAlignment, float yAlignment);

   LayoutSettings alignHorizontally(float xAlignment);

   LayoutSettings alignVertically(float yAlignment);

   default LayoutSettings alignHorizontallyLeft() {
      return this.alignHorizontally(0.0F);
   }

   default LayoutSettings alignHorizontallyCenter() {
      return this.alignHorizontally(0.5F);
   }

   default LayoutSettings alignHorizontallyRight() {
      return this.alignHorizontally(1.0F);
   }

   default LayoutSettings alignVerticallyTop() {
      return this.alignVertically(0.0F);
   }

   default LayoutSettings alignVerticallyMiddle() {
      return this.alignVertically(0.5F);
   }

   default LayoutSettings alignVerticallyBottom() {
      return this.alignVertically(1.0F);
   }

   LayoutSettings copy();

   LayoutSettings$LayoutSettingsImpl getExposed();

   static LayoutSettings defaults() {
      return new LayoutSettings$LayoutSettingsImpl();
   }
}
