package net.minecraft.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

public class EncoderCache {
   private final LoadingCache<EncoderCache$Key<?, ?>, DataResult<?>> cache;

   public EncoderCache(final int maximumSize) {
      this.cache = CacheBuilder.newBuilder().maximumSize(maximumSize).concurrencyLevel(1).softValues().build(new EncoderCache$1(this));
   }

   public <A> Codec<A> wrap(final Codec<A> codec) {
      return new EncoderCache$2(this, codec);
   }
}
