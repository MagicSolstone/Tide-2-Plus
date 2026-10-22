package net.minecraft.advancements.predicates;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;

public class MobEffectsPredicate$Builder {
   private final Builder<Holder<MobEffect>, MobEffectsPredicate$MobEffectInstancePredicate> effectMap = ImmutableMap.builder();

   public static MobEffectsPredicate$Builder effects() {
      return new MobEffectsPredicate$Builder();
   }

   public MobEffectsPredicate$Builder and(final Holder<MobEffect> effect) {
      this.effectMap.put(effect, new MobEffectsPredicate$MobEffectInstancePredicate());
      return this;
   }

   public MobEffectsPredicate$Builder and(final Holder<MobEffect> effect, final MobEffectsPredicate$MobEffectInstancePredicate predicate) {
      this.effectMap.put(effect, predicate);
      return this;
   }

   public MobEffectsPredicate build() {
      return new MobEffectsPredicate(this.effectMap.build());
   }
}
