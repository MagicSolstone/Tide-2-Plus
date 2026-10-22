package net.minecraft.client.renderer;

import com.mojang.blaze3d.buffers.Std140Builder;
import java.nio.ByteBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record DynamicGpuData$ChunkSectionInfo(int x, int y, int z, float visibility) implements DynamicGpuDataStorage$DynamicGpuData {
   @Override
   public void write(final ByteBuffer buffer) {
      Std140Builder.intoBuffer(buffer).putIVec3(this.x, this.y, this.z).putFloat(this.visibility);
   }
}
