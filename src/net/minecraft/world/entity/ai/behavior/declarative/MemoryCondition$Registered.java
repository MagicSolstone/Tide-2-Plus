package net.minecraft.world.entity.ai.behavior.declarative;

import com.mojang.datafixers.kinds.OptionalBox;
import com.mojang.datafixers.kinds.OptionalBox.Mu;
import java.util.Optional;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public record MemoryCondition$Registered<Value>(MemoryModuleType<Value> memory) implements MemoryCondition<Mu, Value> {
   @Override
   public MemoryStatus condition() {
      return MemoryStatus.REGISTERED;
   }

   @Override
   public MemoryAccessor<Mu, Value> createAccessor(final Brain<?> brain, final Optional<Value> value) {
      return new MemoryAccessor<>(brain, this.memory, OptionalBox.create(value));
   }
}
