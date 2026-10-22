package net.minecraft.world.level;

import net.minecraft.core.Direction;

public record CardinalLighting(float down, float up, float north, float south, float west, float east) {
   public static final CardinalLighting DEFAULT = new CardinalLighting(0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F);
   public static final CardinalLighting NETHER = new CardinalLighting(0.9F, 0.9F, 0.8F, 0.8F, 0.6F, 0.6F);

   public float byFace(final Direction direction) {
      return switch (direction) {
         case DOWN -> this.down;
         case UP -> this.up;
         case NORTH -> this.north;
         case SOUTH -> this.south;
         case WEST -> this.west;
         case EAST -> this.east;
      };
   }
}
