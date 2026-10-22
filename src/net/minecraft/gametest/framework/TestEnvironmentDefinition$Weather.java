package net.minecraft.gametest.framework;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;

public record TestEnvironmentDefinition$Weather(TestEnvironmentDefinition$Weather$Type weather)
   implements TestEnvironmentDefinition<TestEnvironmentDefinition$Weather$Type> {
   public static final MapCodec<TestEnvironmentDefinition$Weather> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(TestEnvironmentDefinition$Weather$Type.CODEC.fieldOf("weather").forGetter(TestEnvironmentDefinition$Weather::weather))
         .apply(i, TestEnvironmentDefinition$Weather::new)
   );

   public TestEnvironmentDefinition$Weather$Type setup(final ServerLevel level) {
      TestEnvironmentDefinition$Weather$Type previous;
      if (level.isThundering()) {
         previous = TestEnvironmentDefinition$Weather$Type.THUNDER;
      } else if (level.isRaining()) {
         previous = TestEnvironmentDefinition$Weather$Type.RAIN;
      } else {
         previous = TestEnvironmentDefinition$Weather$Type.CLEAR;
      }

      this.weather.apply(level);
      return previous;
   }

   public void teardown(final ServerLevel level, final TestEnvironmentDefinition$Weather$Type saveData) {
      level.resetWeatherCycle();
      saveData.apply(level);
   }

   @Override
   public MapCodec<TestEnvironmentDefinition$Weather> codec() {
      return CODEC;
   }
}
