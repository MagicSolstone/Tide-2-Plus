package net.minecraft.world.level.block;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Continuation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.mutable.MutableInt;

public interface ChangeOverTimeBlock<T extends Enum<T>> {
   int SCAN_DISTANCE = 4;

   Optional<BlockState> getNext(BlockState state);

   float getChanceModifier();

   default void changeOverTime(final BlockState state, final ServerLevel level, final BlockPos pos, final RandomSource random) {
      float eachBlockOncePerDayChance = 0.05688889F;
      if (random.nextFloat() < 0.05688889F) {
         this.getNextState(state, level, pos, random).ifPresent(weatheredState -> level.setBlockAndUpdate(pos, weatheredState));
      }
   }

   T getAge();

   default Optional<BlockState> getNextState(final BlockState state, final ServerLevel level, final BlockPos pos, final RandomSource random) {
      int ownAge = this.getAge().ordinal();
      MutableInt sameAgeCount = new MutableInt(0);
      MutableInt olderCount = new MutableInt(0);
      boolean aborted = level.findBlocksInManhattan(pos, 4)
         .filterPos(blockPos -> !blockPos.equals(pos))
         .filterState(this::isSameAgeingType)
         .forEachUntil((blockPos, var5x) -> {
            if (level.getBlockState(blockPos).getBlock() instanceof ChangeOverTimeBlock<?> neighborBlock) {
               int foundAge = neighborBlock.getAge().ordinal();
               if (foundAge < ownAge) {
                  return Continuation.ABORT;
               }

               if (foundAge > ownAge) {
                  olderCount.increment();
               } else {
                  sameAgeCount.increment();
               }
            }

            return Continuation.CONTINUE;
         });
      if (aborted) {
         return Optional.empty();
      } else {
         float chance = (float)(olderCount.intValue() + 1) / (olderCount.intValue() + sameAgeCount.intValue() + 1);
         float actualChance = chance * chance * this.getChanceModifier();
         return random.nextFloat() < actualChance ? this.getNext(state) : Optional.empty();
      }
   }

   private boolean isSameAgeingType(final BlockState state) {
      return state.getBlock() instanceof ChangeOverTimeBlock<?> neighborBlock && this.getAge().getClass() == neighborBlock.getAge().getClass();
   }
}
