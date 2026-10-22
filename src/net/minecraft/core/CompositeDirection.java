package net.minecraft.core;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

public record CompositeDirection(Set<Direction> directions, Vec3i step) implements Directional {
   public static final CompositeDirection NORTH = new CompositeDirection(Direction.NORTH);
   public static final CompositeDirection EAST = new CompositeDirection(Direction.EAST);
   public static final CompositeDirection SOUTH = new CompositeDirection(Direction.SOUTH);
   public static final CompositeDirection WEST = new CompositeDirection(Direction.WEST);
   public static final CompositeDirection NORTH_EAST = NORTH.compose(EAST);
   public static final CompositeDirection SOUTH_EAST = SOUTH.compose(EAST);
   public static final CompositeDirection SOUTH_WEST = SOUTH.compose(WEST);
   public static final CompositeDirection NORTH_WEST = NORTH.compose(WEST);
   public static final CompositeDirection UP = new CompositeDirection(Direction.UP);
   public static final CompositeDirection NORTH_UP = NORTH.compose(UP);
   public static final CompositeDirection EAST_UP = EAST.compose(UP);
   public static final CompositeDirection SOUTH_UP = SOUTH.compose(UP);
   public static final CompositeDirection WEST_UP = WEST.compose(UP);
   public static final CompositeDirection NORTH_EAST_UP = NORTH_EAST.compose(UP);
   public static final CompositeDirection SOUTH_EAST_UP = SOUTH_EAST.compose(UP);
   public static final CompositeDirection SOUTH_WEST_UP = SOUTH_WEST.compose(UP);
   public static final CompositeDirection NORTH_WEST_UP = NORTH_WEST.compose(UP);
   public static final CompositeDirection DOWN = new CompositeDirection(Direction.DOWN);
   public static final CompositeDirection NORTH_DOWN = NORTH.compose(DOWN);
   public static final CompositeDirection EAST_DOWN = EAST.compose(DOWN);
   public static final CompositeDirection SOUTH_DOWN = SOUTH.compose(DOWN);
   public static final CompositeDirection WEST_DOWN = WEST.compose(DOWN);
   public static final CompositeDirection NORTH_EAST_DOWN = NORTH_EAST.compose(DOWN);
   public static final CompositeDirection SOUTH_EAST_DOWN = SOUTH_EAST.compose(DOWN);
   public static final CompositeDirection SOUTH_WEST_DOWN = SOUTH_WEST.compose(DOWN);
   public static final CompositeDirection NORTH_WEST_DOWN = NORTH_WEST.compose(DOWN);

   public CompositeDirection(Set<Direction> directions, Vec3i step) {
      if (directions.isEmpty()) {
         throw new IllegalArgumentException("Directions cannot be empty");
      } else {
         this.directions = directions;
         this.step = step;
      }
   }

   @VisibleForTesting
   CompositeDirection(final Direction... directions) {
      Set<Direction> immutableDirections = Sets.immutableEnumSet(Arrays.asList(directions));
      int x = 0;
      int y = 0;
      int z = 0;

      for (Direction direction : immutableDirections) {
         x += direction.getStepX();
         y += direction.getStepY();
         z += direction.getStepZ();
      }

      this(immutableDirections, new Vec3i(x, y, z));
   }

   public CompositeDirection compose(final CompositeDirection other) {
      Set<Direction> newDirections = EnumSet.copyOf(this.directions);
      newDirections.addAll(other.directions);
      Vec3i step = new Vec3i(this.getStepX(), this.getStepY(), this.getStepZ());
      Vec3i additionalStep = other.getStep();
      step.setX(step.getX() + additionalStep.getX()).setY(step.getY() + additionalStep.getY()).setZ(step.getZ() + additionalStep.getZ());
      return new CompositeDirection(Sets.immutableEnumSet(newDirections), step);
   }

   @Override
   public int getStepX() {
      return this.step.getX();
   }

   @Override
   public int getStepY() {
      return this.step.getY();
   }

   @Override
   public int getStepZ() {
      return this.step.getZ();
   }

   @Override
   public Vec3i getStep() {
      return this.step;
   }
}
