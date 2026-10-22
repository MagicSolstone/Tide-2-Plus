package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.util.RandomSource;

class WoodlandMansionPieces$SecondFloorRoomCollection extends WoodlandMansionPieces$FloorRoomCollection {
   private WoodlandMansionPieces$SecondFloorRoomCollection() {
   }

   @Override
   public String get1x1(final RandomSource random) {
      return "1x1_b" + (random.nextInt(5) + 1);
   }

   @Override
   public String get1x1Secret(final RandomSource random) {
      return "1x1_as" + (random.nextInt(4) + 1);
   }

   @Override
   public String get1x2SideEntrance(final RandomSource random, final boolean isStairsRoom) {
      return isStairsRoom ? "1x2_c_stairs" : "1x2_c" + (random.nextInt(4) + 1);
   }

   @Override
   public String get1x2FrontEntrance(final RandomSource random, final boolean isStairsRoom) {
      return isStairsRoom ? "1x2_d_stairs" : "1x2_d" + (random.nextInt(5) + 1);
   }

   @Override
   public String get1x2Secret(final RandomSource random) {
      return "1x2_se" + (random.nextInt(1) + 1);
   }

   @Override
   public String get2x2(final RandomSource random) {
      return "2x2_b" + (random.nextInt(5) + 1);
   }

   @Override
   public String get2x2Secret(final RandomSource random) {
      return "2x2_s1";
   }
}
