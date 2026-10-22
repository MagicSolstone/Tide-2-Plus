package net.minecraft.core.component;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

@FunctionalInterface
interface DataComponentPatch$CodecGetter {
   <T> StreamCodec<? super RegistryFriendlyByteBuf, T> apply(DataComponentType<T> type);
}
