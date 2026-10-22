package net.minecraft.network.codec;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class IdDispatchCodec$Builder<B extends ByteBuf, V, T> {
   private final List<IdDispatchCodec$Entry<B, V, T>> entries = new ArrayList<>();
   private final Function<V, ? extends T> typeGetter;

   private IdDispatchCodec$Builder(final Function<V, ? extends T> typeGetter) {
      this.typeGetter = typeGetter;
   }

   public IdDispatchCodec$Builder<B, V, T> add(final T type, final StreamCodec<? super B, ? extends V> serializer) {
      this.entries.add(new IdDispatchCodec$Entry<>(serializer, type));
      return this;
   }

   public IdDispatchCodec<B, V, T> build() {
      Object2IntOpenHashMap<T> toId = new Object2IntOpenHashMap();
      toId.defaultReturnValue(-2);

      for (IdDispatchCodec$Entry<B, V, T> entry : this.entries) {
         int id = toId.size();
         int previous = toId.putIfAbsent(entry.type, id);
         if (previous != -2) {
            throw new IllegalStateException("Duplicate registration for type " + entry.type);
         }
      }

      return new IdDispatchCodec<>(this.typeGetter, List.copyOf(this.entries), toId);
   }
}
