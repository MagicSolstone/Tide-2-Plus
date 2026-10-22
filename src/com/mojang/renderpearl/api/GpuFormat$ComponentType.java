package com.mojang.renderpearl.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum GpuFormat$ComponentType {
   UNORM_8(1),
   SNORM_8(1),
   UINT_8(1),
   SINT_8(1),
   UNORM_16(2),
   SNORM_16(2),
   UINT_16(2),
   SINT_16(2),
   FLOAT_16(2),
   UINT_32(4),
   SINT_32(4),
   FLOAT_32(4),
   OPAQUE_8(1),
   OPAQUE_16(2),
   OPAQUE_32(4),
   OPAQUE_64(8);

   private final int byteSize;

   private GpuFormat$ComponentType(final int byteSize) {
      this.byteSize = byteSize;
   }

   public int byteSize() {
      return this.byteSize;
   }
}
