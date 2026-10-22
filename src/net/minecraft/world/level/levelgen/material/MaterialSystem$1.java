package net.minecraft.world.level.levelgen.material;

import java.util.Objects;
import net.minecraft.core.BlockPos$MutableBlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.BlockColumn;
import net.minecraft.world.level.chunk.ChunkAccess;

class MaterialSystem$1 implements BlockColumn {
   MaterialSystem$1(
      final MaterialSystem this$0,
      final ChunkAccess val$protoChunk,
      final BlockPos$MutableBlockPos val$columnPos,
      final int val$minY,
      final int val$maxY,
      final ChunkPos val$chunkPos
   ) {
      this.val$protoChunk = val$protoChunkx;
      this.val$columnPos = val$columnPosx;
      this.val$minY = val$minYx;
      this.val$maxY = val$maxYx;
      this.val$chunkPos = val$chunkPosx;
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public BlockState getBlock(final int blockY) {
      return this.val$protoChunk.getBlockState(this.val$columnPos.setY(blockY));
   }

   @Override
   public void setBlock(final int blockY, final BlockState state) {
      if (blockY >= this.val$minY && blockY <= this.val$maxY) {
         this.val$protoChunk.setBlockState(this.val$columnPos.setY(blockY), state);
         if (!state.getFluidState().isEmpty()) {
            this.val$protoChunk.markPosForPostProcessing(this.val$columnPos);
         }
      }
   }

   @Override
   public String toString() {
      return "ChunkBlockColumn " + this.val$chunkPos;
   }
}
