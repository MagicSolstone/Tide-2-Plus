package net.minecraft.world.entity.monster.illager;

import java.util.Objects;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jspecify.annotations.Nullable;

public abstract class SpellcasterIllager$SpellcasterUseSpellGoal extends Goal {
   protected int attackWarmupDelay;
   protected int nextAttackTickCount;

   protected SpellcasterIllager$SpellcasterUseSpellGoal(final SpellcasterIllager this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public boolean canUse() {
      LivingEntity target = this.this$0.getTarget();
      if (target == null || !target.isAlive()) {
         return false;
      } else {
         return this.this$0.isCastingSpell() ? false : this.this$0.tickCount >= this.nextAttackTickCount;
      }
   }

   @Override
   public boolean canContinueToUse() {
      LivingEntity target = this.this$0.getTarget();
      return target != null && target.isAlive() && this.attackWarmupDelay > 0;
   }

   @Override
   public void start() {
      this.attackWarmupDelay = this.adjustedTickDelay(this.getCastWarmupTime());
      this.this$0.spellCastingTickCount = this.getCastingTime();
      this.nextAttackTickCount = this.this$0.tickCount + this.getCastingInterval();
      SoundEvent spellPrepareSound = this.getSpellPrepareSound();
      if (spellPrepareSound != null) {
         this.this$0.playSound(spellPrepareSound, 1.0F, 1.0F);
      }

      this.this$0.setIsCastingSpell(this.getSpell());
   }

   @Override
   public void tick() {
      this.attackWarmupDelay--;
      if (this.attackWarmupDelay == 0) {
         this.performSpellCasting();
         this.this$0.playSound(this.this$0.getCastingSoundEvent(), 1.0F, 1.0F);
      }
   }

   protected abstract void performSpellCasting();

   protected int getCastWarmupTime() {
      return 20;
   }

   protected abstract int getCastingTime();

   protected abstract int getCastingInterval();

   @Nullable
   protected abstract SoundEvent getSpellPrepareSound();

   protected abstract SpellcasterIllager$IllagerSpell getSpell();
}
