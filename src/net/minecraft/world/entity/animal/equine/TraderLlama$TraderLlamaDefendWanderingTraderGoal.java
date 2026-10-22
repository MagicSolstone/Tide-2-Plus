package net.minecraft.world.entity.animal.equine;

import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal$Flag;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;

public class TraderLlama$TraderLlamaDefendWanderingTraderGoal extends TargetGoal {
   private final Llama llama;
   private LivingEntity ownerLastHurtBy;
   private int timestamp;

   public TraderLlama$TraderLlamaDefendWanderingTraderGoal(final Llama tameAnimal) {
      super(tameAnimal, false);
      this.llama = tameAnimal;
      this.setFlags(EnumSet.of(Goal$Flag.TARGET));
   }

   @Override
   public boolean canUse() {
      if (!this.llama.isLeashed()) {
         return false;
      } else if (!(this.llama.getLeashHolder() instanceof WanderingTrader owner)) {
         return false;
      } else {
         this.ownerLastHurtBy = owner.getLastHurtByMob();
         int timeStamp = owner.getLastHurtByMobTimestamp();
         return timeStamp != this.timestamp && this.canAttack(this.ownerLastHurtBy, TargetingConditions.DEFAULT);
      }
   }

   @Override
   public void start() {
      this.mob.setTarget(this.ownerLastHurtBy);
      if (this.llama.getLeashHolder() instanceof WanderingTrader wanderingTrader) {
         this.timestamp = wanderingTrader.getLastHurtByMobTimestamp();
      }

      super.start();
   }
}
