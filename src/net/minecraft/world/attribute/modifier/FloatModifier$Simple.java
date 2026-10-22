package net.minecraft.world.attribute.modifier;

import com.mojang.serialization.Codec;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.LerpFunction;

@FunctionalInterface
public interface FloatModifier$Simple extends FloatModifier<Float> {
   @Override
   default Codec<Float> argumentCodec(final EnvironmentAttribute<Float> type) {
      return Codec.FLOAT;
   }

   @Override
   default LerpFunction<Float> argumentKeyframeLerp(final EnvironmentAttribute<Float> type) {
      return LerpFunction.ofFloat();
   }
}
