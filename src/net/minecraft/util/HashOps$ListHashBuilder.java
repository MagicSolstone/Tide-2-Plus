package net.minecraft.util;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hasher;
import com.mojang.serialization.DataResult;
import java.util.Objects;

class HashOps$ListHashBuilder extends AbstractListBuilder<HashCode, Hasher> {
   public HashOps$ListHashBuilder(final HashOps this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x);
   }

   protected Hasher initBuilder() {
      return this.this$0.hashFunction.newHasher().putByte((byte)4);
   }

   protected Hasher append(final Hasher hasher, final HashCode value) {
      return hasher.putBytes(value.asBytes());
   }

   protected DataResult<HashCode> build(final Hasher hasher, final HashCode prefix) {
      if (!$assertionsDisabled && !prefix.equals(this.this$0.empty)) {
         throw new AssertionError();
      } else {
         hasher.putByte((byte)5);
         return DataResult.success(hasher.hash());
      }
   }
}
