package net.minecraft.core;

import java.util.Set;

public enum CompositeDirection$Direction8 implements Directional {
   NORTH(CompositeDirection.NORTH),
   NORTH_EAST(CompositeDirection.NORTH_EAST),
   EAST(CompositeDirection.EAST),
   SOUTH_EAST(CompositeDirection.SOUTH_EAST),
   SOUTH(CompositeDirection.SOUTH),
   SOUTH_WEST(CompositeDirection.SOUTH_WEST),
   WEST(CompositeDirection.WEST),
   NORTH_WEST(CompositeDirection.NORTH_WEST);

   private final CompositeDirection compositeDirection;

   private CompositeDirection$Direction8(final CompositeDirection compositeDirection) {
      this.compositeDirection = compositeDirection;
   }

   public Set<Direction> getDirections() {
      return this.compositeDirection.directions();
   }

   @Override
   public int getStepX() {
      return this.compositeDirection.getStepX();
   }

   @Override
   public int getStepY() {
      return this.compositeDirection.getStepY();
   }

   @Override
   public int getStepZ() {
      return this.compositeDirection.getStepZ();
   }

   @Override
   public Vec3i getStep() {
      return this.compositeDirection.getStep();
   }
}
