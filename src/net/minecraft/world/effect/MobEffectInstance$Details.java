package net.minecraft.world.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

record MobEffectInstance$Details(
   int amplifier, int duration, boolean ambient, boolean showParticles, boolean showIcon, Optional<MobEffectInstance$Details> hiddenEffect
) {
   public static final MapCodec<MobEffectInstance$Details> MAP_CODEC = MapCodec.recursive(
      "MobEffectInstance.Details",
      codec -> RecordCodecBuilder.mapCodec(
         i -> i.group(
               ExtraCodecs.UNSIGNED_BYTE.optionalFieldOf("amplifier", 0).forGetter(MobEffectInstance$Details::amplifier),
               Codec.INT.optionalFieldOf("duration", 0).forGetter(MobEffectInstance$Details::duration),
               Codec.BOOL.optionalFieldOf("ambient", false).forGetter(MobEffectInstance$Details::ambient),
               Codec.BOOL.optionalFieldOf("show_particles", true).forGetter(MobEffectInstance$Details::showParticles),
               Codec.BOOL.optionalFieldOf("show_icon").forGetter(d -> Optional.of(d.showIcon())),
               codec.optionalFieldOf("hidden_effect").forGetter(MobEffectInstance$Details::hiddenEffect)
            )
            .apply(i, MobEffectInstance$Details::create)
      )
   );
   public static final StreamCodec<ByteBuf, MobEffectInstance$Details> STREAM_CODEC = StreamCodec.recursive(
      subCodec -> StreamCodec.composite(
         ByteBufCodecs.VAR_INT,
         MobEffectInstance$Details::amplifier,
         ByteBufCodecs.VAR_INT,
         MobEffectInstance$Details::duration,
         ByteBufCodecs.BOOL,
         MobEffectInstance$Details::ambient,
         ByteBufCodecs.BOOL,
         MobEffectInstance$Details::showParticles,
         ByteBufCodecs.BOOL,
         MobEffectInstance$Details::showIcon,
         subCodec.apply(ByteBufCodecs::optional),
         MobEffectInstance$Details::hiddenEffect,
         MobEffectInstance$Details::new
      )
   );

   private static MobEffectInstance$Details create(
      final int amplifier,
      final int duration,
      final boolean ambient,
      final boolean showParticles,
      final Optional<Boolean> showIcon,
      final Optional<MobEffectInstance$Details> hiddenEffect
   ) {
      return new MobEffectInstance$Details(amplifier, duration, ambient, showParticles, showIcon.orElse(showParticles), hiddenEffect);
   }
}
