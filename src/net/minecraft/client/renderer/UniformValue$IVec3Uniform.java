package net.minecraft.client.renderer;

import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector3ic;

@Environment(EnvType.CLIENT)
public record UniformValue$IVec3Uniform(Vector3ic value) implements UniformValue {
   public static final Codec<UniformValue$IVec3Uniform> CODEC = ExtraCodecs.VECTOR3I.xmap(UniformValue$IVec3Uniform::new, UniformValue$IVec3Uniform::value);

   @Override
   public void writeTo(final Std140Builder builder) {
      builder.putIVec3(this.value);
   }

   @Override
   public void addSize(final Std140SizeCalculator calculator) {
      calculator.putIVec3();
   }

   @Override
   public UniformValue$Type type() {
      return UniformValue$Type.IVEC3;
   }
}
