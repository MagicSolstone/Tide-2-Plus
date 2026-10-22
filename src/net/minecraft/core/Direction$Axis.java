package net.minecraft.core;

import java.util.function.Predicate;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.StringRepresentable$EnumCodec;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;

public enum Direction$Axis implements Predicate<Direction>, StringRepresentable {
   X("x"),
   Y("y"),
   Z("z");

   public static final Direction$Axis[] VALUES = values();
   public static final StringRepresentable$EnumCodec<Direction$Axis> CODEC = StringRepresentable.fromEnum(Direction$Axis::values);
   private final String name;

   private Direction$Axis(final String name) {
      this.name = name;
   }

   @Nullable
   public static Direction$Axis byName(final String name) {
      return CODEC.byName(name);
   }

   public String getName() {
      return this.name;
   }

   public boolean isVertical() {
      return this == Y;
   }

   public boolean isHorizontal() {
      return this == X || this == Z;
   }

   public Direction getPositive() {
      return switch (this) {
         case X -> Direction.EAST;
         case Y -> Direction.UP;
         case Z -> Direction.SOUTH;
      };
   }

   public Direction getNegative() {
      return switch (this) {
         case X -> Direction.WEST;
         case Y -> Direction.DOWN;
         case Z -> Direction.NORTH;
      };
   }

   public Direction[] getDirections() {
      return new Direction[]{this.getPositive(), this.getNegative()};
   }

   @Override
   public String toString() {
      return this.name;
   }

   public static Direction$Axis getRandom(final RandomSource random) {
      return Util.getRandom(VALUES, random);
   }

   public boolean test(@Nullable final Direction input) {
      return input != null && input.getAxis() == this;
   }

   public Direction$Plane getPlane() {
      return switch (this) {
         case X, Z -> Direction$Plane.HORIZONTAL;
         case Y -> Direction$Plane.VERTICAL;
      };
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }

   public int choose(final int x, final int y, final int z) {
      return switch (this) {
         case X -> x;
         case Y -> y;
         case Z -> z;
      };
   }

   public double choose(final double x, final double y, final double z) {
      return switch (this) {
         case X -> x;
         case Y -> y;
         case Z -> z;
      };
   }

   public boolean choose(final boolean x, final boolean y, final boolean z) {
      return switch (this) {
         case X -> x;
         case Y -> y;
         case Z -> z;
      };
   }
}
