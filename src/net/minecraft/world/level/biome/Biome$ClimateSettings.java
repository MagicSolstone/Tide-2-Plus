package net.minecraft.world.level.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

record Biome$ClimateSettings(boolean hasPrecipitation, float temperature, Biome$TemperatureModifier temperatureModifier, float downfall) {
   public static final MapCodec<Biome$ClimateSettings> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            Codec.BOOL.fieldOf("has_precipitation").forGetter(b -> b.hasPrecipitation),
            Codec.FLOAT.fieldOf("temperature").forGetter(b -> b.temperature),
            Biome$TemperatureModifier.CODEC.optionalFieldOf("temperature_modifier", Biome$TemperatureModifier.NONE).forGetter(b -> b.temperatureModifier),
            Codec.FLOAT.fieldOf("downfall").forGetter(b -> b.downfall)
         )
         .apply(i, Biome$ClimateSettings::new)
   );
}
