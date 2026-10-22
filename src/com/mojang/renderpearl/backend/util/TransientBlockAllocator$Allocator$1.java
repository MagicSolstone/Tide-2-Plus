package com.mojang.renderpearl.backend.util;

import java.util.function.Consumer;
import java.util.function.LongFunction;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class TransientBlockAllocator$Allocator$1 implements TransientBlockAllocator$Allocator<T> {
   TransientBlockAllocator$Allocator$1(final LongFunction val$alloc, final Consumer val$free) {
      this.val$alloc = val$allocx;
      this.val$free = val$freex;
   }

   @Override
   public T alloc(final long size) {
      return (T)((TransientBlockAllocator$Allocator$Block)this.val$alloc.apply(size));
   }

   @Override
   public void free(final T t) {
      this.val$free.accept(t);
   }
}
