package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.util.RandomSource;

class WoodlandMansionPieces$FirstFloorRoomCollection extends WoodlandMansionPieces$FloorRoomCollection {
   private WoodlandMansionPieces$FirstFloorRoomCollection() {
   }

   @Override
   public String get1x1(final RandomSource random) {
      return "1x1_a" + (random.nextInt(5) + 1);
   }

   @Override
   public String get1x1Secret(final RandomSource random) {
      return "1x1_as" + (random.nextInt(4) + 1);
   }

   @Override
   public String get1x2SideEntrance(final RandomSource random, final boolean isStairsRoom) {
      return "1x2_a" + (random.nextInt(9) + 1);
   }

   @Override
   public String get1x2FrontEntrance(final RandomSource random, final boolean isStairsRoom) {
      return "1x2_b" + (random.nextInt(5) + 1);
   }

   @Override
   public String get1x2Secret(final RandomSource random) {
      return "1x2_s" + (random.nextInt(2) + 1);
   }

   @Override
   public String get2x2(final RandomSource random) {
      return "2x2_a" + (random.nextInt(4) + 1);
   }

   @Override
   public String get2x2Secret(final RandomSource random) {
      return "2x2_s1";
   }
}
