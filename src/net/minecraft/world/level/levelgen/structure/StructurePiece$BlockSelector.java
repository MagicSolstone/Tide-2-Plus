package net.minecraft.world.level.levelgen.structure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public abstract class StructurePiece$BlockSelector {
   protected BlockState next = Blocks.AIR.defaultBlockState();

   public abstract void next(RandomSource random, int worldX, int worldY, int worldZ, boolean isEdge);

   public BlockState getNext() {
      return this.next;
   }
}
