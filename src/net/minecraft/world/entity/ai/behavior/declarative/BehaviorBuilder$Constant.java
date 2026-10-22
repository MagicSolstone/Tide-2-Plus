package net.minecraft.world.entity.ai.behavior.declarative;

import java.util.function.Supplier;
import net.minecraft.world.entity.LivingEntity;

final class BehaviorBuilder$Constant<E extends LivingEntity, A> extends BehaviorBuilder<E, A> {
   private BehaviorBuilder$Constant(final A a) {
      this(a, () -> "C[" + a + "]");
   }

   private BehaviorBuilder$Constant(final A a, final Supplier<String> debugString) {
      super(new BehaviorBuilder$Constant$1(a, debugString));
   }
}
