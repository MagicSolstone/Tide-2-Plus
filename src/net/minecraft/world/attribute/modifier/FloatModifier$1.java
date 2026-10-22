package net.minecraft.world.attribute.modifier;

import com.mojang.serialization.Codec;
import net.minecraft.util.Mth;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.LerpFunction;

class FloatModifier$1 implements FloatModifier<FloatWithAlpha> {
   public Float apply(final Float subject, final FloatWithAlpha argument) {
      return Mth.lerp(argument.alpha(), subject, argument.value());
   }

   @Override
   public Codec<FloatWithAlpha> argumentCodec(final EnvironmentAttribute<Float> type) {
      return FloatWithAlpha.CODEC;
   }

   @Override
   public LerpFunction<FloatWithAlpha> argumentKeyframeLerp(final EnvironmentAttribute<Float> type) {
      return (alpha, from, to) -> new FloatWithAlpha(Mth.lerp(alpha, from.value(), to.value()), Mth.lerp(alpha, from.alpha(), to.alpha()));
   }
}
