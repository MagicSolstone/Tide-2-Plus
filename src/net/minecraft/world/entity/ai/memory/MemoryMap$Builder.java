package net.minecraft.world.entity.ai.memory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public class MemoryMap$Builder {
   private final Builder<MemoryModuleType<?>, ExpirableValue<?>> builder = ImmutableMap.builder();

   public <U> MemoryMap$Builder add(final MemoryModuleType<U> type, final ExpirableValue<U> value) {
      this.builder.put(type, value);
      return this;
   }

   public MemoryMap build() {
      return new MemoryMap(this.builder.buildOrThrow());
   }
}
