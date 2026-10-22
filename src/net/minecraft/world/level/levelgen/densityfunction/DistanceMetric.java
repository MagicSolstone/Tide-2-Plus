package net.minecraft.world.level.levelgen.densityfunction;

import com.mojang.serialization.Codec;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;

public enum DistanceMetric implements StringRepresentable {
   EUCLIDEAN("euclidean"),
   EUCLIDEAN_SQUARED("euclidean_squared"),
   MANHATTAN("manhattan"),
   CHEBYSHEV("chebyshev");

   public static final Codec<DistanceMetric> CODEC = StringRepresentable.fromEnum(DistanceMetric::values);
   private final String name;

   private DistanceMetric(final String name) {
      this.name = name;
   }

   public float compute(final float deltaX, final float deltaY, final float deltaZ) {
      return switch (this) {
         case EUCLIDEAN -> Mth.length(deltaX, deltaY, deltaZ);
         case EUCLIDEAN_SQUARED -> Mth.lengthSquared(deltaX, deltaY, deltaZ);
         case MANHATTAN -> Math.abs(deltaX) + Math.abs(deltaY) + Math.abs(deltaZ);
         case CHEBYSHEV -> Math.max(Math.max(Math.abs(deltaX), Math.abs(deltaY)), Math.abs(deltaZ));
      };
   }

   public float compute(final float deltaX, final float deltaZ) {
      return switch (this) {
         case EUCLIDEAN -> Mth.length(deltaX, deltaZ);
         case EUCLIDEAN_SQUARED -> Mth.lengthSquared(deltaX, deltaZ);
         case MANHATTAN -> Math.abs(deltaX) + Math.abs(deltaZ);
         case CHEBYSHEV -> Math.max(Math.abs(deltaX), Math.abs(deltaZ));
      };
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
