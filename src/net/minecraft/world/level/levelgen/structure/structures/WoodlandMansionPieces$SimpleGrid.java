package net.minecraft.world.level.levelgen.structure.structures;

class WoodlandMansionPieces$SimpleGrid {
   private final int[][] grid;
   private final int width;
   private final int height;
   private final int valueIfOutside;

   public WoodlandMansionPieces$SimpleGrid(final int width, final int height, final int valueIfOutside) {
      this.width = width;
      this.height = height;
      this.valueIfOutside = valueIfOutside;
      this.grid = new int[width][height];
   }

   public void set(final int x, final int y, final int value) {
      if (x >= 0 && x < this.width && y >= 0 && y < this.height) {
         this.grid[x][y] = value;
      }
   }

   public void set(final int x0, final int y0, final int x1, final int y1, final int value) {
      for (int y = y0; y <= y1; y++) {
         for (int x = x0; x <= x1; x++) {
            this.set(x, y, value);
         }
      }
   }

   public int get(final int x, final int y) {
      return x >= 0 && x < this.width && y >= 0 && y < this.height ? this.grid[x][y] : this.valueIfOutside;
   }

   public void setif(final int x, final int y, final int ifValue, final int value) {
      if (this.get(x, y) == ifValue) {
         this.set(x, y, value);
      }
   }

   public boolean edgesTo(final int x, final int y, final int ifValue) {
      return this.get(x - 1, y) == ifValue || this.get(x + 1, y) == ifValue || this.get(x, y + 1) == ifValue || this.get(x, y - 1) == ifValue;
   }
}
