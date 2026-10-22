package net.minecraft.core.component;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

class DataComponentPatch$2 implements DataComponentPatch$CodecGetter {
   @Override
   public <T> StreamCodec<RegistryFriendlyByteBuf, T> apply(final DataComponentType<T> type) {
      StreamCodec<RegistryFriendlyByteBuf, T> original = type.streamCodec().cast();
      return original.apply(ByteBufCodecs.registryFriendlyLengthPrefixed(Integer.MAX_VALUE));
   }
}
