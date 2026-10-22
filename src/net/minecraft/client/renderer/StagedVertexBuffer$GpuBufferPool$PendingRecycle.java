package net.minecraft.client.renderer;

import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.GpuFence;
import com.mojang.renderpearl.util.UncheckedAutoCloseable;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
record StagedVertexBuffer$GpuBufferPool$PendingRecycle(List<GpuBuffer> buffers, GpuFence fence) implements AutoCloseable {
   @Nullable
   public List<GpuBuffer> tryRecycle() {
      if (this.fence.awaitCompletion(0L)) {
         this.fence.close();
         return this.buffers;
      } else {
         return null;
      }
   }

   @Override
   public void close() {
      this.buffers.forEach(UncheckedAutoCloseable::close);
      this.fence.close();
   }
}
