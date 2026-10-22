package net.minecraft.util;

import com.google.common.cache.CacheLoader;
import com.mojang.serialization.DataResult;
import java.util.Objects;

class EncoderCache$1 extends CacheLoader<EncoderCache$Key<?, ?>, DataResult<?>> {
   EncoderCache$1(final EncoderCache this$0) {
      Objects.requireNonNull(this$0);
      super();
   }

   public DataResult<?> load(final EncoderCache$Key<?, ?> key) {
      return key.resolve();
   }
}
