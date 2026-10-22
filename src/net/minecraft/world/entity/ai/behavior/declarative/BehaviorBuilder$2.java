package net.minecraft.world.entity.ai.behavior.declarative;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

class BehaviorBuilder$2 extends OneShot<E> {
   BehaviorBuilder$2(final OneShot val$wrapped, final OneShot val$second) {
      this.val$wrapped = val$wrappedx;
      this.val$second = val$secondx;
   }

   @Override
   public boolean trigger(final ServerLevel level, final E body, final long timestamp) {
      return this.val$wrapped.trigger(level, body, timestamp);
   }

   @Override
   public Set<MemoryModuleType<?>> getRequiredMemories() {
      Set<MemoryModuleType<?>> memories = new HashSet<>();
      memories.addAll(this.val$wrapped.getRequiredMemories());
      memories.addAll(this.val$second.getRequiredMemories());
      return memories;
   }

   @Override
   public String debugString() {
      return "OneShot[stuff]";
   }

   @Override
   public String toString() {
      return this.debugString();
   }
}
