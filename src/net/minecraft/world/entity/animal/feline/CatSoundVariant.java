package net.minecraft.world.entity.animal.feline;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record CatSoundVariant(CatSoundVariant$CatSoundSet adultSounds, CatSoundVariant$CatSoundSet babySounds) {
   public static final Codec<CatSoundVariant> DIRECT_CODEC = codec();
   public static final Codec<CatSoundVariant> NETWORK_CODEC = codec();
   public static final Codec<Holder<CatSoundVariant>> CODEC = RegistryCodecs.holder(Registries.CAT_SOUND_VARIANT);
   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<CatSoundVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.CAT_SOUND_VARIANT);

   private static Codec<CatSoundVariant> codec() {
      return RecordCodecBuilder.create(
         i -> i.group(
               CatSoundVariant$CatSoundSet.CODEC.fieldOf("adult_sounds").forGetter(CatSoundVariant::adultSounds),
               CatSoundVariant$CatSoundSet.CODEC.fieldOf("baby_sounds").forGetter(CatSoundVariant::babySounds)
            )
            .apply(i, CatSoundVariant::new)
      );
   }
}
