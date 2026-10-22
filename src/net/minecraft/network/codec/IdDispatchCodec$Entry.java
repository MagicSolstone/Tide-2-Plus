package net.minecraft.network.codec;

record IdDispatchCodec$Entry<B, V, T>(StreamCodec<? super B, ? extends V> serializer, T type) {
}
