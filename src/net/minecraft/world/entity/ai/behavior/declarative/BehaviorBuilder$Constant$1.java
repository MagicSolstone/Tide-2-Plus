package net.minecraft.world.entity.ai.behavior.declarative;

import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

class BehaviorBuilder$Constant$1 implements BehaviorBuilder$TriggerWithResult<E, A> {
   BehaviorBuilder$Constant$1(final Object val$a, final Supplier val$debugString) {
      this.val$a = val$ax;
      this.val$debugString = val$debugStringx;
   }

   @Override
   public A tryTrigger(final ServerLevel level, final E body, final long timestamp) {
      return (A)this.val$a;
   }

   @Override
   public Set<MemoryModuleType<?>> memories() {
      return Set.of();
   }

   @Override
   public String debugString() {
      return (String)this.val$debugString.get();
   }

   @Override
   public String toString() {
      return this.debugString();
   }
}
