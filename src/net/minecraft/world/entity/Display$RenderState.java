package net.minecraft.world.entity;

import com.mojang.math.Transformation;

public record Display$RenderState(
   Display$GenericInterpolator<Transformation> transformation,
   Display$BillboardConstraints billboardConstraints,
   int brightnessOverride,
   Display$FloatInterpolator shadowRadius,
   Display$FloatInterpolator shadowStrength,
   int glowColorOverride
) {
}
