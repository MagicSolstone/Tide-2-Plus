package net.minecraft.advancements.predicates;

public class FoodPredicate$Builder {
   private MinMaxBounds$Ints level = MinMaxBounds$Ints.ANY;
   private MinMaxBounds$Doubles saturation = MinMaxBounds$Doubles.ANY;

   public FoodPredicate$Builder withLevel(final MinMaxBounds$Ints level) {
      this.level = level;
      return this;
   }

   public FoodPredicate$Builder withSaturation(final MinMaxBounds$Doubles saturation) {
      this.saturation = saturation;
      return this;
   }

   public static FoodPredicate$Builder food() {
      return new FoodPredicate$Builder();
   }

   public FoodPredicate build() {
      return new FoodPredicate(this.level, this.saturation);
   }
}
