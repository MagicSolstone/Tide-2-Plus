package net.minecraft.world.entity.ai.behavior.declarative;

import com.mojang.datafixers.util.Unit;
import net.minecraft.world.entity.LivingEntity;

final class BehaviorBuilder$TriggerWrapper<E extends LivingEntity> extends BehaviorBuilder<E, Unit> {
   private BehaviorBuilder$TriggerWrapper(final Trigger<? super E> dependentTrigger) {
      super(new BehaviorBuilder$TriggerWrapper$1(dependentTrigger));
   }
}
