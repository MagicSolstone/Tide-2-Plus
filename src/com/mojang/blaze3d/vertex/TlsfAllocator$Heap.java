package com.mojang.blaze3d.vertex;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class TlsfAllocator$Heap {
   private final long size;

   public TlsfAllocator$Heap(final long size) {
      this.size = size;
   }
}
