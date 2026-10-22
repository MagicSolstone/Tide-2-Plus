package net.minecraft.world.level.levelgen.carver;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.FloatProviders;

public record CanyonWorldCarver$Shape(
   FloatProvider distanceFactor,
   FloatProvider thickness,
   int widthSmoothness,
   FloatProvider horizontalRadiusFactor,
   float verticalRadiusDefaultFactor,
   float verticalRadiusCenterFactor,
   FloatProvider yScale
) {
   public static final Codec<CanyonWorldCarver$Shape> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            FloatProviders.CODEC.fieldOf("distance_factor").forGetter(c -> c.distanceFactor),
            FloatProviders.CODEC.fieldOf("thickness").forGetter(c -> c.thickness),
            ExtraCodecs.POSITIVE_INT.fieldOf("width_smoothness").forGetter(c -> c.widthSmoothness),
            FloatProviders.CODEC.fieldOf("horizontal_radius_factor").forGetter(c -> c.horizontalRadiusFactor),
            Codec.FLOAT.fieldOf("vertical_radius_default_factor").forGetter(c -> c.verticalRadiusDefaultFactor),
            Codec.FLOAT.fieldOf("vertical_radius_center_factor").forGetter(c -> c.verticalRadiusCenterFactor),
            FloatProviders.CODEC.fieldOf("y_scale").forGetter(c -> c.yScale)
         )
         .apply(i, CanyonWorldCarver$Shape::new)
   );
}
