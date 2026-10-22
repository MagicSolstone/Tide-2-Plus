package net.minecraft.world.entity.ai;

import java.util.Objects;
import net.minecraft.world.entity.ai.memory.ExpirableValue;
import net.minecraft.world.entity.ai.memory.MemoryMap$Builder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

class Brain$1 implements Brain$Visitor {
   Brain$1(final Brain this$0, final MemoryMap$Builder val$builder) {
      this.val$builder = val$builderx;
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public <U> void acceptEmpty(final MemoryModuleType<U> type) {
   }

   @Override
   public <U> void accept(final MemoryModuleType<U> type, final U value, final long timeToLive) {
      if (type.canSerialize()) {
         this.val$builder.add(type, ExpirableValue.of(value, timeToLive));
      }
   }

   @Override
   public <U> void accept(final MemoryModuleType<U> type, final U value) {
      if (type.canSerialize()) {
         this.val$builder.add(type, ExpirableValue.of(value));
      }
   }
}
