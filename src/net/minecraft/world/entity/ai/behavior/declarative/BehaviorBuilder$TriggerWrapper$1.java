package net.minecraft.world.entity.ai.behavior.declarative;

import com.mojang.datafixers.util.Unit;
import java.util.Set;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import org.jspecify.annotations.Nullable;

class BehaviorBuilder$TriggerWrapper$1 implements BehaviorBuilder$TriggerWithResult<E, Unit> {
   BehaviorBuilder$TriggerWrapper$1(final Trigger val$dependentTrigger) {
      this.val$dependentTrigger = val$dependentTriggerx;
   }

   @Nullable
   public Unit tryTrigger(final ServerLevel level, final E body, final long timestamp) {
      return this.val$dependentTrigger.trigger(level, body, timestamp) ? Unit.INSTANCE : null;
   }

   @Override
   public Set<MemoryModuleType<?>> memories() {
      return Set.of();
   }

   @Override
   public String debugString() {
      return "T[" + this.val$dependentTrigger + "]";
   }
}
