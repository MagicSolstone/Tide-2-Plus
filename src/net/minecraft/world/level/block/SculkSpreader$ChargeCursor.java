package net.minecraft.world.level.block;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos$MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction$Axis;
import net.minecraft.core.Direction$AxisDirection;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class SculkSpreader$ChargeCursor {
   private static final ObjectArrayList<Vec3i> NON_CORNER_NEIGHBOURS = Util.make(
      new ObjectArrayList(18),
      list -> BlockPos.betweenClosedStream(new BlockPos(-1, -1, -1), new BlockPos(1, 1, 1))
         .filter(position -> (position.getX() == 0 || position.getY() == 0 || position.getZ() == 0) && !position.equals(BlockPos.ZERO))
         .map(BlockPos::immutable)
         .forEach(list::add)
   );
   public static final int MAX_CURSOR_DECAY_DELAY = 1;
   private static final int MAX_WORLDGEN_SPREAD = 12;
   private BlockPos pos;
   private int charge;
   private int updateDelay;
   private int decayDelay;
   @Nullable
   private Set<Direction> facings;
   private static final Codec<Set<Direction>> DIRECTION_SET = Direction.CODEC.listOf().xmap(l -> Sets.newEnumSet(l, Direction.class), Lists::newArrayList);
   public static final Codec<SculkSpreader$ChargeCursor> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            BlockPos.CODEC.fieldOf("pos").forGetter(SculkSpreader$ChargeCursor::getPos),
            Codec.intRange(0, 1000).optionalFieldOf("charge", 0).forGetter(SculkSpreader$ChargeCursor::getCharge),
            Codec.intRange(0, 1).optionalFieldOf("decay_delay", 1).forGetter(SculkSpreader$ChargeCursor::getDecayDelay),
            Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("update_delay", 0).forGetter(o -> o.updateDelay),
            DIRECTION_SET.lenientOptionalFieldOf("facings").forGetter(o -> Optional.ofNullable(o.getFacingData()))
         )
         .apply(i, SculkSpreader$ChargeCursor::new)
   );

   private SculkSpreader$ChargeCursor(final BlockPos pos, final int charge, final int decayDelay, final int updateDelay, final Optional<Set<Direction>> facings) {
      this.pos = pos;
      this.charge = charge;
      this.decayDelay = decayDelay;
      this.updateDelay = updateDelay;
      this.facings = facings.orElse(null);
   }

   public SculkSpreader$ChargeCursor(final BlockPos pos, final int charge) {
      this(pos, charge, 1, 0, Optional.empty());
   }

   public BlockPos getPos() {
      return this.pos;
   }

   private boolean isPosUnreasonable(final BlockPos originPos) {
      return this.pos.distChessboard(originPos) > 1024;
   }

   public int getCharge() {
      return this.charge;
   }

   public int getDecayDelay() {
      return this.decayDelay;
   }

   @Nullable
   public Set<Direction> getFacingData() {
      return this.facings;
   }

   private boolean shouldUpdate(final LevelAccessor level, final BlockPos pos, final boolean isWorldGen) {
      if (this.charge <= 0) {
         return false;
      } else if (isWorldGen) {
         return true;
      } else {
         return level instanceof ServerLevel serverLevel ? serverLevel.shouldTickBlocksAt(pos) : false;
      }
   }

   public void update(final LevelAccessor level, final BlockPos originPos, final RandomSource random, final SculkSpreader spreader, final boolean spreadVeins) {
      if (this.shouldUpdate(level, originPos, spreader.isWorldGeneration)) {
         if (this.updateDelay > 0) {
            this.updateDelay--;
         } else {
            BlockState currentState = level.getBlockState(this.pos);
            SculkBehaviour sculkBehaviour = getBlockBehaviour(currentState);
            if (spreadVeins && sculkBehaviour.attemptSpreadVein(level, this.pos, currentState, this.facings, spreader.isWorldGeneration())) {
               if (sculkBehaviour.canChangeBlockStateOnSpread()) {
                  currentState = level.getBlockState(this.pos);
                  sculkBehaviour = getBlockBehaviour(currentState);
               }

               level.playSound(null, this.pos, SoundEvents.SCULK_BLOCK_SPREAD, SoundSource.BLOCKS, 1.0F, 1.0F);
            }

            this.charge = sculkBehaviour.attemptUseCharge(this, level, originPos, random, spreader, spreadVeins);
            if (this.charge <= 0) {
               sculkBehaviour.onDischarged(level, currentState, this.pos, random);
            } else {
               BlockPos transferPos = getValidMovementPos(level, this.pos, random, originPos, spreader);
               if (transferPos != null) {
                  sculkBehaviour.onDischarged(level, currentState, this.pos, random);
                  this.pos = transferPos.immutable();
                  currentState = level.getBlockState(transferPos);
               } else if (spreader.isWorldGeneration()) {
                  sculkBehaviour.onDischarged(level, currentState, this.pos, random);
                  this.charge = 0;
                  return;
               }

               if (currentState.getBlock() instanceof SculkBehaviour) {
                  this.facings = MultifaceBlock.availableFaces(currentState);
               }

               this.decayDelay = sculkBehaviour.updateDecayDelay(this.decayDelay);
               this.updateDelay = sculkBehaviour.getSculkSpreadDelay();
            }
         }
      }
   }

   private void mergeWith(final SculkSpreader$ChargeCursor other) {
      this.charge = this.charge + other.charge;
      other.charge = 0;
      this.updateDelay = Math.min(this.updateDelay, other.updateDelay);
   }

   private static SculkBehaviour getBlockBehaviour(final BlockState state) {
      return state.getBlock() instanceof SculkBehaviour behaviour ? behaviour : SculkBehaviour.DEFAULT;
   }

   private static List<Vec3i> getRandomizedNonCornerNeighbourOffsets(final RandomSource random) {
      return Util.shuffledCopy(NON_CORNER_NEIGHBOURS, random);
   }

   @Nullable
   private static BlockPos getValidMovementPos(
      final LevelAccessor level, final BlockPos pos, final RandomSource random, final BlockPos originPos, final SculkSpreader spreader
   ) {
      BlockPos$MutableBlockPos sculkPosition = pos.mutable();
      BlockPos$MutableBlockPos neighbour = pos.mutable();

      for (Vec3i offset : getRandomizedNonCornerNeighbourOffsets(random)) {
         neighbour.setWithOffset(pos, offset);
         if (canMoveToPos(originPos, neighbour, spreader)) {
            BlockState transferee = level.getBlockState(neighbour);
            if (transferee.getBlock() instanceof SculkBehaviour && isMovementUnobstructed(level, pos, neighbour)) {
               sculkPosition.set(neighbour);
               if (SculkVeinBlock.hasSubstrateAccess(level, transferee, neighbour)) {
                  break;
               }
            }
         }
      }

      return sculkPosition.equals(pos) ? null : sculkPosition;
   }

   private static boolean canMoveToPos(final BlockPos origin, final BlockPos target, final SculkSpreader spreader) {
      if (!spreader.isWorldGeneration()) {
         return true;
      } else {
         int distanceSq = Mth.square(origin.getX() - target.getX()) + Mth.square(origin.getZ() - target.getZ());
         return distanceSq <= 144;
      }
   }

   private static boolean isMovementUnobstructed(final LevelAccessor level, final BlockPos from, final BlockPos to) {
      if (from.distManhattan(to) == 1) {
         return true;
      } else {
         BlockPos delta = to.subtract(from);
         Direction directionX = Direction.fromAxisAndDirection(
            Direction$Axis.X, delta.getX() < 0 ? Direction$AxisDirection.NEGATIVE : Direction$AxisDirection.POSITIVE
         );
         Direction directionY = Direction.fromAxisAndDirection(
            Direction$Axis.Y, delta.getY() < 0 ? Direction$AxisDirection.NEGATIVE : Direction$AxisDirection.POSITIVE
         );
         Direction directionZ = Direction.fromAxisAndDirection(
            Direction$Axis.Z, delta.getZ() < 0 ? Direction$AxisDirection.NEGATIVE : Direction$AxisDirection.POSITIVE
         );
         if (delta.getX() == 0) {
            return isUnobstructed(level, from, directionY) || isUnobstructed(level, from, directionZ);
         } else {
            return delta.getY() == 0
               ? isUnobstructed(level, from, directionX) || isUnobstructed(level, from, directionZ)
               : isUnobstructed(level, from, directionX) || isUnobstructed(level, from, directionY);
         }
      }
   }

   private static boolean isUnobstructed(final LevelAccessor level, final BlockPos from, final Direction direction) {
      BlockPos testPos = from.relative(direction);
      return !level.getBlockState(testPos).isFaceSturdy(level, testPos, direction.getOpposite());
   }
}
