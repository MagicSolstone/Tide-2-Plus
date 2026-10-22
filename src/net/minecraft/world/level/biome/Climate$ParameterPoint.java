package net.minecraft.world.level.biome;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.util.Mth;

public record Climate$ParameterPoint(
   Climate$Parameter temperature,
   Climate$Parameter humidity,
   Climate$Parameter continentalness,
   Climate$Parameter erosion,
   Climate$Parameter depth,
   Climate$Parameter weirdness,
   long offset
) {
   public static final Codec<Climate$ParameterPoint> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Climate$Parameter.CODEC.fieldOf("temperature").forGetter(p -> p.temperature),
            Climate$Parameter.CODEC.fieldOf("humidity").forGetter(p -> p.humidity),
            Climate$Parameter.CODEC.fieldOf("continentalness").forGetter(p -> p.continentalness),
            Climate$Parameter.CODEC.fieldOf("erosion").forGetter(p -> p.erosion),
            Climate$Parameter.CODEC.fieldOf("depth").forGetter(p -> p.depth),
            Climate$Parameter.CODEC.fieldOf("weirdness").forGetter(p -> p.weirdness),
            Codec.floatRange(0.0F, 1.0F).fieldOf("offset").xmap(Climate::quantizeCoord, Climate::unquantizeCoord).forGetter(p -> p.offset)
         )
         .apply(i, Climate$ParameterPoint::new)
   );

   public long fitness(final Climate$TargetPoint target) {
      return Mth.square(this.temperature.distance(target.temperature))
         + Mth.square(this.humidity.distance(target.humidity))
         + Mth.square(this.continentalness.distance(target.continentalness))
         + Mth.square(this.erosion.distance(target.erosion))
         + Mth.square(this.depth.distance(target.depth))
         + Mth.square(this.weirdness.distance(target.weirdness))
         + Mth.square(this.offset);
   }

   List<Climate$Parameter> parameterSpace() {
      return ImmutableList.of(
         this.temperature, this.humidity, this.continentalness, this.erosion, this.depth, this.weirdness, new Climate$Parameter(this.offset, this.offset)
      );
   }
}
