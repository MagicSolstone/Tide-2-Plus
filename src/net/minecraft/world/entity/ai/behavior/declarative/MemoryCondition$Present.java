package net.minecraft.world.entity.ai.behavior.declarative;

import com.mojang.datafixers.kinds.IdF;
import com.mojang.datafixers.kinds.IdF.Mu;
import java.util.Optional;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public record MemoryCondition$Present<Value>(MemoryModuleType<Value> memory) implements MemoryCondition<Mu, Value> {
   @Override
   public MemoryStatus condition() {
      return MemoryStatus.VALUE_PRESENT;
   }

   @Override
   public MemoryAccessor<Mu, Value> createAccessor(final Brain<?> brain, final Optional<Value> value) {
      return value.isEmpty() ? null : new MemoryAccessor<>(brain, this.memory, IdF.create(value.get()));
   }
}
