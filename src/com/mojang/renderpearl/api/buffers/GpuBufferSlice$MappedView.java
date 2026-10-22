package com.mojang.renderpearl.api.buffers;

import com.mojang.renderpearl.util.UncheckedAutoCloseable;
import java.nio.ByteBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record GpuBufferSlice$MappedView(GpuBufferSlice slice, ByteBuffer data, Runnable onClose) implements UncheckedAutoCloseable {
   @Override
   public void close() {
      this.onClose.run();
   }
}
