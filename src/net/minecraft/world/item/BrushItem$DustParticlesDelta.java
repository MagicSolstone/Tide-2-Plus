package net.minecraft.world.item;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

record BrushItem$DustParticlesDelta(double xd, double yd, double zd) {
   private static final double ALONG_SIDE_DELTA = 1.0;
   private static final double OUT_FROM_SIDE_DELTA = 0.1;

   public static BrushItem$DustParticlesDelta fromDirection(final Vec3 viewVector, final Direction hitDirection) {
      double yd = 0.0;

      return switch (hitDirection) {
         case DOWN, UP -> new BrushItem$DustParticlesDelta(viewVector.z(), 0.0, -viewVector.x());
         case NORTH -> new BrushItem$DustParticlesDelta(1.0, 0.0, -0.1);
         case SOUTH -> new BrushItem$DustParticlesDelta(-1.0, 0.0, 0.1);
         case WEST -> new BrushItem$DustParticlesDelta(-0.1, 0.0, -1.0);
         case EAST -> new BrushItem$DustParticlesDelta(0.1, 0.0, 1.0);
      };
   }
}
