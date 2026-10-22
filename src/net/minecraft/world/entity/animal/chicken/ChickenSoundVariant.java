package net.minecraft.world.entity.animal.chicken;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ChickenSoundVariant(ChickenSoundVariant$ChickenSoundSet adultSounds, ChickenSoundVariant$ChickenSoundSet babySounds) {
   public static final Codec<ChickenSoundVariant> DIRECT_CODEC = codec();
   public static final Codec<ChickenSoundVariant> NETWORK_CODEC = codec();
   public static final Codec<Holder<ChickenSoundVariant>> CODEC = RegistryCodecs.holder(Registries.CHICKEN_SOUND_VARIANT);
   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<ChickenSoundVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(
      Registries.CHICKEN_SOUND_VARIANT
   );

   private static Codec<ChickenSoundVariant> codec() {
      return RecordCodecBuilder.create(
         i -> i.group(
               ChickenSoundVariant$ChickenSoundSet.CODEC.fieldOf("adult_sounds").forGetter(ChickenSoundVariant::adultSounds),
               ChickenSoundVariant$ChickenSoundSet.CODEC.fieldOf("baby_sounds").forGetter(ChickenSoundVariant::babySounds)
            )
            .apply(i, ChickenSoundVariant::new)
      );
   }
}
