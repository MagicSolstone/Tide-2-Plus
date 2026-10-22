package net.minecraft.gametest.framework;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.server.level.ServerLevel;

public interface TestEnvironmentDefinition<SavedDataType> {
   Codec<TestEnvironmentDefinition<?>> DIRECT_CODEC = BuiltInRegistries.TEST_ENVIRONMENT_DEFINITION_TYPE
      .byNameCodec()
      .dispatch(TestEnvironmentDefinition::codec, c -> c);
   Codec<Holder<TestEnvironmentDefinition<?>>> CODEC = RegistryCodecs.holder(Registries.TEST_ENVIRONMENT, DIRECT_CODEC);

   static MapCodec<? extends TestEnvironmentDefinition<?>> bootstrap(final Registry<MapCodec<? extends TestEnvironmentDefinition<?>>> registry) {
      Registry.register(registry, "all_of", TestEnvironmentDefinition$AllOf.CODEC);
      Registry.register(registry, "clock_time", TestEnvironmentDefinition$ClockTime.CODEC);
      Registry.register(registry, "difficulty", TestEnvironmentDefinition$SetDifficulty.CODEC);
      Registry.register(registry, "function", TestEnvironmentDefinition$Functions.CODEC);
      Registry.register(registry, "game_rules", TestEnvironmentDefinition$SetGameRules.CODEC);
      Registry.register(registry, "timeline_attributes", TestEnvironmentDefinition$Timelines.CODEC);
      return Registry.register(registry, "weather", TestEnvironmentDefinition$Weather.CODEC);
   }

   SavedDataType setup(ServerLevel level);

   void teardown(final ServerLevel level, final SavedDataType saveData);

   MapCodec<? extends TestEnvironmentDefinition<SavedDataType>> codec();

   static <T> TestEnvironmentDefinition$Activation<T> activate(final TestEnvironmentDefinition<T> environment, final ServerLevel level) {
      return new TestEnvironmentDefinition$Activation<>(environment.setup(level), environment, level);
   }
}
