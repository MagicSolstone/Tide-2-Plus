package net.minecraft.core;

import com.google.common.collect.Iterators;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;

public enum Direction$Plane implements Predicate<Direction>, Iterable<Direction> {
   HORIZONTAL(new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST}, new Direction$Axis[]{Direction$Axis.X, Direction$Axis.Z}),
   VERTICAL(new Direction[]{Direction.UP, Direction.DOWN}, new Direction$Axis[]{Direction$Axis.Y});

   private final Direction[] faces;
   private final Direction$Axis[] axis;

   private Direction$Plane(final Direction[] faces, final Direction$Axis[] axis) {
      this.faces = faces;
      this.axis = axis;
   }

   public Direction getRandomDirection(final RandomSource random) {
      return Util.getRandom(this.faces, random);
   }

   public Direction$Axis getRandomAxis(final RandomSource random) {
      return Util.getRandom(this.axis, random);
   }

   public boolean test(@Nullable final Direction input) {
      return input != null && input.getAxis().getPlane() == this;
   }

   @Override
   public Iterator<Direction> iterator() {
      return Iterators.forArray(this.faces);
   }

   public Stream<Direction> stream() {
      return Arrays.stream(this.faces);
   }

   public List<Direction> shuffledCopy(final RandomSource random) {
      return Util.shuffledCopy(this.faces, random);
   }

   public int length() {
      return this.faces.length;
   }
}
