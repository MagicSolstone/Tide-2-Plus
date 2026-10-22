package net.minecraft.advancements.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.jspecify.annotations.Nullable;

public record MobEffectsPredicate$MobEffectInstancePredicate(
   MinMaxBounds$Ints amplifier, MinMaxBounds$Ints duration, Optional<Boolean> ambient, Optional<Boolean> visible
) {
   public static final Codec<MobEffectsPredicate$MobEffectInstancePredicate> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            MinMaxBounds$Ints.CODEC.optionalFieldOf("amplifier", MinMaxBounds$Ints.ANY).forGetter(MobEffectsPredicate$MobEffectInstancePredicate::amplifier),
            MinMaxBounds$Ints.CODEC.optionalFieldOf("duration", MinMaxBounds$Ints.ANY).forGetter(MobEffectsPredicate$MobEffectInstancePredicate::duration),
            Codec.BOOL.optionalFieldOf("ambient").forGetter(MobEffectsPredicate$MobEffectInstancePredicate::ambient),
            Codec.BOOL.optionalFieldOf("visible").forGetter(MobEffectsPredicate$MobEffectInstancePredicate::visible)
         )
         .apply(i, MobEffectsPredicate$MobEffectInstancePredicate::new)
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, MobEffectsPredicate$MobEffectInstancePredicate> STREAM_CODEC = StreamCodec.composite(
      MinMaxBounds$Ints.STREAM_CODEC,
      MobEffectsPredicate$MobEffectInstancePredicate::amplifier,
      MinMaxBounds$Ints.STREAM_CODEC,
      MobEffectsPredicate$MobEffectInstancePredicate::duration,
      ByteBufCodecs.optional(ByteBufCodecs.BOOL),
      MobEffectsPredicate$MobEffectInstancePredicate::ambient,
      ByteBufCodecs.optional(ByteBufCodecs.BOOL),
      MobEffectsPredicate$MobEffectInstancePredicate::visible,
      MobEffectsPredicate$MobEffectInstancePredicate::new
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, Map<Holder<MobEffect>, MobEffectsPredicate$MobEffectInstancePredicate>> MAP_STREAM_CODEC = ByteBufCodecs.map(
      HashMap::new, MobEffect.STREAM_CODEC, STREAM_CODEC
   );

   public MobEffectsPredicate$MobEffectInstancePredicate() {
      this(MinMaxBounds$Ints.ANY, MinMaxBounds$Ints.ANY, Optional.empty(), Optional.empty());
   }

   public boolean matches(@Nullable final MobEffectInstance instance) {
      if (instance == null) {
         return false;
      } else if (!this.amplifier.matches(instance.getAmplifier())) {
         return false;
      } else if (!this.duration.matches(instance.getDuration())) {
         return false;
      } else {
         return this.ambient.isPresent() && this.ambient.get() != instance.isAmbient()
            ? false
            : !this.visible.isPresent() || this.visible.get() == instance.isVisible();
      }
   }
}
