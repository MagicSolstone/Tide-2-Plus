package net.minecraft.client.renderer;

import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector4fc;

@Environment(EnvType.CLIENT)
public record UniformValue$Vec4Uniform(Vector4fc value) implements UniformValue {
   public static final Codec<UniformValue$Vec4Uniform> CODEC = ExtraCodecs.VECTOR4F.xmap(UniformValue$Vec4Uniform::new, UniformValue$Vec4Uniform::value);

   @Override
   public void writeTo(final Std140Builder builder) {
      builder.putVec4(this.value);
   }

   @Override
   public void addSize(final Std140SizeCalculator calculator) {
      calculator.putVec4();
   }

   @Override
   public UniformValue$Type type() {
      return UniformValue$Type.VEC4;
   }
}
