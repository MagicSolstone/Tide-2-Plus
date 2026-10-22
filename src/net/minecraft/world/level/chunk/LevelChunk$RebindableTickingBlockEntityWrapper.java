package net.minecraft.world.level.chunk;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.TickingBlockEntity;

class LevelChunk$RebindableTickingBlockEntityWrapper implements TickingBlockEntity {
   private TickingBlockEntity ticker;

   private LevelChunk$RebindableTickingBlockEntityWrapper(final TickingBlockEntity ticker) {
      this.ticker = ticker;
   }

   private void rebind(final TickingBlockEntity ticker) {
      this.ticker = ticker;
   }

   @Override
   public void tick() {
      this.ticker.tick();
   }

   @Override
   public boolean isRemoved() {
      return this.ticker.isRemoved();
   }

   @Override
   public BlockPos getPos() {
      return this.ticker.getPos();
   }

   @Override
   public String getType() {
      return this.ticker.getType();
   }

   @Override
   public String toString() {
      return this.ticker + " <wrapped>";
   }
}
