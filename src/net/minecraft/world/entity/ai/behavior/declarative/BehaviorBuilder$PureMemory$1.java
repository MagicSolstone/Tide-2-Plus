package net.minecraft.world.entity.ai.behavior.declarative;

import java.util.Optional;
import java.util.Set;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import org.jspecify.annotations.Nullable;

class BehaviorBuilder$PureMemory$1 implements BehaviorBuilder$TriggerWithResult<E, MemoryAccessor<F, Value>> {
   BehaviorBuilder$PureMemory$1(final MemoryCondition val$condition) {
      this.val$condition = val$conditionx;
   }

   @Nullable
   public MemoryAccessor<F, Value> tryTrigger(final ServerLevel level, final E body, final long timestamp) {
      Brain<?> brain = body.getBrain();
      Optional<Value> value = brain.getMemoryInternal(this.val$condition.memory());
      return value == null ? null : this.val$condition.createAccessor(brain, value);
   }

   @Override
   public Set<MemoryModuleType<?>> memories() {
      return Set.of(this.val$condition.memory());
   }

   @Override
   public String debugString() {
      return "M[" + this.val$condition + "]";
   }

   @Override
   public String toString() {
      return this.debugString();
   }
}
