package net.minecraft.world.entity.ai.behavior.declarative;

import com.mojang.datafixers.kinds.K1;
import net.minecraft.world.entity.LivingEntity;

final class BehaviorBuilder$PureMemory<E extends LivingEntity, F extends K1, Value> extends BehaviorBuilder<E, MemoryAccessor<F, Value>> {
   private BehaviorBuilder$PureMemory(final MemoryCondition<F, Value> condition) {
      super(new BehaviorBuilder$PureMemory$1(condition));
   }
}
