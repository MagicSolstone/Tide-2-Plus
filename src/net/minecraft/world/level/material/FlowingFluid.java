package net.minecraft.world.level.material;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2ByteLinkedOpenHashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos$MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction$Plane;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition$Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class FlowingFluid extends Fluid {
   public static final BooleanProperty FALLING = BlockStateProperties.FALLING;
   public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_FLOWING;
   private static final int CACHE_SIZE = 200;
   private static final ThreadLocal<Object2ByteLinkedOpenHashMap<FlowingFluid$BlockStatePairKey>> OCCLUSION_CACHE = ThreadLocal.withInitial(() -> {
      Object2ByteLinkedOpenHashMap<FlowingFluid$BlockStatePairKey> map = new FlowingFluid$1(200);
      map.defaultReturnValue((byte)127);
      return map;
   });
   private final Map<FluidState, VoxelShape> shapes = Maps.newIdentityHashMap();

   @Override
   protected void createFluidStateDefinition(final StateDefinition$Builder<Fluid, FluidState> builder) {
      builder.add(FALLING);
   }

   @Override
   public Vec3 getFlow(final BlockGetter level, final BlockPos pos, final FluidState fluidState) {
      double flowX = 0.0;
      double flowZ = 0.0;
      BlockPos$MutableBlockPos blockPos = new BlockPos$MutableBlockPos();

      for (Direction direction : Direction$Plane.HORIZONTAL) {
         blockPos.setWithOffset(pos, direction);
         FluidState neighbourFluid = level.getFluidState(blockPos);
         if (this.affectsFlow(neighbourFluid)) {
            float neighborHeight = neighbourFluid.getOwnHeight();
            float distance = 0.0F;
            if (neighborHeight == 0.0F) {
               if (!level.getBlockState(blockPos).is(BlockTags.BLOCKS_FLUID_FLOW)) {
                  BlockPos neighborPos = blockPos.below();
                  FluidState belowNeighborState = level.getFluidState(neighborPos);
                  if (this.affectsFlow(belowNeighborState)) {
                     neighborHeight = belowNeighborState.getOwnHeight();
                     if (neighborHeight > 0.0F) {
                        distance = fluidState.getOwnHeight() - (neighborHeight - 0.8888889F);
                     }
                  }
               }
            } else if (neighborHeight > 0.0F) {
               distance = fluidState.getOwnHeight() - neighborHeight;
            }

            if (distance != 0.0F) {
               flowX += direction.getStepX() * distance;
               flowZ += direction.getStepZ() * distance;
            }
         }
      }

      Vec3 flow = new Vec3(flowX, 0.0, flowZ);
      if (fluidState.getValue(FALLING)) {
         for (Direction directionx : Direction$Plane.HORIZONTAL) {
            blockPos.setWithOffset(pos, directionx);
            if (this.isSolidFace(level, blockPos, directionx) || this.isSolidFace(level, blockPos.above(), directionx)) {
               flow = flow.normalize().add(0.0, -6.0, 0.0);
               break;
            }
         }
      }

      return flow.normalize();
   }

   private boolean affectsFlow(final FluidState neighbourFluid) {
      return neighbourFluid.isEmpty() || neighbourFluid.getType().isSame(this);
   }

   protected boolean isSolidFace(final BlockGetter level, final BlockPos pos, final Direction direction) {
      BlockState state = level.getBlockState(pos);
      FluidState fluidState = level.getFluidState(pos);
      if (fluidState.getType().isSame(this)) {
         return false;
      } else if (direction == Direction.UP) {
         return true;
      } else {
         return state.getBlock() instanceof IceBlock ? false : state.isFaceSturdy(level, pos, direction);
      }
   }

   protected void spread(final ServerLevel level, final BlockPos pos, final BlockState state, final FluidState fluidState) {
      if (!fluidState.isEmpty()) {
         BlockPos belowPos = pos.below();
         BlockState belowState = level.getBlockState(belowPos);
         FluidState belowFluid = belowState.getFluidState();
         if (this.canMaybePassThrough(level, pos, state, Direction.DOWN, belowPos, belowState, belowFluid)) {
            FluidState newBelowFluid = this.getNewLiquid(level, belowPos, belowState);
            Fluid newBelowFluidType = newBelowFluid.getType();
            if (belowFluid.canBeReplacedWith(level, belowPos, newBelowFluidType, Direction.DOWN)
               && canHoldSpecificFluid(level, belowPos, belowState, newBelowFluidType)) {
               this.spreadTo(level, belowPos, belowState, Direction.DOWN, newBelowFluid);
               if (this.sourceNeighborCount(level, pos) >= 3) {
                  this.spreadToSides(level, pos, fluidState, state);
               }

               return;
            }
         }

         if (fluidState.isSource() || !this.isWaterHole(level, pos, state, belowPos, belowState)) {
            this.spreadToSides(level, pos, fluidState, state);
         }
      }
   }

   private void spreadToSides(final ServerLevel level, final BlockPos pos, final FluidState fluidState, final BlockState state) {
      int neighbor = fluidState.getAmount() - this.getDropOff(level);
      if (fluidState.getValue(FALLING)) {
         neighbor = 7;
      }

      if (neighbor > 0) {
         Map<Direction, FluidState> spreads = this.getSpread(level, pos, state);

         for (Entry<Direction, FluidState> entry : spreads.entrySet()) {
            Direction spread = entry.getKey();
            FluidState newNeighborFluid = entry.getValue();
            BlockPos neighborPos = pos.relative(spread);
            this.spreadTo(level, neighborPos, level.getBlockState(neighborPos), spread, newNeighborFluid);
         }
      }
   }

   protected FluidState getNewLiquid(final ServerLevel level, final BlockPos pos, final BlockState state) {
      int highestNeighbor = 0;
      int neighbourSources = 0;
      BlockPos$MutableBlockPos mutablePos = new BlockPos$MutableBlockPos();

      for (Direction direction : Direction$Plane.HORIZONTAL) {
         BlockPos relativePos = mutablePos.setWithOffset(pos, direction);
         BlockState blockState = level.getBlockState(relativePos);
         FluidState fluidState = blockState.getFluidState();
         if (fluidState.getType().isSame(this) && canPassThroughWall(direction, level, pos, state, relativePos, blockState)) {
            if (fluidState.isSource()) {
               neighbourSources++;
            }

            highestNeighbor = Math.max(highestNeighbor, fluidState.getAmount());
         }
      }

      if (neighbourSources >= 2 && this.canConvertToSource(level)) {
         BlockState belowState = level.getBlockState(mutablePos.setWithOffset(pos, Direction.DOWN));
         FluidState belowFluid = belowState.getFluidState();
         if (belowState.isSolid() || this.isSourceBlockOfThisType(belowFluid)) {
            return this.getSource(false);
         }
      }

      BlockPos abovePos = mutablePos.setWithOffset(pos, Direction.UP);
      BlockState aboveState = level.getBlockState(abovePos);
      FluidState aboveFluid = aboveState.getFluidState();
      if (!aboveFluid.isEmpty() && aboveFluid.getType().isSame(this) && canPassThroughWall(Direction.UP, level, pos, state, abovePos, aboveState)) {
         return this.getFlowing(8, true);
      } else {
         int amount = highestNeighbor - this.getDropOff(level);
         return amount <= 0 ? Fluids.EMPTY.defaultFluidState() : this.getFlowing(amount, false);
      }
   }

   private static boolean canPassThroughWall(
      final Direction direction,
      final BlockGetter level,
      final BlockPos sourcePos,
      final BlockState sourceState,
      final BlockPos targetPos,
      final BlockState targetState
   ) {
      if (!SharedConstants.DEBUG_DISABLE_LIQUID_SPREADING && (!SharedConstants.DEBUG_ONLY_GENERATE_HALF_THE_WORLD || targetPos.getZ() >= 0)) {
         VoxelShape targetShape = targetState.getCollisionShape(level, targetPos);
         if (targetShape == Shapes.block()) {
            return false;
         } else {
            VoxelShape sourceShape = sourceState.getCollisionShape(level, sourcePos);
            if (sourceShape == Shapes.block()) {
               return false;
            } else if (sourceShape == Shapes.empty() && targetShape == Shapes.empty()) {
               return true;
            } else {
               Object2ByteLinkedOpenHashMap<FlowingFluid$BlockStatePairKey> cache;
               if (!sourceState.getBlock().hasDynamicShape() && !targetState.getBlock().hasDynamicShape()) {
                  cache = OCCLUSION_CACHE.get();
               } else {
                  cache = null;
               }

               FlowingFluid$BlockStatePairKey key;
               if (cache != null) {
                  key = new FlowingFluid$BlockStatePairKey(sourceState, targetState, direction);
                  byte cached = cache.getAndMoveToFirst(key);
                  if (cached != 127) {
                     return cached != 0;
                  }
               } else {
                  key = null;
               }

               boolean result = !Shapes.mergedFaceOccludes(sourceShape, targetShape, direction);
               if (cache != null) {
                  if (cache.size() == 200) {
                     cache.removeLastByte();
                  }

                  cache.putAndMoveToFirst(key, (byte)(result ? 1 : 0));
               }

               return result;
            }
         }
      } else {
         return false;
      }
   }

   public abstract Fluid getFlowing();

   public FluidState getFlowing(final int amount, final boolean falling) {
      return this.getFlowing().defaultFluidState().setValue(LEVEL, amount).setValue(FALLING, falling);
   }

   public abstract Fluid getSource();

   public FluidState getSource(final boolean falling) {
      return this.getSource().defaultFluidState().setValue(FALLING, falling);
   }

   protected abstract boolean canConvertToSource(ServerLevel level);

   protected void spreadTo(final LevelAccessor level, final BlockPos pos, final BlockState state, final Direction direction, final FluidState target) {
      if (state.getBlock() instanceof LiquidBlockContainer container) {
         container.placeLiquid(level, pos, state, target);
      } else {
         if (!state.isAir()) {
            this.beforeDestroyingBlock(level, pos, state);
         }

         level.setBlockAndUpdate(pos, target.createLegacyBlock());
      }
   }

   protected abstract void beforeDestroyingBlock(LevelAccessor level, BlockPos pos, BlockState state);

   protected int getSlopeDistance(
      final LevelReader level, final BlockPos pos, final int pass, final Direction from, final BlockState state, final FlowingFluid$SpreadContext context
   ) {
      int lowest = 1000;

      for (Direction direction : Direction$Plane.HORIZONTAL) {
         if (direction != from) {
            BlockPos testPos = pos.relative(direction);
            BlockState testState = context.getBlockState(testPos);
            FluidState testFluidState = testState.getFluidState();
            if (this.canPassThrough(level, this.getFlowing(), pos, state, direction, testPos, testState, testFluidState)) {
               if (context.isHole(testPos)) {
                  return pass;
               }

               if (pass < this.getSlopeFindDistance(level)) {
                  int v = this.getSlopeDistance(level, testPos, pass + 1, direction.getOpposite(), testState, context);
                  if (v < lowest) {
                     lowest = v;
                  }
               }
            }
         }
      }

      return lowest;
   }

   private boolean isWaterHole(
      final BlockGetter level, final BlockPos topPos, final BlockState topState, final BlockPos bottomPos, final BlockState bottomState
   ) {
      if (!canPassThroughWall(Direction.DOWN, level, topPos, topState, bottomPos, bottomState)) {
         return false;
      } else {
         return bottomState.getFluidState().getType().isSame(this) ? true : canHoldFluid(level, bottomPos, bottomState, this.getFlowing());
      }
   }

   private boolean canPassThrough(
      final BlockGetter level,
      final Fluid fluid,
      final BlockPos sourcePos,
      final BlockState sourceState,
      final Direction direction,
      final BlockPos testPos,
      final BlockState testState,
      final FluidState testFluidState
   ) {
      return this.canMaybePassThrough(level, sourcePos, sourceState, direction, testPos, testState, testFluidState)
         && canHoldSpecificFluid(level, testPos, testState, fluid);
   }

   private boolean canMaybePassThrough(
      final BlockGetter level,
      final BlockPos sourcePos,
      final BlockState sourceState,
      final Direction direction,
      final BlockPos testPos,
      final BlockState testState,
      final FluidState testFluidState
   ) {
      return !this.isSourceBlockOfThisType(testFluidState)
         && canHoldAnyFluid(testState)
         && canPassThroughWall(direction, level, sourcePos, sourceState, testPos, testState);
   }

   private boolean isSourceBlockOfThisType(final FluidState state) {
      return state.getType().isSame(this) && state.isSource();
   }

   protected abstract int getSlopeFindDistance(LevelReader level);

   private int sourceNeighborCount(final LevelReader level, final BlockPos pos) {
      int count = 0;

      for (Direction direction : Direction$Plane.HORIZONTAL) {
         BlockPos testPos = pos.relative(direction);
         FluidState testFluidState = level.getFluidState(testPos);
         if (this.isSourceBlockOfThisType(testFluidState)) {
            count++;
         }
      }

      return count;
   }

   protected Map<Direction, FluidState> getSpread(final ServerLevel level, final BlockPos pos, final BlockState state) {
      int lowest = 1000;
      Map<Direction, FluidState> result = Maps.newEnumMap(Direction.class);
      FlowingFluid$SpreadContext context = null;

      for (Direction direction : Direction$Plane.HORIZONTAL) {
         BlockPos testPos = pos.relative(direction);
         BlockState testState = level.getBlockState(testPos);
         FluidState testFluidState = testState.getFluidState();
         if (this.canMaybePassThrough(level, pos, state, direction, testPos, testState, testFluidState)) {
            FluidState newFluid = this.getNewLiquid(level, testPos, testState);
            if (canHoldSpecificFluid(level, testPos, testState, newFluid.getType())) {
               if (context == null) {
                  context = new FlowingFluid$SpreadContext(this, level, pos);
               }

               int distance;
               if (context.isHole(testPos)) {
                  distance = 0;
               } else {
                  distance = this.getSlopeDistance(level, testPos, 1, direction.getOpposite(), testState, context);
               }

               if (distance < lowest) {
                  result.clear();
               }

               if (distance <= lowest) {
                  if (testFluidState.canBeReplacedWith(level, testPos, newFluid.getType(), direction)) {
                     result.put(direction, newFluid);
                  }

                  lowest = distance;
               }
            }
         }
      }

      return result;
   }

   private static boolean canHoldAnyFluid(final BlockState state) {
      Block block = state.getBlock();
      return block instanceof LiquidBlockContainer ? true : state.is(BlockTags.WASHED_AWAY_BY_FLUIDS);
   }

   private static boolean canHoldFluid(final BlockGetter level, final BlockPos pos, final BlockState state, final Fluid newFluid) {
      return canHoldAnyFluid(state) && canHoldSpecificFluid(level, pos, state, newFluid);
   }

   private static boolean canHoldSpecificFluid(final BlockGetter level, final BlockPos pos, final BlockState state, final Fluid newFluid) {
      return state.getBlock() instanceof LiquidBlockContainer container ? container.canPlaceLiquid(null, level, pos, state, newFluid) : true;
   }

   protected abstract int getDropOff(LevelReader level);

   protected int getSpreadDelay(final Level level, final BlockPos pos, final FluidState oldFluidState, final FluidState newFluidState) {
      return this.getTickDelay(level);
   }

   @Override
   public void tick(final ServerLevel level, final BlockPos pos, BlockState blockState, FluidState fluidState) {
      if (!fluidState.isSource()) {
         FluidState newFluidState = this.getNewLiquid(level, pos, level.getBlockState(pos));
         int tickDelay = this.getSpreadDelay(level, pos, fluidState, newFluidState);
         if (newFluidState.isEmpty()) {
            fluidState = newFluidState;
            blockState = Blocks.AIR.defaultBlockState();
            level.setBlockAndUpdate(pos, blockState);
         } else if (newFluidState != fluidState) {
            fluidState = newFluidState;
            blockState = newFluidState.createLegacyBlock();
            level.setBlockAndUpdate(pos, blockState);
            level.scheduleTick(pos, newFluidState.getType(), tickDelay);
         }
      }

      this.spread(level, pos, blockState, fluidState);
   }

   protected static int getLegacyLevel(final FluidState fluidState) {
      return fluidState.isSource() ? 0 : 8 - Math.min(fluidState.getAmount(), 8) + (fluidState.getValue(FALLING) ? 8 : 0);
   }

   private static boolean hasSameAbove(final FluidState fluidState, final BlockGetter level, final BlockPos pos) {
      return fluidState.getType().isSame(level.getFluidState(pos.above()).getType());
   }

   @Override
   public float getHeight(final FluidState fluidState, final BlockGetter level, final BlockPos pos) {
      return hasSameAbove(fluidState, level, pos) ? 1.0F : fluidState.getOwnHeight();
   }

   @Override
   public float getOwnHeight(final FluidState fluidState) {
      return fluidState.getAmount() / 9.0F;
   }

   @Override
   public abstract int getAmount(final FluidState fluidState);

   @Override
   public VoxelShape getShape(final FluidState state, final BlockGetter level, final BlockPos pos) {
      return state.getAmount() == 9 && hasSameAbove(state, level, pos)
         ? Shapes.block()
         : this.shapes.computeIfAbsent(state, fluidState -> Shapes.box(0.0, 0.0, 0.0, 1.0, fluidState.getHeight(level, pos), 1.0));
   }
}
