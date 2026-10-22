package net.minecraft.world.attribute.modifier;

import com.mojang.serialization.Codec;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.LerpFunction;

@FunctionalInterface
public interface IntegerModifier$Simple extends IntegerModifier<Integer> {
   @Override
   default Codec<Integer> argumentCodec(final EnvironmentAttribute<Integer> type) {
      return Codec.INT;
   }

   @Override
   default LerpFunction<Integer> argumentKeyframeLerp(final EnvironmentAttribute<Integer> type) {
      return LerpFunction.ofInteger();
   }
}
