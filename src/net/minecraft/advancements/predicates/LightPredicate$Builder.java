package net.minecraft.advancements.predicates;

public class LightPredicate$Builder {
   private MinMaxBounds$Ints composite = MinMaxBounds$Ints.ANY;

   public static LightPredicate$Builder light() {
      return new LightPredicate$Builder();
   }

   public LightPredicate$Builder setComposite(final MinMaxBounds$Ints composite) {
      this.composite = composite;
      return this;
   }

   public LightPredicate build() {
      return new LightPredicate(this.composite);
   }
}
