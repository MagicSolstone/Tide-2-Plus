package com.mojang.renderpearl.api.pipeline;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum IndexType {
   SHORT(2),
   INT(4);

   public final int bytes;

   private IndexType(final int bytes) {
      this.bytes = bytes;
   }

   public static IndexType least(final int length) {
      return (length & -65536) != 0 ? INT : SHORT;
   }
}
