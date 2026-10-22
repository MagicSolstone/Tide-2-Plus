package net.minecraft.world.level.redstone;

public enum Orientation$SideBias {
   LEFT("left"),
   RIGHT("right");

   private final String name;

   private Orientation$SideBias(final String name) {
      this.name = name;
   }

   public Orientation$SideBias getOpposite() {
      return this == LEFT ? RIGHT : LEFT;
   }

   @Override
   public String toString() {
      return this.name;
   }
}
