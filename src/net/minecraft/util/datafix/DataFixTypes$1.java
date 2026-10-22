package net.minecraft.util.datafix;

import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import java.util.Objects;

class DataFixTypes$1 implements Codec<A> {
   DataFixTypes$1(final DataFixTypes this$0, final Codec val$codec, final int val$defaultVersion, final DataFixer val$dataFixer) {
      this.val$codec = val$codecx;
      this.val$defaultVersion = val$defaultVersionx;
      this.val$dataFixer = val$dataFixerx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   public <T> DataResult<T> encode(final A input, final DynamicOps<T> ops, final T prefix) {
      return this.val$codec
         .encode(input, ops, prefix)
         .flatMap(data -> ops.mergeToMap(data, ops.createString("DataVersion"), ops.createInt(DataFixTypes.currentVersion())));
   }

   public <T> DataResult<Pair<A, T>> decode(final DynamicOps<T> ops, final T input) {
      int fromVersion = ops.get(input, "DataVersion").flatMap(ops::getNumberValue).map(Number::intValue).result().orElse(this.val$defaultVersion);
      Dynamic<T> dataWithoutVersion = new Dynamic(ops, ops.remove(input, "DataVersion"));
      Dynamic<T> fixedData = this.this$0.updateToCurrentVersion(this.val$dataFixer, dataWithoutVersion, fromVersion);
      return this.val$codec.decode(fixedData);
   }
}
