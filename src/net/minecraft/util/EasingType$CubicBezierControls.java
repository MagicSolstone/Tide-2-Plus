package net.minecraft.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.List;

public record EasingType$CubicBezierControls(float x1, float y1, float x2, float y2) {
   public static final Codec<EasingType$CubicBezierControls> CODEC = Codec.FLOAT
      .listOf(4, 4)
      .xmap(
         floats -> new EasingType$CubicBezierControls((Float)floats.get(0), (Float)floats.get(1), (Float)floats.get(2), (Float)floats.get(3)),
         controls -> List.of(controls.x1, controls.y1, controls.x2, controls.y2)
      )
      .validate(EasingType$CubicBezierControls::validate);

   private DataResult<EasingType$CubicBezierControls> validate() {
      if (this.x1 < 0.0F || this.x1 > 1.0F) {
         return DataResult.error(() -> "x1 must be in range [0; 1]");
      } else {
         return !(this.x2 < 0.0F) && !(this.x2 > 1.0F) ? DataResult.success(this) : DataResult.error(() -> "x2 must be in range [0; 1]");
      }
   }
}
