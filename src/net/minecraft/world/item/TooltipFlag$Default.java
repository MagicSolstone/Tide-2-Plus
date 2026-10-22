package net.minecraft.world.item;

public record TooltipFlag$Default(boolean advanced, boolean creative) implements TooltipFlag {
   @Override
   public boolean isAdvanced() {
      return this.advanced;
   }

   @Override
   public boolean isCreative() {
      return this.creative;
   }

   public TooltipFlag$Default asCreative() {
      return new TooltipFlag$Default(this.advanced, true);
   }
}
