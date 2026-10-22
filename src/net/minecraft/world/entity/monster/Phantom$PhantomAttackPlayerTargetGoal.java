package net.minecraft.world.entity.monster;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

class Phantom$PhantomAttackPlayerTargetGoal extends Goal {
   private final TargetingConditions attackTargeting;
   private int nextScanTick;

   private Phantom$PhantomAttackPlayerTargetGoal(final Phantom this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.attackTargeting = TargetingConditions.forCombat().range(64.0);
      this.nextScanTick = reducedTickDelay(20);
   }

   @Override
   public boolean canUse() {
      if (this.nextScanTick > 0) {
         this.nextScanTick--;
         return false;
      } else {
         this.nextScanTick = reducedTickDelay(60);
         ServerLevel level = getServerLevel(this.this$0.level());
         List<Player> players = level.getNearbyPlayers(this.attackTargeting, this.this$0, this.this$0.getBoundingBox().inflate(16.0, 64.0, 16.0));
         if (!players.isEmpty()) {
            players.sort(Comparator.<Player, Double>comparing(Entity::getY).reversed());

            for (Player player : players) {
               if (this.this$0.canAttack(level, player, TargetingConditions.DEFAULT)) {
                  this.this$0.setTarget(player);
                  return true;
               }
            }
         }

         return false;
      }
   }

   @Override
   public boolean canContinueToUse() {
      LivingEntity target = this.this$0.getTarget();
      return target != null ? this.this$0.canAttack(getServerLevel(this.this$0.level()), target, TargetingConditions.DEFAULT) : false;
   }
}
