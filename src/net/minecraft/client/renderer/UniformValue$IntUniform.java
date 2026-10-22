package net.minecraft.client.renderer;

import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record UniformValue$IntUniform(int value) implements UniformValue {
   public static final Codec<UniformValue$IntUniform> CODEC = Codec.INT.xmap(UniformValue$IntUniform::new, UniformValue$IntUniform::value);

   @Override
   public void writeTo(final Std140Builder builder) {
      builder.putInt(this.value);
   }

   @Override
   public void addSize(final Std140SizeCalculator calculator) {
      calculator.putInt();
   }

   @Override
   public UniformValue$Type type() {
      return UniformValue$Type.INT;
   }
}
