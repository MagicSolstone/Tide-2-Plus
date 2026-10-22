package net.minecraft.world.entity.ai.behavior.declarative;

import java.util.Set;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

class BehaviorBuilder$1 extends OneShot<E> {
   BehaviorBuilder$1(final BehaviorBuilder$TriggerWithResult val$resolvedBuilder) {
      this.val$resolvedBuilder = val$resolvedBuilderx;
   }

   @Override
   public boolean trigger(final ServerLevel level, final E body, final long timestamp) {
      Trigger<E> trigger = (Trigger<E>)this.val$resolvedBuilder.tryTrigger(level, body, timestamp);
      return trigger == null ? false : trigger.trigger(level, body, timestamp);
   }

   @Override
   public Set<MemoryModuleType<?>> getRequiredMemories() {
      return this.val$resolvedBuilder.memories();
   }

   @Override
   public String debugString() {
      return "OneShot[" + this.val$resolvedBuilder.debugString() + "]";
   }

   @Override
   public String toString() {
      return this.debugString();
   }
}
