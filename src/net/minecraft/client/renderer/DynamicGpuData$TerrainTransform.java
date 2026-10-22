package net.minecraft.client.renderer;

import com.mojang.blaze3d.buffers.Std140Builder;
import java.nio.ByteBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Matrix4fc;

@Environment(EnvType.CLIENT)
public record DynamicGpuData$TerrainTransform(Matrix4fc modelView, int textureAtlasWidth, int textureAtlasHeight)
   implements DynamicGpuDataStorage$DynamicGpuData {
   @Override
   public void write(final ByteBuffer buffer) {
      Std140Builder.intoBuffer(buffer).putMat4f(this.modelView).putIVec2(this.textureAtlasWidth, this.textureAtlasHeight);
   }
}
