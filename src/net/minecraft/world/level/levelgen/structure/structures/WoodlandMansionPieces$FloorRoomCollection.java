package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.util.RandomSource;

abstract class WoodlandMansionPieces$FloorRoomCollection {
   private WoodlandMansionPieces$FloorRoomCollection() {
   }

   public abstract String get1x1(RandomSource random);

   public abstract String get1x1Secret(RandomSource random);

   public abstract String get1x2SideEntrance(RandomSource random, boolean isStairsRoom);

   public abstract String get1x2FrontEntrance(RandomSource random, boolean isStairsRoom);

   public abstract String get1x2Secret(RandomSource random);

   public abstract String get2x2(RandomSource random);

   public abstract String get2x2Secret(RandomSource random);
}
