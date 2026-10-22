package net.minecraft.util.datafix.fixes;

public enum ChunkPalettedStorageFix$Direction$AxisDirection {
   POSITIVE(1),
   NEGATIVE(-1);

   private final int step;

   private ChunkPalettedStorageFix$Direction$AxisDirection(final int step) {
      this.step = step;
   }

   public int getStep() {
      return this.step;
   }
}
