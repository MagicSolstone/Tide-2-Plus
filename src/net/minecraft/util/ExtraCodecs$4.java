package net.minecraft.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.Codec.ResultFunction;
import java.util.function.Function;

class ExtraCodecs$4 implements ResultFunction<E> {
   ExtraCodecs$4(final Function val$decodeLifecycle, final Function val$encodeLifecycle) {
      this.val$decodeLifecycle = val$decodeLifecyclex;
      this.val$encodeLifecycle = val$encodeLifecyclex;
   }

   public <T> DataResult<Pair<E, T>> apply(final DynamicOps<T> ops, final T input, final DataResult<Pair<E, T>> a) {
      return (DataResult<Pair<E, T>>)a.result().map(r -> a.setLifecycle((Lifecycle)decodeLifecycle.apply(r.getFirst()))).orElse(a);
   }

   public <T> DataResult<T> coApply(final DynamicOps<T> ops, final E input, final DataResult<T> t) {
      return t.setLifecycle((Lifecycle)this.val$encodeLifecycle.apply((T)input));
   }

   @Override
   public String toString() {
      return "WithLifecycle[" + this.val$decodeLifecycle + " " + this.val$encodeLifecycle + "]";
   }
}
