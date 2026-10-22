package net.minecraft.world.entity.ai.behavior.declarative;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

class BehaviorBuilder$Instance$1 implements BehaviorBuilder$TriggerWithResult<E, R> {
   BehaviorBuilder$Instance$1(
      final BehaviorBuilder$Instance this$0, final BehaviorBuilder$TriggerWithResult val$aTrigger, final BehaviorBuilder$TriggerWithResult val$fTrigger
   ) {
      this.val$aTrigger = val$aTriggerx;
      this.val$fTrigger = val$fTriggerx;
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public R tryTrigger(final ServerLevel level, final E body, final long timestamp) {
      A ra = (A)this.val$aTrigger.tryTrigger(level, body, timestamp);
      if (ra == null) {
         return null;
      } else {
         Function<A, R> rf = (Function<A, R>)this.val$fTrigger.tryTrigger(level, body, timestamp);
         return (R)(rf == null ? null : rf.apply(ra));
      }
   }

   @Override
   public Set<MemoryModuleType<?>> memories() {
      Set<MemoryModuleType<?>> memories = new HashSet<>();
      memories.addAll(this.val$aTrigger.memories());
      memories.addAll(this.val$fTrigger.memories());
      return memories;
   }

   @Override
   public String debugString() {
      return this.val$fTrigger.debugString() + " * " + this.val$aTrigger.debugString();
   }

   @Override
   public String toString() {
      return this.debugString();
   }
}
