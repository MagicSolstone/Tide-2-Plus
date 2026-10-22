package net.minecraft.world.entity.ai.memory;

public record MemoryMap$Value<U>(MemoryModuleType<U> type, ExpirableValue<U> value) {
   public static <U> MemoryMap$Value<U> createUnchecked(final MemoryModuleType<U> type, final ExpirableValue<?> value) {
      return new MemoryMap$Value<>(type, (ExpirableValue<U>)value);
   }
}
