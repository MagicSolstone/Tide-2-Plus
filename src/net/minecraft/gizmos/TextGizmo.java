package net.minecraft.gizmos;

import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;

public record TextGizmo(Vec3 pos, String text, TextGizmo$Style style) implements Gizmo {
   @Override
   public void emit(final GizmoPrimitives primitives, final float alphaMultiplier) {
      TextGizmo$Style newStyle;
      if (alphaMultiplier < 1.0F) {
         newStyle = new TextGizmo$Style(ARGB.multiplyAlpha(this.style.color, alphaMultiplier), this.style.scale, this.style.adjustLeft);
      } else {
         newStyle = this.style;
      }

      primitives.addText(this.pos, this.text, newStyle);
   }
}
