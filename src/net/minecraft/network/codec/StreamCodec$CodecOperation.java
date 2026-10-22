package net.minecraft.network.codec;

@FunctionalInterface
public interface StreamCodec$CodecOperation<B, S, T> {
   StreamCodec<B, T> apply(StreamCodec<B, S> original);
}
