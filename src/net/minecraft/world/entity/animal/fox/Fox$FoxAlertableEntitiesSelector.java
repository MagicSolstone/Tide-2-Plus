package net.minecraft.world.entity.animal.fox;

import java.util.Objects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions$Selector;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.rabbit.Rabbit;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;

public class Fox$FoxAlertableEntitiesSelector implements TargetingConditions$Selector {
   public Fox$FoxAlertableEntitiesSelector(final Fox this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public boolean test(final LivingEntity target, final ServerLevel level) {
      if (target instanceof Fox) {
         return false;
      } else if (target instanceof Chicken || target instanceof Rabbit || target instanceof Monster) {
         return true;
      } else if (target instanceof TamableAnimal tamableAnimal) {
         return !tamableAnimal.isTame();
      } else if (target instanceof Player player && (player.isSpectator() || player.isCreative())) {
         return false;
      } else {
         return this.this$0.trusts(target) ? false : !target.isSleeping() && !target.isDiscrete();
      }
   }
}
