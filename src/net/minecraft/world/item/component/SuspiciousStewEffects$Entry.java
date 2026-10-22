package net.minecraft.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public record SuspiciousStewEffects$Entry(Holder<MobEffect> effect, int duration) {
   public static final Codec<SuspiciousStewEffects$Entry> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            MobEffect.CODEC.fieldOf("id").forGetter(SuspiciousStewEffects$Entry::effect),
            Codec.INT.lenientOptionalFieldOf("duration", 160).forGetter(SuspiciousStewEffects$Entry::duration)
         )
         .apply(i, SuspiciousStewEffects$Entry::new)
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, SuspiciousStewEffects$Entry> STREAM_CODEC = StreamCodec.composite(
      MobEffect.STREAM_CODEC,
      SuspiciousStewEffects$Entry::effect,
      ByteBufCodecs.VAR_INT,
      SuspiciousStewEffects$Entry::duration,
      SuspiciousStewEffects$Entry::new
   );

   public MobEffectInstance createEffectInstance() {
      return new MobEffectInstance(this.effect, this.duration);
   }
}
