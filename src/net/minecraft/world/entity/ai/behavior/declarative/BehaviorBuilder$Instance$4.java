package net.minecraft.world.entity.ai.behavior.declarative;

import com.mojang.datafixers.util.Function3;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

class BehaviorBuilder$Instance$4 implements BehaviorBuilder$TriggerWithResult<E, R> {
   BehaviorBuilder$Instance$4(
      final BehaviorBuilder$Instance this$0,
      final BehaviorBuilder$TriggerWithResult val$t1Trigger,
      final BehaviorBuilder$TriggerWithResult val$t2Trigger,
      final BehaviorBuilder$TriggerWithResult val$t3Trigger,
      final BehaviorBuilder$TriggerWithResult val$fTrigger
   ) {
      this.val$t1Trigger = val$t1Triggerx;
      this.val$t2Trigger = val$t2Triggerx;
      this.val$t3Trigger = val$t3Triggerx;
      this.val$fTrigger = val$fTriggerx;
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public R tryTrigger(final ServerLevel level, final E body, final long timestamp) {
      T1 r1 = (T1)this.val$t1Trigger.tryTrigger(level, body, timestamp);
      if (r1 == null) {
         return null;
      } else {
         T2 r2 = (T2)this.val$t2Trigger.tryTrigger(level, body, timestamp);
         if (r2 == null) {
            return null;
         } else {
            T3 r3 = (T3)this.val$t3Trigger.tryTrigger(level, body, timestamp);
            if (r3 == null) {
               return null;
            } else {
               Function3<T1, T2, T3, R> rf = (Function3<T1, T2, T3, R>)this.val$fTrigger.tryTrigger(level, body, timestamp);
               return (R)(rf == null ? null : rf.apply(r1, r2, r3));
            }
         }
      }
   }

   @Override
   public Set<MemoryModuleType<?>> memories() {
      Set<MemoryModuleType<?>> memories = new HashSet<>();
      memories.addAll(this.val$t1Trigger.memories());
      memories.addAll(this.val$t2Trigger.memories());
      memories.addAll(this.val$t3Trigger.memories());
      memories.addAll(this.val$fTrigger.memories());
      return memories;
   }

   @Override
   public String debugString() {
      return this.val$fTrigger.debugString()
         + " * "
         + this.val$t1Trigger.debugString()
         + " * "
         + this.val$t2Trigger.debugString()
         + " * "
         + this.val$t3Trigger.debugString();
   }

   @Override
   public String toString() {
      return this.debugString();
   }
}
