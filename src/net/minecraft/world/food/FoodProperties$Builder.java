package net.minecraft.world.food;

public class FoodProperties$Builder {
   private int nutrition;
   private float saturationModifier;
   private boolean canAlwaysEat;

   public FoodProperties$Builder nutrition(final int nutrition) {
      this.nutrition = nutrition;
      return this;
   }

   public FoodProperties$Builder saturationModifier(final float saturationModifier) {
      this.saturationModifier = saturationModifier;
      return this;
   }

   public FoodProperties$Builder alwaysEdible() {
      this.canAlwaysEat = true;
      return this;
   }

   public FoodProperties build() {
      float saturation = FoodConstants.saturationByModifier(this.nutrition, this.saturationModifier);
      return new FoodProperties(this.nutrition, saturation, this.canAlwaysEat);
   }
}
