package net.minecraft.client.renderer;

import com.mojang.blaze3d.buffers.Std140Builder;
import java.nio.ByteBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Matrix4fc;
import org.joml.Vector3fc;
import org.joml.Vector4fc;

@Environment(EnvType.CLIENT)
public record DynamicGpuData$Transform(Matrix4fc modelView, Vector4fc colorModulator, Vector3fc modelOffset, Matrix4fc textureMatrix)
   implements DynamicGpuDataStorage$DynamicGpuData {
   @Override
   public void write(final ByteBuffer buffer) {
      Std140Builder.intoBuffer(buffer).putMat4f(this.modelView).putMat4f(this.textureMatrix).putVec4(this.colorModulator).putVec3(this.modelOffset);
   }
}
