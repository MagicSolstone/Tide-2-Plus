package net.minecraft.world.level.levelgen;

import java.util.OptionalInt;

public final class Column$Line extends Column {
   private static final Column$Line INSTANCE = new Column$Line();

   private Column$Line() {
   }

   @Override
   public OptionalInt getCeiling() {
      return OptionalInt.empty();
   }

   @Override
   public OptionalInt getFloor() {
      return OptionalInt.empty();
   }

   @Override
   public OptionalInt getHeight() {
      return OptionalInt.empty();
   }

   @Override
   public String toString() {
      return "C(-)";
   }
}
