package net.minecraft.advancements.predicates;

import com.mojang.serialization.Codec;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public record MobEffectsPredicate(Map<Holder<MobEffect>, MobEffectsPredicate$MobEffectInstancePredicate> effectMap) implements Predicate<MobEffectInstance> {
   public static final Codec<MobEffectsPredicate> CODEC = Codec.unboundedMap(MobEffect.CODEC, MobEffectsPredicate$MobEffectInstancePredicate.CODEC)
      .xmap(MobEffectsPredicate::new, MobEffectsPredicate::effectMap);
   public static final StreamCodec<RegistryFriendlyByteBuf, MobEffectsPredicate> STREAM_CODEC = MobEffectsPredicate$MobEffectInstancePredicate.MAP_STREAM_CODEC
      .map(MobEffectsPredicate::new, MobEffectsPredicate::effectMap);

   public boolean matches(final Entity entity) {
      return entity instanceof LivingEntity living && this.matches(living.getActiveEffectsMap());
   }

   public boolean matches(final LivingEntity entity) {
      return this.matches(entity.getActiveEffectsMap());
   }

   public boolean matches(final Map<Holder<MobEffect>, MobEffectInstance> effects) {
      for (Entry<Holder<MobEffect>, MobEffectsPredicate$MobEffectInstancePredicate> entry : this.effectMap.entrySet()) {
         MobEffectInstance instance = effects.get(entry.getKey());
         if (!entry.getValue().matches(instance)) {
            return false;
         }
      }

      return true;
   }

   public boolean test(final MobEffectInstance mobEffect) {
      MobEffectsPredicate$MobEffectInstancePredicate predicate = this.effectMap.get(mobEffect.getEffect());
      return predicate != null && predicate.matches(mobEffect);
   }
}
