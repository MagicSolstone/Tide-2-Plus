package net.minecraft.client.gui.layouts;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public final class GridLayout$RowHelper {
   private final int columns;
   private int index;

   private GridLayout$RowHelper(final GridLayout this$0, final int columns) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.columns = columns;
   }

   public <T extends LayoutElement> T addChild(final T widget) {
      return this.addChild(widget, 1);
   }

   public <T extends LayoutElement> T addChild(final T widget, final int columnWidth) {
      return this.addChild(widget, columnWidth, this.defaultCellSetting());
   }

   public <T extends LayoutElement> T addChild(final T widget, final LayoutSettings layoutSettings) {
      return this.addChild(widget, 1, layoutSettings);
   }

   public <T extends LayoutElement> T addChild(final T widget, final int columnWidth, final LayoutSettings layoutSettings) {
      int row = this.index / this.columns;
      int columnBegin = this.index % this.columns;
      if (columnBegin + columnWidth > this.columns) {
         row++;
         columnBegin = 0;
         this.index = Mth.roundToward(this.index, this.columns);
      }

      this.index += columnWidth;
      return this.this$0.addChild(widget, row, columnBegin, 1, columnWidth, layoutSettings);
   }

   public GridLayout getGrid() {
      return this.this$0;
   }

   public LayoutSettings newCellSettings() {
      return this.this$0.newCellSettings();
   }

   public LayoutSettings defaultCellSetting() {
      return this.this$0.defaultCellSetting();
   }
}
