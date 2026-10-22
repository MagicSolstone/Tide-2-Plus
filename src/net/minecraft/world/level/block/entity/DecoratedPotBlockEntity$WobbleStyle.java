package net.minecraft.world.level.block.entity;

public enum DecoratedPotBlockEntity$WobbleStyle {
   POSITIVE(7),
   NEGATIVE(10);

   public final int duration;

   private DecoratedPotBlockEntity$WobbleStyle(final int duration) {
      this.duration = duration;
   }
}
