package net.minecraft.client.renderer;

import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record UniformValue$FloatUniform(float value) implements UniformValue {
   public static final Codec<UniformValue$FloatUniform> CODEC = Codec.FLOAT.xmap(UniformValue$FloatUniform::new, UniformValue$FloatUniform::value);

   @Override
   public void writeTo(final Std140Builder builder) {
      builder.putFloat(this.value);
   }

   @Override
   public void addSize(final Std140SizeCalculator calculator) {
      calculator.putFloat();
   }

   @Override
   public UniformValue$Type type() {
      return UniformValue$Type.FLOAT;
   }
}
