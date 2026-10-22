package net.minecraft.world.entity.animal.wolf;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record WolfSoundVariant(WolfSoundVariant$WolfSoundSet adultSounds, WolfSoundVariant$WolfSoundSet babySounds) {
   public static final Codec<WolfSoundVariant> DIRECT_CODEC = getWolfSoundVariantCodec();
   public static final Codec<WolfSoundVariant> NETWORK_CODEC = getWolfSoundVariantCodec();
   public static final Codec<Holder<WolfSoundVariant>> CODEC = RegistryCodecs.holder(Registries.WOLF_SOUND_VARIANT);
   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<WolfSoundVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.WOLF_SOUND_VARIANT);

   private static Codec<WolfSoundVariant> getWolfSoundVariantCodec() {
      return RecordCodecBuilder.create(
         i -> i.group(
               WolfSoundVariant$WolfSoundSet.CODEC.fieldOf("adult_sounds").forGetter(WolfSoundVariant::adultSounds),
               WolfSoundVariant$WolfSoundSet.CODEC.fieldOf("baby_sounds").forGetter(WolfSoundVariant::babySounds)
            )
            .apply(i, WolfSoundVariant::new)
      );
   }
}
