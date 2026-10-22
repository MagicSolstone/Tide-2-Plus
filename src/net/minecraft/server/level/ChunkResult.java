package net.minecraft.server.level;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jspecify.annotations.Nullable;

public interface ChunkResult<T> {
   static <T> ChunkResult<T> of(final T value) {
      return new ChunkResult$Success<>(value);
   }

   static <T> ChunkResult<T> error(final String error) {
      return error(() -> error);
   }

   static <T> ChunkResult<T> error(final Supplier<String> errorSupplier) {
      return new ChunkResult$Fail<>(errorSupplier);
   }

   boolean isSuccess();

   @Nullable
   T orElse(@Nullable T orElse);

   @Nullable
   static <R> R orElse(final ChunkResult<? extends R> chunkResult, @Nullable final R orElse) {
      R result = (R)chunkResult.orElse(null);
      return result != null ? result : orElse;
   }

   @Nullable
   String getError();

   ChunkResult<T> ifSuccess(Consumer<T> consumer);

   <R> ChunkResult<R> map(Function<T, R> map);

   <E extends Throwable> T orElseThrow(Supplier<E> exceptionSupplier) throws E;
}
