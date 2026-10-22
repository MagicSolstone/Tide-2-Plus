package net.minecraft.client.gui.layouts;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class GridLayout$ChildContainer extends AbstractLayout$AbstractChildWrapper {
   private final int row;
   private final int column;
   private final int occupiedRows;
   private final int occupiedColumns;

   private GridLayout$ChildContainer(
      final LayoutElement widget, final int row, final int column, final int occupiedRows, final int occupiedColumns, final LayoutSettings cellSettings
   ) {
      super(widget, cellSettings.getExposed());
      this.row = row;
      this.column = column;
      this.occupiedRows = occupiedRows;
      this.occupiedColumns = occupiedColumns;
   }

   public int getLastOccupiedRow() {
      return this.row + this.occupiedRows - 1;
   }

   public int getLastOccupiedColumn() {
      return this.column + this.occupiedColumns - 1;
   }
}
