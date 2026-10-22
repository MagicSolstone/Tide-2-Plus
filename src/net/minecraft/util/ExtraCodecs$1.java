package net.minecraft.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Codec.ResultFunction;
import java.util.Optional;
import org.apache.commons.lang3.mutable.MutableObject;

class ExtraCodecs$1 implements ResultFunction<A> {
   ExtraCodecs$1(final Object val$value) {
      this.val$value = val$valuex;
   }

   public <T> DataResult<Pair<A, T>> apply(final DynamicOps<T> ops, final T input, final DataResult<Pair<A, T>> a) {
      MutableObject<String> message = new MutableObject();
      Optional<Pair<A, T>> result = a.resultOrPartial(message::setValue);
      return result.isPresent() ? a : DataResult.error(() -> "(" + (String)message.get() + " -> using default)", Pair.of(this.val$value, input));
   }

   public <T> DataResult<T> coApply(final DynamicOps<T> ops, final A input, final DataResult<T> t) {
      return t;
   }

   @Override
   public String toString() {
      return "OrElsePartial[" + this.val$value + "]";
   }
}
