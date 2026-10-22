package net.minecraft.world.attribute.modifier;

import com.mojang.serialization.Codec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.LerpFunction;
import org.joml.Vector4fc;

@FunctionalInterface
public interface ColorModifier$ArgbModifier<Subject> extends ColorModifier<Subject, Vector4fc> {
   @Override
   default Codec<Vector4fc> argumentCodec(final EnvironmentAttribute<Subject> type) {
      return ExtraCodecs.STRING_ARGB_VEC4_COLOR;
   }

   @Override
   default LerpFunction<Vector4fc> argumentKeyframeLerp(final EnvironmentAttribute<Subject> type) {
      return LerpFunction.ofColorVec4();
   }
}
