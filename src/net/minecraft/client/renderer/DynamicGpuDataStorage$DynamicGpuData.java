package net.minecraft.client.renderer;

import java.nio.ByteBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface DynamicGpuDataStorage$DynamicGpuData {
   void write(ByteBuffer byteBuffer);
}
