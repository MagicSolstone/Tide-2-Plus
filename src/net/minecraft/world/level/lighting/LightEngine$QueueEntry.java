package net.minecraft.world.level.lighting;

import net.minecraft.core.Direction;

public class LightEngine$QueueEntry {
   private static final int FROM_LEVEL_BITS = 4;
   private static final int DIRECTION_BITS = 6;
   private static final long LEVEL_MASK = 15L;
   private static final long DIRECTIONS_MASK = 1008L;
   private static final long FLAG_FROM_EMPTY_SHAPE = 1024L;
   private static final long FLAG_INCREASE_FROM_EMISSION = 2048L;

   public static long decreaseSkipOneDirection(final int oldFromLevel, final Direction skipDirection) {
      long decreaseData = withoutDirection(1008L, skipDirection);
      return withLevel(decreaseData, oldFromLevel);
   }

   public static long decreaseAllDirections(final int oldFromLevel) {
      return withLevel(1008L, oldFromLevel);
   }

   public static long increaseLightFromEmission(final int newFromLevel, final boolean fromEmptyShape) {
      long increaseData = 1008L;
      increaseData |= 2048L;
      if (fromEmptyShape) {
         increaseData |= 1024L;
      }

      return withLevel(increaseData, newFromLevel);
   }

   public static long increaseSkipOneDirection(final int newFromLevel, final boolean fromEmptyShape, final Direction skipDirection) {
      long increaseData = withoutDirection(1008L, skipDirection);
      if (fromEmptyShape) {
         increaseData |= 1024L;
      }

      return withLevel(increaseData, newFromLevel);
   }

   public static long increaseOnlyOneDirection(final int newFromLevel, final boolean fromEmptyShape, final Direction direction) {
      long increaseData = 0L;
      if (fromEmptyShape) {
         increaseData |= 1024L;
      }

      increaseData = withDirection(increaseData, direction);
      return withLevel(increaseData, newFromLevel);
   }

   public static long increaseSkySourceInDirections(final boolean down, final boolean north, final boolean south, final boolean west, final boolean east) {
      long increaseData = withLevel(0L, 15);
      if (down) {
         increaseData = withDirection(increaseData, Direction.DOWN);
      }

      if (north) {
         increaseData = withDirection(increaseData, Direction.NORTH);
      }

      if (south) {
         increaseData = withDirection(increaseData, Direction.SOUTH);
      }

      if (west) {
         increaseData = withDirection(increaseData, Direction.WEST);
      }

      if (east) {
         increaseData = withDirection(increaseData, Direction.EAST);
      }

      return increaseData;
   }

   public static int getFromLevel(final long entry) {
      return (int)(entry & 15L);
   }

   public static boolean isFromEmptyShape(final long entry) {
      return (entry & 1024L) != 0L;
   }

   public static boolean isIncreaseFromEmission(final long entry) {
      return (entry & 2048L) != 0L;
   }

   public static boolean shouldPropagateInDirection(final long entry, final Direction direction) {
      return (entry & 1L << direction.ordinal() + 4) != 0L;
   }

   private static long withLevel(final long entry, final int level) {
      return entry & -16L | level & 15L;
   }

   private static long withDirection(final long entry, final Direction direction) {
      return entry | 1L << direction.ordinal() + 4;
   }

   private static long withoutDirection(final long entry, final Direction direction) {
      return entry & ~(1L << direction.ordinal() + 4);
   }
}
