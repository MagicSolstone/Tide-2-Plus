package net.minecraft.world.level.storage.loot.predicates;

import java.util.Optional;

public class WeatherCheck$Builder implements LootItemCondition$Builder {
   private Optional<Boolean> isRaining = Optional.empty();
   private Optional<Boolean> isThundering = Optional.empty();

   public WeatherCheck$Builder setRaining(final boolean raining) {
      this.isRaining = Optional.of(raining);
      return this;
   }

   public WeatherCheck$Builder setThundering(final boolean thundering) {
      this.isThundering = Optional.of(thundering);
      return this;
   }

   public WeatherCheck build() {
      return new WeatherCheck(this.isRaining, this.isThundering);
   }
}
