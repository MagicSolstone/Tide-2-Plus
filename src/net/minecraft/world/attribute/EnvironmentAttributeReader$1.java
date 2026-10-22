package net.minecraft.world.attribute;

import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

class EnvironmentAttributeReader$1 implements EnvironmentAttributeReader {
   @Override
   public <Value> Value getDimensionValue(final EnvironmentAttribute<Value> attribute) {
      return attribute.defaultValue();
   }

   @Override
   public <Value> Value getValue(final EnvironmentAttribute<Value> attribute, final Vec3 pos, @Nullable final SpatialAttributeInterpolator biomeInterpolator) {
      return attribute.defaultValue();
   }
}
