package net.minecraft.world.entity.ai.behavior.declarative;

import com.mojang.datafixers.kinds.Const;
import com.mojang.datafixers.kinds.Const.Mu;
import com.mojang.datafixers.util.Unit;
import java.util.Optional;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public record MemoryCondition$Absent<Value>(MemoryModuleType<Value> memory) implements MemoryCondition<Mu<Unit>, Value> {
   @Override
   public MemoryStatus condition() {
      return MemoryStatus.VALUE_ABSENT;
   }

   @Override
   public MemoryAccessor<Mu<Unit>, Value> createAccessor(final Brain<?> brain, final Optional<Value> value) {
      return value.isPresent() ? null : new MemoryAccessor<>(brain, this.memory, Const.create(Unit.INSTANCE));
   }
}
