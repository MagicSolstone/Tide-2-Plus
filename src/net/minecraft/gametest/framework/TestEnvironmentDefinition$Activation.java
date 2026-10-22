package net.minecraft.gametest.framework;

import net.minecraft.server.level.ServerLevel;

public class TestEnvironmentDefinition$Activation<T> {
   private final T value;
   private final TestEnvironmentDefinition<T> definition;
   private final ServerLevel level;

   private TestEnvironmentDefinition$Activation(final T value, final TestEnvironmentDefinition<T> definition, final ServerLevel level) {
      this.value = value;
      this.definition = definition;
      this.level = level;
   }

   public void teardown() {
      this.definition.teardown(this.level, this.value);
   }
}
