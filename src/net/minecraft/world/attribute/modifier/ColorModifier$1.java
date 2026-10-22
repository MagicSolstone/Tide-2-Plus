package net.minecraft.world.attribute.modifier;

import com.mojang.serialization.Codec;
import net.minecraft.util.ARGB;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.LerpFunction;
import org.joml.Vector3fc;

class ColorModifier$1 implements ColorModifier<Vector3fc, ColorModifier$BlendToGray> {
   public Vector3fc apply(final Vector3fc subject, final ColorModifier$BlendToGray argument) {
      Vector3fc multipliedGreyscale = ARGB.scaleRGB(ARGB.greyscale(subject), argument.brightness);
      return ARGB.srgbLerp(argument.factor, subject, multipliedGreyscale);
   }

   @Override
   public Codec<ColorModifier$BlendToGray> argumentCodec(final EnvironmentAttribute<Vector3fc> type) {
      return ColorModifier$BlendToGray.CODEC;
   }

   @Override
   public LerpFunction<ColorModifier$BlendToGray> argumentKeyframeLerp(final EnvironmentAttribute<Vector3fc> type) {
      return ColorModifier$BlendToGray::lerp;
   }
}
