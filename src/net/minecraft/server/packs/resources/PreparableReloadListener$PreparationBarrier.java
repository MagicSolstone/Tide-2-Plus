package net.minecraft.server.packs.resources;

import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface PreparableReloadListener$PreparationBarrier {
   <T> CompletableFuture<T> wait(T t);
}
