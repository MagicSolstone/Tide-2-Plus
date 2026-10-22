package net.minecraft.world.level.levelgen;

import java.util.OptionalInt;

public final class Column$Range extends Column {
   private final int floor;
   private final int ceiling;

   public Column$Range(final int floor, final int ceiling) {
      this.floor = floor;
      this.ceiling = ceiling;
      if (this.height() < 0) {
         throw new IllegalArgumentException("Column of negative height: " + this);
      }
   }

   @Override
   public OptionalInt getCeiling() {
      return OptionalInt.of(this.ceiling);
   }

   @Override
   public OptionalInt getFloor() {
      return OptionalInt.of(this.floor);
   }

   @Override
   public OptionalInt getHeight() {
      return OptionalInt.of(this.height());
   }

   public int ceiling() {
      return this.ceiling;
   }

   public int floor() {
      return this.floor;
   }

   public int height() {
      return this.ceiling - this.floor - 1;
   }

   @Override
   public String toString() {
      return "C(" + this.ceiling + "-" + this.floor + ")";
   }
}
