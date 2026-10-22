package net.minecraft.resources;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.RecordBuilder;
import java.util.Objects;
import java.util.function.UnaryOperator;

public class DelegatingOps$DelegateRecordBuilder implements RecordBuilder<T> {
   private final RecordBuilder<T> original;

   protected DelegatingOps$DelegateRecordBuilder(final RecordBuilder<T> this$0, final RecordBuilder original) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.original = original;
   }

   public DynamicOps<T> ops() {
      return this.this$0;
   }

   public RecordBuilder<T> add(final T key, final T value) {
      this.original.add(key, value);
      return this;
   }

   public RecordBuilder<T> add(final T key, final DataResult<T> value) {
      this.original.add(key, value);
      return this;
   }

   public RecordBuilder<T> add(final DataResult<T> key, final DataResult<T> value) {
      this.original.add(key, value);
      return this;
   }

   public RecordBuilder<T> add(final String key, final T value) {
      this.original.add(key, value);
      return this;
   }

   public RecordBuilder<T> add(final String key, final DataResult<T> value) {
      this.original.add(key, value);
      return this;
   }

   public <E> RecordBuilder<T> add(final String key, final E value, final Encoder<E> encoder) {
      return this.original.add(key, encoder.encodeStart(this.ops(), value));
   }

   public RecordBuilder<T> withErrorsFrom(final DataResult<?> result) {
      this.original.withErrorsFrom(result);
      return this;
   }

   public RecordBuilder<T> setLifecycle(final Lifecycle lifecycle) {
      this.original.setLifecycle(lifecycle);
      return this;
   }

   public RecordBuilder<T> mapError(final UnaryOperator<String> onError) {
      this.original.mapError(onError);
      return this;
   }

   public DataResult<T> build(final T prefix) {
      return this.original.build(prefix);
   }

   public DataResult<T> build(final DataResult<T> prefix) {
      return this.original.build(prefix);
   }
}
