package com.mojang.renderpearl.api.vertex;

import com.mojang.renderpearl.api.GpuFormat;
import java.util.Locale;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record VertexFormatElement(String name, int offset, GpuFormat format) {
   @Override
   public String toString() {
      return String.format(Locale.ROOT, "%s %s offset:%d", this.name, this.format, this.offset);
   }
}
