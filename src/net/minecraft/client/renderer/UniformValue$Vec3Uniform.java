package net.minecraft.client.renderer;

import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector3fc;

@Environment(EnvType.CLIENT)
public record UniformValue$Vec3Uniform(Vector3fc value) implements UniformValue {
   public static final Codec<UniformValue$Vec3Uniform> CODEC = ExtraCodecs.VECTOR3F.xmap(UniformValue$Vec3Uniform::new, UniformValue$Vec3Uniform::value);

   @Override
   public void writeTo(final Std140Builder builder) {
      builder.putVec3(this.value);
   }

   @Override
   public void addSize(final Std140SizeCalculator calculator) {
      calculator.putVec3();
   }

   @Override
   public UniformValue$Type type() {
      return UniformValue$Type.VEC3;
   }
}
