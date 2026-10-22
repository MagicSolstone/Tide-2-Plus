package net.minecraft.world.level.block;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public record WeatheringCopperCollection$ByState<T>(T unaffected, T exposed, T weathered, T oxidized) {
   public static <T> WeatheringCopperCollection$ByState<T> create(final T value) {
      return new WeatheringCopperCollection$ByState<>(value, value, value, value);
   }

   public <U> WeatheringCopperCollection$ByState<U> map(final Function<T, U> mapper) {
      return new WeatheringCopperCollection$ByState<>(
         mapper.apply(this.unaffected), mapper.apply(this.exposed), mapper.apply(this.weathered), mapper.apply(this.oxidized)
      );
   }

   public T pick(final WeatheringCopper$WeatherState state) {
      return (T)(switch (state) {
         case UNAFFECTED -> this.unaffected;
         case EXPOSED -> this.exposed;
         case WEATHERED -> this.weathered;
         case OXIDIZED -> this.oxidized;
      });
   }

   public void forEach(final Consumer<T> consumer) {
      consumer.accept(this.unaffected);
      consumer.accept(this.exposed);
      consumer.accept(this.weathered);
      consumer.accept(this.oxidized);
   }

   public void progressMapping(final BiConsumer<T, T> consumer) {
      consumer.accept(this.unaffected, this.exposed);
      consumer.accept(this.exposed, this.weathered);
      consumer.accept(this.weathered, this.oxidized);
   }
}
