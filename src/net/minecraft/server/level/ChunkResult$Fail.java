package net.minecraft.server.level;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jspecify.annotations.Nullable;

public record ChunkResult$Fail<T>(Supplier<String> error) implements ChunkResult<T> {
   @Override
   public boolean isSuccess() {
      return false;
   }

   @Nullable
   @Override
   public T orElse(@Nullable final T orElse) {
      return orElse;
   }

   @Override
   public String getError() {
      return this.error.get();
   }

   @Override
   public ChunkResult<T> ifSuccess(final Consumer<T> consumer) {
      return this;
   }

   @Override
   public <R> ChunkResult<R> map(final Function<T, R> map) {
      return new ChunkResult$Fail(this.error);
   }

   @Override
   public <E extends Throwable> T orElseThrow(final Supplier<E> exceptionSupplier) throws E {
      throw exceptionSupplier.get();
   }
}
