package net.minecraft.client.gui.layouts;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public abstract class AbstractLayout$AbstractChildWrapper {
   public final LayoutElement child;
   public final LayoutSettings$LayoutSettingsImpl layoutSettings;

   protected AbstractLayout$AbstractChildWrapper(final LayoutElement child, final LayoutSettings layoutSettings) {
      this.child = child;
      this.layoutSettings = layoutSettings.getExposed();
   }

   public int getHeight() {
      return this.child.getHeight() + this.layoutSettings.paddingTop + this.layoutSettings.paddingBottom;
   }

   public int getWidth() {
      return this.child.getWidth() + this.layoutSettings.paddingLeft + this.layoutSettings.paddingRight;
   }

   public void setX(final int x, final int availableSpace) {
      float leastOffset = this.layoutSettings.paddingLeft;
      float mostOffset = availableSpace - this.child.getWidth() - this.layoutSettings.paddingRight;
      int offset = (int)Mth.lerp(this.layoutSettings.xAlignment, leastOffset, mostOffset);
      this.child.setX(offset + x);
   }

   public void setY(final int y, final int availableSpace) {
      float leastOffset = this.layoutSettings.paddingTop;
      float mostOffset = availableSpace - this.child.getHeight() - this.layoutSettings.paddingBottom;
      int offset = Math.round(Mth.lerp(this.layoutSettings.yAlignment, leastOffset, mostOffset));
      this.child.setY(offset + y);
   }
}
