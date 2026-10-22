package net.minecraft.world.attribute;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.List;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.joml.Vector3fc;
import org.joml.Vector4fc;

public interface LerpFunction<T> {
   LerpFunction<?> CONSTANT = ofStep(1.0F);

   static LerpFunction<Float> ofFloat() {
      return Mth::lerp;
   }

   static LerpFunction<Integer> ofInteger() {
      return Mth::lerpInt;
   }

   static LerpFunction<Float> ofDegrees(final float maxDelta) {
      return (alpha, from, to) -> {
         float delta = Mth.wrapDegrees(to - from);
         return Math.abs(delta) >= maxDelta ? to : from + alpha * delta;
      };
   }

   static <T> LerpFunction<T> ofConstant() {
      return (LerpFunction<T>)CONSTANT;
   }

   static <T> LerpFunction<T> ofStep(final float threshold) {
      return (alpha, from, to) -> alpha >= threshold ? to : from;
   }

   static LerpFunction<Integer> ofColor() {
      return ARGB::srgbLerp;
   }

   static LerpFunction<Vector3fc> ofColorVec3() {
      return ARGB::srgbLerp;
   }

   static LerpFunction<Vector4fc> ofColorVec4() {
      return ARGB::srgbLerp;
   }

   static <T> LerpFunction<List<T>> ofListCrossFade(final LerpFunction$AlphaScaler<T> scaler) {
      return (alpha, from, to) -> {
         if (alpha == 0.0F) {
            return from;
         } else if (alpha == 1.0F) {
            return to;
         } else {
            Builder<T> builder = ImmutableList.builderWithExpectedSize(from.size() + to.size());

            for (T element : from) {
               builder.add(scaler.apply(element, 1.0F - alpha));
            }

            for (T element : to) {
               builder.add(scaler.apply(element, alpha));
            }

            return builder.build();
         }
      };
   }

   T apply(float alpha, T from, T to);
}
