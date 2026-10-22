package net.minecraft.world.entity.monster.illager;

import java.util.Objects;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import org.jspecify.annotations.Nullable;

class Illusioner$IllusionerMirrorSpellGoal extends SpellcasterIllager$SpellcasterUseSpellGoal {
   private Illusioner$IllusionerMirrorSpellGoal(final Illusioner this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x);
   }

   @Override
   public boolean canUse() {
      return !super.canUse() ? false : !this.this$0.hasEffect(MobEffects.INVISIBILITY);
   }

   @Override
   protected int getCastingTime() {
      return 20;
   }

   @Override
   protected int getCastingInterval() {
      return 340;
   }

   @Override
   protected void performSpellCasting() {
      this.this$0.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 1200));
   }

   @Nullable
   @Override
   protected SoundEvent getSpellPrepareSound() {
      return SoundEvents.ILLUSIONER_PREPARE_MIRROR;
   }

   @Override
   protected SpellcasterIllager$IllagerSpell getSpell() {
      return SpellcasterIllager$IllagerSpell.DISAPPEAR;
   }
}
