package net.minecraft.world.attribute.modifier;

import com.mojang.serialization.Codec;
import net.minecraft.util.ARGB;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.LerpFunction;
import org.joml.Vector4fc;

class ColorModifier$2 implements ColorModifier<Vector4fc, ColorModifier$BlendToGray> {
   public Vector4fc apply(final Vector4fc subject, final ColorModifier$BlendToGray argument) {
      Vector4fc multipliedGreyscale = ARGB.scaleRGB(ARGB.greyscale(subject), argument.brightness);
      return ARGB.srgbLerp(argument.factor, subject, multipliedGreyscale);
   }

   @Override
   public Codec<ColorModifier$BlendToGray> argumentCodec(final EnvironmentAttribute<Vector4fc> type) {
      return ColorModifier$BlendToGray.CODEC;
   }

   @Override
   public LerpFunction<ColorModifier$BlendToGray> argumentKeyframeLerp(final EnvironmentAttribute<Vector4fc> type) {
      return ColorModifier$BlendToGray::lerp;
   }
}
