package net.minecraft.world.attribute;

import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
public non-sealed interface EnvironmentAttributeLayer$Positional<Value> extends EnvironmentAttributeLayer<Value> {
   Value applyPositional(Value baseValue, Vec3 pos, @Nullable SpatialAttributeInterpolator biomeInterpolator);
}
