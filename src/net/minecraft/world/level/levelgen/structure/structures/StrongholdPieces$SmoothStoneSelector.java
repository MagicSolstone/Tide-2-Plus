package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.StructurePiece$BlockSelector;

class StrongholdPieces$SmoothStoneSelector extends StructurePiece$BlockSelector {
   private StrongholdPieces$SmoothStoneSelector() {
   }

   @Override
   public void next(final RandomSource random, final int worldX, final int worldY, final int worldZ, final boolean isEdge) {
      if (isEdge) {
         float selection = random.nextFloat();
         if (selection < 0.2F) {
            this.next = Blocks.CRACKED_STONE_BRICKS.defaultBlockState();
         } else if (selection < 0.5F) {
            this.next = Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
         } else if (selection < 0.55F) {
            this.next = Blocks.INFESTED_STONE_BRICKS.defaultBlockState();
         } else {
            this.next = Blocks.STONE_BRICKS.defaultBlockState();
         }
      } else {
         this.next = Blocks.CAVE_AIR.defaultBlockState();
      }
   }
}
