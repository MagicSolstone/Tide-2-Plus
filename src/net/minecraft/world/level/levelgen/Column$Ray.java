package net.minecraft.world.level.levelgen;

import java.util.OptionalInt;

public final class Column$Ray extends Column {
   private final int edge;
   private final boolean pointingUp;

   public Column$Ray(final int edge, final boolean pointingUp) {
      this.edge = edge;
      this.pointingUp = pointingUp;
   }

   @Override
   public OptionalInt getCeiling() {
      return this.pointingUp ? OptionalInt.empty() : OptionalInt.of(this.edge);
   }

   @Override
   public OptionalInt getFloor() {
      return this.pointingUp ? OptionalInt.of(this.edge) : OptionalInt.empty();
   }

   @Override
   public OptionalInt getHeight() {
      return OptionalInt.empty();
   }

   @Override
   public String toString() {
      return this.pointingUp ? "C(" + this.edge + "-)" : "C(-" + this.edge + ")";
   }
}
