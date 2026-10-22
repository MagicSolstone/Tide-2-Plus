package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.StructurePiece$BlockSelector;

class JungleTemplePiece$MossStoneSelector extends StructurePiece$BlockSelector {
   private JungleTemplePiece$MossStoneSelector() {
   }

   @Override
   public void next(final RandomSource random, final int worldX, final int worldY, final int worldZ, final boolean isEdge) {
      if (random.nextFloat() < 0.4F) {
         this.next = Blocks.COBBLESTONE.defaultBlockState();
      } else {
         this.next = Blocks.MOSSY_COBBLESTONE.defaultBlockState();
      }
   }
}
