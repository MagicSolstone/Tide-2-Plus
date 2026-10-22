package net.minecraft.data;

import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface HashCache$UpdateFunction {
   CompletableFuture<?> update(CachedOutput output);
}
