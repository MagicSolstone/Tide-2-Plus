package net.minecraft.world.level.material;

import it.unimi.dsi.fastutil.shorts.Short2BooleanMap;
import it.unimi.dsi.fastutil.shorts.Short2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class FlowingFluid$SpreadContext {
   private final BlockGetter level;
   private final BlockPos origin;
   private final Short2ObjectMap<BlockState> stateCache;
   private final Short2BooleanMap holeCache;

   private FlowingFluid$SpreadContext(final FlowingFluid this$0, final BlockGetter level, final BlockPos origin) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.stateCache = new Short2ObjectOpenHashMap();
      this.holeCache = new Short2BooleanOpenHashMap();
      this.level = level;
      this.origin = origin;
   }

   public BlockState getBlockState(final BlockPos pos) {
      return this.getBlockState(pos, this.getCacheKey(pos));
   }

   private BlockState getBlockState(final BlockPos pos, final short key) {
      return (BlockState)this.stateCache.computeIfAbsent(key, k -> this.level.getBlockState(pos));
   }

   public boolean isHole(final BlockPos pos) {
      return this.holeCache.computeIfAbsent(this.getCacheKey(pos), key -> {
         BlockState state = this.getBlockState(pos, key);
         BlockPos below = pos.below();
         BlockState belowState = this.level.getBlockState(below);
         return this.this$0.isWaterHole(this.level, pos, state, below, belowState);
      });
   }

   private short getCacheKey(final BlockPos pos) {
      int relativeX = pos.getX() - this.origin.getX();
      int relativeZ = pos.getZ() - this.origin.getZ();
      return (short)((relativeX + 128 & 0xFF) << 8 | relativeZ + 128 & 0xFF);
   }
}
