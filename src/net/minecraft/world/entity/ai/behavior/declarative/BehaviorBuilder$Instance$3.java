package net.minecraft.world.entity.ai.behavior.declarative;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

class BehaviorBuilder$Instance$3 implements BehaviorBuilder$TriggerWithResult<E, R> {
   BehaviorBuilder$Instance$3(
      final BehaviorBuilder$Instance this$0,
      final BehaviorBuilder$TriggerWithResult val$aTrigger,
      final BehaviorBuilder$TriggerWithResult val$bTrigger,
      final BehaviorBuilder$TriggerWithResult val$fTrigger
   ) {
      this.val$aTrigger = val$aTriggerx;
      this.val$bTrigger = val$bTriggerx;
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
         B rb = (B)this.val$bTrigger.tryTrigger(level, body, timestamp);
         if (rb == null) {
            return null;
         } else {
            BiFunction<A, B, R> fr = (BiFunction<A, B, R>)this.val$fTrigger.tryTrigger(level, body, timestamp);
            return (R)(fr == null ? null : fr.apply(ra, rb));
         }
      }
   }

   @Override
   public Set<MemoryModuleType<?>> memories() {
      Set<MemoryModuleType<?>> memories = new HashSet<>();
      memories.addAll(this.val$aTrigger.memories());
      memories.addAll(this.val$bTrigger.memories());
      memories.addAll(this.val$fTrigger.memories());
      return memories;
   }

   @Override
   public String debugString() {
      return this.val$fTrigger.debugString() + " * " + this.val$aTrigger.debugString() + " * " + this.val$bTrigger.debugString();
   }

   @Override
   public String toString() {
      return this.debugString();
   }
}
