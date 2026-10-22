package net.minecraft.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

record EncoderCache$Key<A, T>(Codec<A> codec, A value, DynamicOps<T> ops) {
   public DataResult<T> resolve() {
      return this.codec.encodeStart(this.ops, this.value);
   }

   @Override
   public boolean equals(final Object obj) {
      if (this == obj) {
         return true;
      } else {
         return !(obj instanceof EncoderCache$Key<?, ?> key) ? false : this.codec == key.codec && this.value.equals(key.value) && this.ops.equals(key.ops);
      }
   }

   @Override
   public int hashCode() {
      int result = System.identityHashCode(this.codec);
      result = 31 * result + this.value.hashCode();
      return 31 * result + this.ops.hashCode();
   }
}
