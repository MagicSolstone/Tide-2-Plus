package net.minecraft.resources;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.ListBuilder;
import java.util.Objects;
import java.util.function.UnaryOperator;

public class DelegatingOps$DelegateListBuilder implements ListBuilder<T> {
   private final ListBuilder<T> original;

   protected DelegatingOps$DelegateListBuilder(final ListBuilder<T> this$0, final ListBuilder original) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.original = original;
   }

   public DynamicOps<T> ops() {
      return this.this$0;
   }

   public DataResult<T> build(final T prefix) {
      return this.original.build(prefix);
   }

   public ListBuilder<T> add(final T value) {
      this.original.add(value);
      return this;
   }

   public ListBuilder<T> add(final DataResult<T> value) {
      this.original.add(value);
      return this;
   }

   public <E> ListBuilder<T> add(final E value, final Encoder<E> encoder) {
      this.original.add(encoder.encodeStart(this.ops(), value));
      return this;
   }

   public <E> ListBuilder<T> addAll(final Iterable<E> values, final Encoder<E> encoder) {
      values.forEach(v -> this.original.add(encoder.encode(v, this.ops(), this.ops().empty())));
      return this;
   }

   public ListBuilder<T> withErrorsFrom(final DataResult<?> result) {
      this.original.withErrorsFrom(result);
      return this;
   }

   public ListBuilder<T> mapError(final UnaryOperator<String> onError) {
      this.original.mapError(onError);
      return this;
   }

   public DataResult<T> build(final DataResult<T> prefix) {
      return this.original.build(prefix);
   }
}
