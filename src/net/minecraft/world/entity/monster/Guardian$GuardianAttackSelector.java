package net.minecraft.world.entity.monster;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions$Selector;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.squid.Squid;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

class Guardian$GuardianAttackSelector implements TargetingConditions$Selector {
   private final Guardian guardian;

   public Guardian$GuardianAttackSelector(final Guardian guardian) {
      this.guardian = guardian;
   }

   @Override
   public boolean test(@Nullable final LivingEntity target, final ServerLevel level) {
      return (target instanceof Player || target instanceof Squid || target instanceof Axolotl) && target.distanceToSqr(this.guardian) > 9.0;
   }
}
