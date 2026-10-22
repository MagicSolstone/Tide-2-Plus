package net.minecraft.world.attribute.modifier;

import com.mojang.serialization.Codec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.LerpFunction;
import org.joml.Vector3fc;

public interface ColorModifier$RgbModifier<Subject> extends ColorModifier<Subject, Vector3fc> {
   @Override
   default Codec<Vector3fc> argumentCodec(final EnvironmentAttribute<Subject> type) {
      return ExtraCodecs.STRING_RGB_VEC3_COLOR;
   }

   @Override
   default LerpFunction<Vector3fc> argumentKeyframeLerp(final EnvironmentAttribute<Subject> type) {
      return LerpFunction.ofColorVec3();
   }
}
