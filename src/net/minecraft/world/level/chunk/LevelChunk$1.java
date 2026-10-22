package net.minecraft.world.level.chunk;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.TickingBlockEntity;

class LevelChunk$1 implements TickingBlockEntity {
   @Override
   public void tick() {
   }

   @Override
   public boolean isRemoved() {
      return true;
   }

   @Override
   public BlockPos getPos() {
      return BlockPos.ZERO;
   }

   @Override
   public String getType() {
      return "<null>";
   }
}
