package net.minecraft.client.gui.layouts;

import com.mojang.math.Divisor;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Util;

@Environment(EnvType.CLIENT)
public class GridLayout extends AbstractLayout {
   private final List<GridLayout$ChildContainer> children = new ArrayList<>();
   private final LayoutSettings defaultCellSettings = LayoutSettings.defaults();
   private int rowSpacing = 0;
   private int columnSpacing = 0;

   public GridLayout() {
      this(0, 0);
   }

   public GridLayout(final int x, final int y) {
      super(x, y, 0, 0);
   }

   @Override
   public void arrangeElements() {
      super.arrangeElements();
      int maxRow = 0;
      int maxColumn = 0;

      for (GridLayout$ChildContainer child : this.children) {
         maxRow = Math.max(child.getLastOccupiedRow(), maxRow);
         maxColumn = Math.max(child.getLastOccupiedColumn(), maxColumn);
      }

      int[] maxColumnWidths = new int[maxColumn + 1];
      int[] maxRowHeights = new int[maxRow + 1];

      for (GridLayout$ChildContainer child : this.children) {
         int childHeight = child.getHeight() - (child.occupiedRows - 1) * this.rowSpacing;
         Divisor heightDivisor = new Divisor(childHeight, child.occupiedRows);

         for (int row = child.row; row <= child.getLastOccupiedRow(); row++) {
            maxRowHeights[row] = Math.max(maxRowHeights[row], heightDivisor.nextInt());
         }

         int childWidth = child.getWidth() - (child.occupiedColumns - 1) * this.columnSpacing;
         Divisor widthDivisor = new Divisor(childWidth, child.occupiedColumns);

         for (int column = child.column; column <= child.getLastOccupiedColumn(); column++) {
            maxColumnWidths[column] = Math.max(maxColumnWidths[column], widthDivisor.nextInt());
         }
      }

      int[] columnXOffsets = new int[maxColumn + 1];
      int[] rowYOffsets = new int[maxRow + 1];
      columnXOffsets[0] = 0;

      for (int column = 1; column <= maxColumn; column++) {
         columnXOffsets[column] = columnXOffsets[column - 1] + maxColumnWidths[column - 1] + this.columnSpacing;
      }

      rowYOffsets[0] = 0;

      for (int row = 1; row <= maxRow; row++) {
         rowYOffsets[row] = rowYOffsets[row - 1] + maxRowHeights[row - 1] + this.rowSpacing;
      }

      for (GridLayout$ChildContainer child : this.children) {
         int availableWidth = 0;

         for (int column = child.column; column <= child.getLastOccupiedColumn(); column++) {
            availableWidth += maxColumnWidths[column];
         }

         availableWidth += this.columnSpacing * (child.occupiedColumns - 1);
         child.setX(this.getX() + columnXOffsets[child.column], availableWidth);
         int availableHeight = 0;

         for (int row = child.row; row <= child.getLastOccupiedRow(); row++) {
            availableHeight += maxRowHeights[row];
         }

         availableHeight += this.rowSpacing * (child.occupiedRows - 1);
         child.setY(this.getY() + rowYOffsets[child.row], availableHeight);
      }

      this.width = columnXOffsets[maxColumn] + maxColumnWidths[maxColumn];
      this.height = rowYOffsets[maxRow] + maxRowHeights[maxRow];
   }

   public <T extends LayoutElement> T addChild(final T child, final int row, final int column) {
      return this.addChild(child, row, column, this.newCellSettings());
   }

   public <T extends LayoutElement> T addChild(final T child, final int row, final int column, final LayoutSettings cellSettings) {
      return this.addChild(child, row, column, 1, 1, cellSettings);
   }

   public <T extends LayoutElement> T addChild(final T child, final int row, final int column, final Consumer<LayoutSettings> layoutSettingsAdjustments) {
      return this.addChild(child, row, column, 1, 1, Util.make(this.newCellSettings(), layoutSettingsAdjustments));
   }

   public <T extends LayoutElement> T addChild(final T child, final int row, final int column, final int rows, final int columns) {
      return this.addChild(child, row, column, rows, columns, this.newCellSettings());
   }

   public <T extends LayoutElement> T addChild(
      final T child, final int row, final int column, final int rows, final int columns, final LayoutSettings cellSettings
   ) {
      if (rows < 1) {
         throw new IllegalArgumentException("Occupied rows must be at least 1");
      } else if (columns < 1) {
         throw new IllegalArgumentException("Occupied columns must be at least 1");
      } else {
         this.children.add(new GridLayout$ChildContainer(child, row, column, rows, columns, cellSettings));
         return child;
      }
   }

   public <T extends LayoutElement> T addChild(
      final T child, final int row, final int column, final int rows, final int columns, final Consumer<LayoutSettings> layoutSettingsAdjustments
   ) {
      return this.addChild(child, row, column, rows, columns, Util.make(this.newCellSettings(), layoutSettingsAdjustments));
   }

   public GridLayout columnSpacing(final int columnSpacing) {
      this.columnSpacing = columnSpacing;
      return this;
   }

   public GridLayout rowSpacing(final int rowSpacing) {
      this.rowSpacing = rowSpacing;
      return this;
   }

   public GridLayout spacing(final int spacing) {
      return this.columnSpacing(spacing).rowSpacing(spacing);
   }

   @Override
   public void visitChildren(final Consumer<LayoutElement> layoutElementVisitor) {
      this.children.forEach(child -> layoutElementVisitor.accept(child.child));
   }

   @Override
   public void removeChildren() {
      this.children.clear();
   }

   public LayoutSettings newCellSettings() {
      return this.defaultCellSettings.copy();
   }

   public LayoutSettings defaultCellSetting() {
      return this.defaultCellSettings;
   }

   public GridLayout$RowHelper createRowHelper(final int columns) {
      return new GridLayout$RowHelper(this, columns);
   }
}
