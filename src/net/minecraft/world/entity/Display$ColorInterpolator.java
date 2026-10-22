package net.minecraft.world.entity;

import net.minecraft.util.ARGB;

record Display$ColorInterpolator(int previous, int current) implements Display$IntInterpolator {
   @Override
   public int get(final float progress) {
      return ARGB.srgbLerp(progress, this.previous, this.current);
   }
}
