package net.minecraft.client.renderer;

import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface UniformValue {
   Codec<UniformValue> CODEC = UniformValue$Type.CODEC.dispatch(UniformValue::type, t -> t.valueCodec);

   void writeTo(Std140Builder builder);

   void addSize(Std140SizeCalculator calculator);

   UniformValue$Type type();
}
