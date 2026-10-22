package net.minecraft.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JavaOps;
import java.util.Objects;
import net.minecraft.core.HolderLookup$Provider;

class PlaceholderLookupProvider$2 implements RegistryContextSwapper {
   PlaceholderLookupProvider$2(final PlaceholderLookupProvider this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public <T> DataResult<T> swapTo(final Codec<T> codec, final T value, final HolderLookup$Provider newContext) {
      return codec.encodeStart(this.this$0.createSerializationContext(JavaOps.INSTANCE), value)
         .flatMap(v -> codec.parse(newContext.createSerializationContext(JavaOps.INSTANCE), v));
   }
}
