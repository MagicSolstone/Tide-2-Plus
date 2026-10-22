package net.minecraft.world.level.block.state.pattern;

import com.google.common.base.MoreObjects;
import com.google.common.cache.LoadingCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class BlockPattern$BlockPatternMatch {
   private final BlockPos frontTopLeft;
   private final Direction forwards;
   private final Direction up;
   private final LoadingCache<BlockPos, BlockInWorld> cache;
   private final int width;
   private final int height;
   private final int depth;

   public BlockPattern$BlockPatternMatch(
      final BlockPos frontTopLeft,
      final Direction forwards,
      final Direction up,
      final LoadingCache<BlockPos, BlockInWorld> cache,
      final int width,
      final int height,
      final int depth
   ) {
      this.frontTopLeft = frontTopLeft;
      this.forwards = forwards;
      this.up = up;
      this.cache = cache;
      this.width = width;
      this.height = height;
      this.depth = depth;
   }

   public BlockPos getFrontTopLeft() {
      return this.frontTopLeft;
   }

   public Direction getForwards() {
      return this.forwards;
   }

   public Direction getUp() {
      return this.up;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public int getDepth() {
      return this.depth;
   }

   public BlockInWorld getBlock(final int right, final int down, final int forwards) {
      return (BlockInWorld)this.cache.getUnchecked(BlockPattern.translateAndRotate(this.frontTopLeft, this.getForwards(), this.getUp(), right, down, forwards));
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this).add("up", this.up).add("forwards", this.forwards).add("frontTopLeft", this.frontTopLeft).toString();
   }
}
