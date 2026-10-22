package net.minecraft.core.component;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

class DataComponentPatch$1 implements DataComponentPatch$CodecGetter {
   @Override
   public <T> StreamCodec<RegistryFriendlyByteBuf, T> apply(final DataComponentType<T> type) {
      return type.streamCodec().cast();
   }
}
