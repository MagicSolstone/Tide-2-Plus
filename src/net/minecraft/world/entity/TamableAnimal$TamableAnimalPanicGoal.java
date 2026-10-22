package net.minecraft.world.entity;

import java.util.Objects;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.ai.goal.PanicGoal;

public class TamableAnimal$TamableAnimalPanicGoal extends PanicGoal {
   public TamableAnimal$TamableAnimalPanicGoal(final double this$0, final TagKey<DamageType> speedModifier, final TagKey panicCausingDamageTypes) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(this$0, speedModifier, panicCausingDamageTypes);
   }

   public TamableAnimal$TamableAnimalPanicGoal(final TamableAnimal this$0, final double speedModifier) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(this$0, speedModifier);
   }

   @Override
   public void tick() {
      if (!this.this$0.unableToMoveToOwner() && this.this$0.shouldTryTeleportToOwner()) {
         this.this$0.tryToTeleportToOwner();
      }

      super.tick();
   }
}
