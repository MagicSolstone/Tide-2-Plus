package net.minecraft.util.datafix.fixes;

public enum ChunkPalettedStorageFix$Direction {
   DOWN(ChunkPalettedStorageFix$Direction$AxisDirection.NEGATIVE, ChunkPalettedStorageFix$Direction$Axis.Y),
   UP(ChunkPalettedStorageFix$Direction$AxisDirection.POSITIVE, ChunkPalettedStorageFix$Direction$Axis.Y),
   NORTH(ChunkPalettedStorageFix$Direction$AxisDirection.NEGATIVE, ChunkPalettedStorageFix$Direction$Axis.Z),
   SOUTH(ChunkPalettedStorageFix$Direction$AxisDirection.POSITIVE, ChunkPalettedStorageFix$Direction$Axis.Z),
   WEST(ChunkPalettedStorageFix$Direction$AxisDirection.NEGATIVE, ChunkPalettedStorageFix$Direction$Axis.X),
   EAST(ChunkPalettedStorageFix$Direction$AxisDirection.POSITIVE, ChunkPalettedStorageFix$Direction$Axis.X);

   private final ChunkPalettedStorageFix$Direction$Axis axis;
   private final ChunkPalettedStorageFix$Direction$AxisDirection axisDirection;

   private ChunkPalettedStorageFix$Direction(
      final ChunkPalettedStorageFix$Direction$AxisDirection axisDirection, final ChunkPalettedStorageFix$Direction$Axis axis
   ) {
      this.axis = axis;
      this.axisDirection = axisDirection;
   }

   public ChunkPalettedStorageFix$Direction$AxisDirection getAxisDirection() {
      return this.axisDirection;
   }

   public ChunkPalettedStorageFix$Direction$Axis getAxis() {
      return this.axis;
   }
}
