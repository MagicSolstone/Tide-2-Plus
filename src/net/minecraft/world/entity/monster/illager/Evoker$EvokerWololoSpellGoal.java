package net.minecraft.world.entity.monster.illager;

import java.util.List;
import java.util.Objects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.gamerules.GameRules;

public class Evoker$EvokerWololoSpellGoal extends SpellcasterIllager$SpellcasterUseSpellGoal {
   private final TargetingConditions wololoTargeting;

   public Evoker$EvokerWololoSpellGoal(final Evoker this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(this$0);
      this.wololoTargeting = TargetingConditions.forNonCombat().range(16.0).selector((target, level) -> ((Sheep)target).getColor() == DyeColor.BLUE);
   }

   @Override
   public boolean canUse() {
      if (this.this$0.getTarget() != null) {
         return false;
      } else if (this.this$0.isCastingSpell()) {
         return false;
      } else if (this.this$0.tickCount < this.nextAttackTickCount) {
         return false;
      } else {
         ServerLevel level = getServerLevel(this.this$0.level());
         if (!level.getGameRules().get(GameRules.MOB_GRIEFING)) {
            return false;
         } else {
            List<Sheep> entities = level.getNearbyEntities(
               Sheep.class, this.wololoTargeting, this.this$0, this.this$0.getBoundingBox().inflate(16.0, 4.0, 16.0)
            );
            if (entities.isEmpty()) {
               return false;
            } else {
               this.this$0.setWololoTarget(entities.get(Evoker.access$400(this.this$0).nextInt(entities.size())));
               return true;
            }
         }
      }
   }

   @Override
   public boolean canContinueToUse() {
      return this.this$0.getWololoTarget() != null && this.attackWarmupDelay > 0;
   }

   @Override
   public void stop() {
      super.stop();
      this.this$0.setWololoTarget(null);
   }

   @Override
   protected void performSpellCasting() {
      Sheep wololoTarget = this.this$0.getWololoTarget();
      if (wololoTarget != null && wololoTarget.isAlive()) {
         wololoTarget.setColor(DyeColor.RED);
      }
   }

   @Override
   protected int getCastWarmupTime() {
      return 40;
   }

   @Override
   protected int getCastingTime() {
      return 60;
   }

   @Override
   protected int getCastingInterval() {
      return 140;
   }

   @Override
   protected SoundEvent getSpellPrepareSound() {
      return SoundEvents.EVOKER_PREPARE_WOLOLO;
   }

   @Override
   protected SpellcasterIllager$IllagerSpell getSpell() {
      return SpellcasterIllager$IllagerSpell.WOLOLO;
   }
}
