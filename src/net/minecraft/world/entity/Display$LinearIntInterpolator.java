package net.minecraft.world.entity;

import net.minecraft.util.Mth;

record Display$LinearIntInterpolator(int previous, int current) implements Display$IntInterpolator {
   @Override
   public int get(final float progress) {
      return Mth.lerpInt(progress, this.previous, this.current);
   }
}
