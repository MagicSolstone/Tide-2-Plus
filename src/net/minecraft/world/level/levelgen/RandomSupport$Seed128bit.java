package net.minecraft.world.level.levelgen;

public record RandomSupport$Seed128bit(long seedLo, long seedHi) {
   public RandomSupport$Seed128bit xor(final long lo, final long hi) {
      return new RandomSupport$Seed128bit(this.seedLo ^ lo, this.seedHi ^ hi);
   }

   public RandomSupport$Seed128bit xor(final RandomSupport$Seed128bit other) {
      return this.xor(other.seedLo, other.seedHi);
   }

   public RandomSupport$Seed128bit mixed() {
      return new RandomSupport$Seed128bit(RandomSupport.mixStafford13(this.seedLo), RandomSupport.mixStafford13(this.seedHi));
   }
}
