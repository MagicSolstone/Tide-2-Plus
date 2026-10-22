package net.minecraft.util;

import com.google.common.hash.HashCode;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.RecordBuilder.AbstractUniversalBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

final class HashOps$MapHashBuilder extends AbstractUniversalBuilder<HashCode, List<Pair<HashCode, HashCode>>> {
   public HashOps$MapHashBuilder(final HashOps this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x);
   }

   protected List<Pair<HashCode, HashCode>> initBuilder() {
      return new ArrayList<>();
   }

   protected List<Pair<HashCode, HashCode>> append(final HashCode key, final HashCode value, final List<Pair<HashCode, HashCode>> builder) {
      builder.add(Pair.of(key, value));
      return builder;
   }

   protected DataResult<HashCode> build(final List<Pair<HashCode, HashCode>> builder, final HashCode prefix) {
      if (!$assertionsDisabled && !this.this$0.isEmpty(prefix)) {
         throw new AssertionError();
      } else {
         return DataResult.success(HashOps.hashMap(this.this$0.hashFunction.newHasher(), builder.stream()).hash());
      }
   }
}
