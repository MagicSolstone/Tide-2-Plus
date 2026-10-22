package net.minecraft.client.renderer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.StringRepresentable;

@Environment(EnvType.CLIENT)
public enum UniformValue$Type implements StringRepresentable {
   INT("int", UniformValue$IntUniform.CODEC),
   IVEC3("ivec3", UniformValue$IVec3Uniform.CODEC),
   FLOAT("float", UniformValue$FloatUniform.CODEC),
   VEC2("vec2", UniformValue$Vec2Uniform.CODEC),
   VEC3("vec3", UniformValue$Vec3Uniform.CODEC),
   VEC4("vec4", UniformValue$Vec4Uniform.CODEC),
   MATRIX4X4("matrix4x4", UniformValue$Matrix4x4Uniform.CODEC);

   public static final Codec<UniformValue$Type> CODEC = StringRepresentable.fromEnum(UniformValue$Type::values);
   private final String name;
   private final MapCodec<? extends UniformValue> valueCodec;

   private UniformValue$Type(final String name, final Codec<? extends UniformValue> valueCodec) {
      this.name = name;
      this.valueCodec = valueCodec.fieldOf("value");
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
