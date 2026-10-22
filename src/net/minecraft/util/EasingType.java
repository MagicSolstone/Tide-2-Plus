package net.minecraft.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;

public interface EasingType {
   ExtraCodecs$LateBoundIdMapper<String, EasingType> SIMPLE_REGISTRY = new ExtraCodecs$LateBoundIdMapper<>();
   Codec<EasingType> CODEC = Codec.either(SIMPLE_REGISTRY.codec(Codec.STRING), EasingType$CubicBezier.CODEC)
      .xmap(Either::unwrap, easing -> easing instanceof EasingType$CubicBezier bezier ? Either.right(bezier) : Either.left(easing));
   EasingType CONSTANT = registerSimple("constant", x -> 0.0F);
   EasingType LINEAR = registerSimple("linear", x -> x);
   EasingType IN_BACK = registerSimple("in_back", Ease::inBack);
   EasingType IN_BOUNCE = registerSimple("in_bounce", Ease::inBounce);
   EasingType IN_CIRC = registerSimple("in_circ", Ease::inCirc);
   EasingType IN_CUBIC = registerSimple("in_cubic", Ease::inCubic);
   EasingType IN_ELASTIC = registerSimple("in_elastic", Ease::inElastic);
   EasingType IN_EXPO = registerSimple("in_expo", Ease::inExpo);
   EasingType IN_QUAD = registerSimple("in_quad", Ease::inQuad);
   EasingType IN_QUART = registerSimple("in_quart", Ease::inQuart);
   EasingType IN_QUINT = registerSimple("in_quint", Ease::inQuint);
   EasingType IN_SINE = registerSimple("in_sine", Ease::inSine);
   EasingType IN_OUT_BACK = registerSimple("in_out_back", Ease::inOutBack);
   EasingType IN_OUT_BOUNCE = registerSimple("in_out_bounce", Ease::inOutBounce);
   EasingType IN_OUT_CIRC = registerSimple("in_out_circ", Ease::inOutCirc);
   EasingType IN_OUT_CUBIC = registerSimple("in_out_cubic", Ease::inOutCubic);
   EasingType IN_OUT_ELASTIC = registerSimple("in_out_elastic", Ease::inOutElastic);
   EasingType IN_OUT_EXPO = registerSimple("in_out_expo", Ease::inOutExpo);
   EasingType IN_OUT_QUAD = registerSimple("in_out_quad", Ease::inOutQuad);
   EasingType IN_OUT_QUART = registerSimple("in_out_quart", Ease::inOutQuart);
   EasingType IN_OUT_QUINT = registerSimple("in_out_quint", Ease::inOutQuint);
   EasingType IN_OUT_SINE = registerSimple("in_out_sine", Ease::inOutSine);
   EasingType OUT_BACK = registerSimple("out_back", Ease::outBack);
   EasingType OUT_BOUNCE = registerSimple("out_bounce", Ease::outBounce);
   EasingType OUT_CIRC = registerSimple("out_circ", Ease::outCirc);
   EasingType OUT_CUBIC = registerSimple("out_cubic", Ease::outCubic);
   EasingType OUT_ELASTIC = registerSimple("out_elastic", Ease::outElastic);
   EasingType OUT_EXPO = registerSimple("out_expo", Ease::outExpo);
   EasingType OUT_QUAD = registerSimple("out_quad", Ease::outQuad);
   EasingType OUT_QUART = registerSimple("out_quart", Ease::outQuart);
   EasingType OUT_QUINT = registerSimple("out_quint", Ease::outQuint);
   EasingType OUT_SINE = registerSimple("out_sine", Ease::outSine);

   static EasingType registerSimple(final String id, final EasingType easing) {
      SIMPLE_REGISTRY.put(id, easing);
      return easing;
   }

   static EasingType cubicBezier(final float x1, final float y1, final float x2, final float y2) {
      return new EasingType$CubicBezier(new EasingType$CubicBezierControls(x1, y1, x2, y2));
   }

   static EasingType symmetricCubicBezier(final float x1, final float y1) {
      return cubicBezier(x1, y1, 1.0F - x1, 1.0F - y1);
   }

   float apply(float x);
}
