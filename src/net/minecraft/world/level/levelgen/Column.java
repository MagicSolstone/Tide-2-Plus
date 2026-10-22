package net.minecraft.world.level.levelgen;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos$MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;

public abstract class Column {
   public static Column$Range around(final int lowest, final int highest) {
      return new Column$Range(lowest - 1, highest + 1);
   }

   public static Column$Range inside(final int floor, final int ceiling) {
      return new Column$Range(floor, ceiling);
   }

   public static Column below(final int ceiling) {
      return new Column$Ray(ceiling, false);
   }

   public static Column fromHighest(final int highest) {
      return new Column$Ray(highest + 1, false);
   }

   public static Column above(final int floor) {
      return new Column$Ray(floor, true);
   }

   public static Column fromLowest(final int lowest) {
      return new Column$Ray(lowest - 1, true);
   }

   public static Column line() {
      return Column$Line.INSTANCE;
   }

   public static Column create(final OptionalInt floor, final OptionalInt ceiling) {
      if (floor.isPresent() && ceiling.isPresent()) {
         return inside(floor.getAsInt(), ceiling.getAsInt());
      } else if (floor.isPresent()) {
         return above(floor.getAsInt());
      } else {
         return ceiling.isPresent() ? below(ceiling.getAsInt()) : line();
      }
   }

   public abstract OptionalInt getCeiling();

   public abstract OptionalInt getFloor();

   public abstract OptionalInt getHeight();

   public Column withFloor(final OptionalInt floor) {
      return create(floor, this.getCeiling());
   }

   public Column withCeiling(final OptionalInt ceiling) {
      return create(this.getFloor(), ceiling);
   }

   public static Optional<Column> scan(
      final LevelSimulatedReader level,
      final BlockPos pos,
      final int searchRange,
      final Predicate<BlockState> insideColumn,
      final Predicate<BlockState> validEdge
   ) {
      BlockPos$MutableBlockPos mutablePos = pos.mutable();
      if (!level.isStateAtPosition(pos, insideColumn)) {
         return Optional.empty();
      } else {
         int nearestEmptyY = pos.getY();
         OptionalInt ceiling = scanDirection(level, searchRange, insideColumn, validEdge, mutablePos, nearestEmptyY, Direction.UP);
         OptionalInt floor = scanDirection(level, searchRange, insideColumn, validEdge, mutablePos, nearestEmptyY, Direction.DOWN);
         return Optional.of(create(floor, ceiling));
      }
   }

   private static OptionalInt scanDirection(
      final LevelSimulatedReader level,
      final int searchRange,
      final Predicate<BlockState> insideColumn,
      final Predicate<BlockState> validEdge,
      final BlockPos$MutableBlockPos mutablePos,
      final int nearestEmptyY,
      final Direction direction
   ) {
      mutablePos.setY(nearestEmptyY);

      for (int i = 1; i < searchRange && level.isStateAtPosition(mutablePos, insideColumn); i++) {
         mutablePos.move(direction);
      }

      return level.isStateAtPosition(mutablePos, validEdge) ? OptionalInt.of(mutablePos.getY()) : OptionalInt.empty();
   }
}
