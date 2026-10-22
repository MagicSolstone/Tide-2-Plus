package net.minecraft.client.renderer;

import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector2fc;

@Environment(EnvType.CLIENT)
public record UniformValue$Vec2Uniform(Vector2fc value) implements UniformValue {
   public static final Codec<UniformValue$Vec2Uniform> CODEC = ExtraCodecs.VECTOR2F.xmap(UniformValue$Vec2Uniform::new, UniformValue$Vec2Uniform::value);

   @Override
   public void writeTo(final Std140Builder builder) {
      builder.putVec2(this.value);
   }

   @Override
   public void addSize(final Std140SizeCalculator calculator) {
      calculator.putVec2();
   }

   @Override
   public UniformValue$Type type() {
      return UniformValue$Type.VEC2;
   }
}
