package net.minecraft.world.level.chunk.storage;

import org.jspecify.annotations.Nullable;

@FunctionalInterface
interface IOWorker$ThrowingSupplier<T> {
   @Nullable
   T get() throws Exception;
}
