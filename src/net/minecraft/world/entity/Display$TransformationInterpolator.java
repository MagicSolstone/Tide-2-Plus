package net.minecraft.world.entity;

import com.mojang.math.Transformation;

record Display$TransformationInterpolator(Transformation previous, Transformation current) implements Display$GenericInterpolator<Transformation> {
   public Transformation get(final float progress) {
      return progress >= 1.0 ? this.current : this.previous.slerp(this.current, progress);
   }
}
