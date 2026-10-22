package com.mojang.renderpearl.backend.util;

import java.util.function.Consumer;
import java.util.function.LongFunction;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface TransientBlockAllocator$Allocator<T extends TransientBlockAllocator$Allocator$Block> {
   T alloc(long size);

   void free(T t);

   static <T extends TransientBlockAllocator$Allocator$Block> TransientBlockAllocator$Allocator<T> create(final LongFunction<T> alloc, final Consumer<T> free) {
      return new TransientBlockAllocator$Allocator$1(alloc, free);
   }
}
