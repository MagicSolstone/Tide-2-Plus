package net.minecraft.client.gui.layouts;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum LinearLayout$Orientation {
   HORIZONTAL,
   VERTICAL;

   private void setSpacing(final GridLayout gridLayout, final int spacing) {
      switch (this) {
         case HORIZONTAL:
            gridLayout.columnSpacing(spacing);
            break;
         case VERTICAL:
            gridLayout.rowSpacing(spacing);
      }
   }

   public <T extends LayoutElement> T addChild(final GridLayout gridLayout, final T child, final int index, final LayoutSettings cellSettings) {
      return (T)(switch (this) {
         case HORIZONTAL -> gridLayout.addChild(child, 0, index, cellSettings);
         case VERTICAL -> gridLayout.addChild(child, index, 0, cellSettings);
      });
   }
}
