package net.minecraft.gizmos;

import java.util.OptionalDouble;

public record TextGizmo$Style(int color, float scale, OptionalDouble adjustLeft) {
   public static final float DEFAULT_SCALE = 0.32F;

   public static TextGizmo$Style whiteAndCentered() {
      return new TextGizmo$Style(-1, 0.32F, OptionalDouble.empty());
   }

   public static TextGizmo$Style forColorAndCentered(final int argb) {
      return new TextGizmo$Style(argb, 0.32F, OptionalDouble.empty());
   }

   public static TextGizmo$Style forColor(final int argb) {
      return new TextGizmo$Style(argb, 0.32F, OptionalDouble.of(0.0));
   }

   public TextGizmo$Style withScale(final float scale) {
      return new TextGizmo$Style(this.color, scale, this.adjustLeft);
   }

   public TextGizmo$Style withLeftAlignment(final float adjustLeft) {
      return new TextGizmo$Style(this.color, this.scale, OptionalDouble.of(adjustLeft));
   }
}
