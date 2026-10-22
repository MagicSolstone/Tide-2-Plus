package net.minecraft.server.level;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jspecify.annotations.Nullable;

public record ChunkResult$Success<T>(T value) implements ChunkResult<T> {
   @Override
   public boolean isSuccess() {
      return true;
   }

   @Override
   public T orElse(@Nullable final T orElse) {
      return this.value;
   }

   @Nullable
   @Override
   public String getError() {
      return null;
   }

   @Override
   public ChunkResult<T> ifSuccess(final Consumer<T> consumer) {
      consumer.accept(this.value);
      return this;
   }

   @Override
   public <R> ChunkResult<R> map(final Function<T, R> map) {
      return new ChunkResult$Success<>(map.apply(this.value));
   }

   @Override
   public <E extends Throwable> T orElseThrow(final Supplier<E> exceptionSupplier) throws E {
      return this.value;
   }
}
