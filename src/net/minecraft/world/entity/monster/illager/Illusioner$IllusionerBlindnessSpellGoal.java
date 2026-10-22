package net.minecraft.world.entity.monster.illager;

import java.util.Objects;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

class Illusioner$IllusionerBlindnessSpellGoal extends SpellcasterIllager$SpellcasterUseSpellGoal {
   private int lastTargetId;

   private Illusioner$IllusionerBlindnessSpellGoal(final Illusioner this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x);
   }

   @Override
   public boolean canUse() {
      if (!super.canUse()) {
         return false;
      } else if (this.this$0.getTarget() == null) {
         return false;
      } else {
         return this.this$0.getTarget().getId() == this.lastTargetId
            ? false
            : getServerLevel(this.this$0).getCurrentDifficultyAt(this.this$0.blockPosition()).isHarderThan(Difficulty.NORMAL.ordinal());
      }
   }

   @Override
   public void start() {
      super.start();
      LivingEntity target = this.this$0.getTarget();
      if (target != null) {
         this.lastTargetId = target.getId();
      }
   }

   @Override
   protected int getCastingTime() {
      return 20;
   }

   @Override
   protected int getCastingInterval() {
      return 180;
   }

   @Override
   protected void performSpellCasting() {
      this.this$0.getTarget().addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 400), this.this$0);
   }

   @Override
   protected SoundEvent getSpellPrepareSound() {
      return SoundEvents.ILLUSIONER_PREPARE_BLINDNESS;
   }

   @Override
   protected SpellcasterIllager$IllagerSpell getSpell() {
      return SpellcasterIllager$IllagerSpell.BLINDNESS;
   }
}
