package net.minecraft.client.renderer;

import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.ExtraCodecs;
import org.joml.Matrix4fc;

@Environment(EnvType.CLIENT)
public record UniformValue$Matrix4x4Uniform(Matrix4fc value) implements UniformValue {
   public static final Codec<UniformValue$Matrix4x4Uniform> CODEC = ExtraCodecs.MATRIX4F
      .xmap(UniformValue$Matrix4x4Uniform::new, UniformValue$Matrix4x4Uniform::value);

   @Override
   public void writeTo(final Std140Builder builder) {
      builder.putMat4f(this.value);
   }

   @Override
   public void addSize(final Std140SizeCalculator calculator) {
      calculator.putMat4f();
   }

   @Override
   public UniformValue$Type type() {
      return UniformValue$Type.MATRIX4X4;
   }
}
