package net.minecraft.client.renderer;

import java.nio.ByteBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record DynamicGpuData$IndexedDraw(int indexCount, int instanceCount, int firstIndex, int baseVertex, int baseInstance)
   implements DynamicGpuDataStorage$DynamicGpuData {
   @Override
   public void write(final ByteBuffer buffer) {
      buffer.putInt(this.indexCount).putInt(this.instanceCount).putInt(this.firstIndex).putInt(this.baseVertex).putInt(this.baseInstance);
   }
}
