package net.minecraft.world.entity.animal.pig;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record PigSoundVariant(PigSoundVariant$PigSoundSet adultSounds, PigSoundVariant$PigSoundSet babySounds) {
   public static final Codec<PigSoundVariant> DIRECT_CODEC = codec();
   public static final Codec<PigSoundVariant> NETWORK_CODEC = codec();
   public static final Codec<Holder<PigSoundVariant>> CODEC = RegistryCodecs.holder(Registries.PIG_SOUND_VARIANT);
   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<PigSoundVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.PIG_SOUND_VARIANT);

   private static Codec<PigSoundVariant> codec() {
      return RecordCodecBuilder.create(
         i -> i.group(
               PigSoundVariant$PigSoundSet.CODEC.fieldOf("adult_sounds").forGetter(PigSoundVariant::adultSounds),
               PigSoundVariant$PigSoundSet.CODEC.fieldOf("baby_sounds").forGetter(PigSoundVariant::babySounds)
            )
            .apply(i, PigSoundVariant::new)
      );
   }
}
