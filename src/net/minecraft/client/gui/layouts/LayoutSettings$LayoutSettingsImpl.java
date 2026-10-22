package net.minecraft.client.gui.layouts;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class LayoutSettings$LayoutSettingsImpl implements LayoutSettings {
   public int paddingLeft;
   public int paddingTop;
   public int paddingRight;
   public int paddingBottom;
   public float xAlignment;
   public float yAlignment;

   public LayoutSettings$LayoutSettingsImpl() {
   }

   public LayoutSettings$LayoutSettingsImpl(final LayoutSettings$LayoutSettingsImpl copy) {
      this.paddingLeft = copy.paddingLeft;
      this.paddingTop = copy.paddingTop;
      this.paddingRight = copy.paddingRight;
      this.paddingBottom = copy.paddingBottom;
      this.xAlignment = copy.xAlignment;
      this.yAlignment = copy.yAlignment;
   }

   public LayoutSettings$LayoutSettingsImpl padding(final int padding) {
      return this.padding(padding, padding);
   }

   public LayoutSettings$LayoutSettingsImpl padding(final int horizontal, final int vertical) {
      return this.paddingHorizontal(horizontal).paddingVertical(vertical);
   }

   public LayoutSettings$LayoutSettingsImpl padding(final int left, final int top, final int right, final int bottom) {
      return this.paddingLeft(left).paddingRight(right).paddingTop(top).paddingBottom(bottom);
   }

   public LayoutSettings$LayoutSettingsImpl paddingLeft(final int padding) {
      this.paddingLeft = padding;
      return this;
   }

   public LayoutSettings$LayoutSettingsImpl paddingTop(final int padding) {
      this.paddingTop = padding;
      return this;
   }

   public LayoutSettings$LayoutSettingsImpl paddingRight(final int padding) {
      this.paddingRight = padding;
      return this;
   }

   public LayoutSettings$LayoutSettingsImpl paddingBottom(final int padding) {
      this.paddingBottom = padding;
      return this;
   }

   public LayoutSettings$LayoutSettingsImpl paddingHorizontal(final int padding) {
      return this.paddingLeft(padding).paddingRight(padding);
   }

   public LayoutSettings$LayoutSettingsImpl paddingVertical(final int padding) {
      return this.paddingTop(padding).paddingBottom(padding);
   }

   public LayoutSettings$LayoutSettingsImpl align(final float xAlignment, final float yAlignment) {
      this.xAlignment = xAlignment;
      this.yAlignment = yAlignment;
      return this;
   }

   public LayoutSettings$LayoutSettingsImpl alignHorizontally(final float xAlignment) {
      this.xAlignment = xAlignment;
      return this;
   }

   public LayoutSettings$LayoutSettingsImpl alignVertically(final float yAlignment) {
      this.yAlignment = yAlignment;
      return this;
   }

   public LayoutSettings$LayoutSettingsImpl copy() {
      return new LayoutSettings$LayoutSettingsImpl(this);
   }

   @Override
   public LayoutSettings$LayoutSettingsImpl getExposed() {
      return this;
   }
}
