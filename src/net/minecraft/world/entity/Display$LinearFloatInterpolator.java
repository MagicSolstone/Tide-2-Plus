package net.minecraft.world.entity;

import net.minecraft.util.Mth;

record Display$LinearFloatInterpolator(float previous, float current) implements Display$FloatInterpolator {
   @Override
   public float get(final float progress) {
      return Mth.lerp(progress, this.previous, this.current);
   }
}
