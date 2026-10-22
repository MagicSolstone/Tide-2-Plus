package net.minecraft.world.entity.ai.behavior.declarative;

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

class BehaviorBuilder$Instance$2 implements BehaviorBuilder$TriggerWithResult<E, R> {
   BehaviorBuilder$Instance$2(final BehaviorBuilder$Instance this$0, final BehaviorBuilder$TriggerWithResult val$tTrigger, final Function val$func) {
      this.val$tTrigger = val$tTriggerx;
      this.val$func = val$funcx;
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public R tryTrigger(final ServerLevel level, final E body, final long timestamp) {
      T t = (T)this.val$tTrigger.tryTrigger(level, body, timestamp);
      return (R)(t == null ? null : this.val$func.apply(t));
   }

   @Override
   public Set<MemoryModuleType<?>> memories() {
      return this.val$tTrigger.memories();
   }

   @Override
   public String debugString() {
      return this.val$tTrigger.debugString() + ".map[" + this.val$func + "]";
   }

   @Override
   public String toString() {
      return this.debugString();
   }
}
