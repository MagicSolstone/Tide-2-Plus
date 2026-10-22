package net.minecraft.world.level.biome;

import com.google.common.annotations.VisibleForTesting;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.longs.Long2FloatLinkedOpenHashMap;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.level.DryFoliageColor;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.synth.Noise;
import net.minecraft.world.level.levelgen.synth.NoiseStack;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public final class Biome {
   public static final Codec<Biome> DIRECT_CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Biome$ClimateSettings.CODEC.forGetter(b -> b.climateSettings),
            EnvironmentAttributeMap.CODEC_ONLY_POSITIONAL.optionalFieldOf("attributes", EnvironmentAttributeMap.EMPTY).forGetter(b -> b.attributes),
            BiomeSpecialEffects.CODEC.fieldOf("effects").forGetter(b -> b.specialEffects),
            BiomeGenerationSettings.CODEC.forGetter(b -> b.generationSettings)
         )
         .apply(i, Biome::new)
   );
   public static final Codec<Biome> NETWORK_CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Biome$ClimateSettings.CODEC.forGetter(b -> b.climateSettings),
            EnvironmentAttributeMap.NETWORK_CODEC.optionalFieldOf("attributes", EnvironmentAttributeMap.EMPTY).forGetter(b -> b.attributes),
            BiomeSpecialEffects.CODEC.fieldOf("effects").forGetter(b -> b.specialEffects)
         )
         .apply(i, (climateSettings, attributes, specialEffects) -> new Biome(climateSettings, attributes, specialEffects, BiomeGenerationSettings.EMPTY))
   );
   public static final Codec<Holder<Biome>> CODEC = RegistryCodecs.holder(Registries.BIOME, DIRECT_CODEC);
   public static final Codec<HolderSet<Biome>> LIST_CODEC = RegistryCodecs.holderSet(Registries.BIOME, DIRECT_CODEC);
   private static final Noise TEMPERATURE_NOISE = new SimplexNoise(new WorldgenRandom(new LegacyRandomSource(1234L)), true);
   @VisibleForTesting
   public static final Noise FROZEN_TEMPERATURE_NOISE = Util.make(
      () -> {
         WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(3456L));
         return NoiseStack.builder()
            .add(new SimplexNoise(random, true), 1.0, 0.14285715F)
            .add(new SimplexNoise(random, true), 0.5, 0.2857143F)
            .add(new SimplexNoise(random, true), 0.25, 0.5714286F)
            .build();
      }
   );
   @Deprecated(
      forRemoval = true
   )
   public static final Noise BIOME_INFO_NOISE = new SimplexNoise(new WorldgenRandom(new LegacyRandomSource(2345L)), true);
   private static final int TEMPERATURE_CACHE_SIZE = 1024;
   private final Biome$ClimateSettings climateSettings;
   private final BiomeGenerationSettings generationSettings;
   private final EnvironmentAttributeMap attributes;
   private final BiomeSpecialEffects specialEffects;
   private final ThreadLocal<Long2FloatLinkedOpenHashMap> temperatureCache = ThreadLocal.withInitial(() -> {
      Long2FloatLinkedOpenHashMap map = new Biome$1(this, 1024, 0.25F);
      map.defaultReturnValue(Float.NaN);
      return map;
   });

   private Biome(
      final Biome$ClimateSettings climateSettings,
      final EnvironmentAttributeMap attributes,
      final BiomeSpecialEffects specialEffects,
      final BiomeGenerationSettings generationSettings
   ) {
      this.climateSettings = climateSettings;
      this.attributes = attributes;
      this.specialEffects = specialEffects;
      this.generationSettings = generationSettings;
   }

   public boolean hasPrecipitation() {
      return this.climateSettings.hasPrecipitation();
   }

   public Biome$Precipitation getPrecipitationAt(final BlockPos pos, final int seaLevel) {
      if (!this.hasPrecipitation()) {
         return Biome$Precipitation.NONE;
      } else {
         return this.coldEnoughToSnow(pos, seaLevel) ? Biome$Precipitation.SNOW : Biome$Precipitation.RAIN;
      }
   }

   private float getHeightAdjustedTemperature(final BlockPos pos, final int seaLevel) {
      float adjustedTemperature = this.climateSettings.temperatureModifier.modifyTemperature(pos, this.getBaseTemperature());
      int snowLevel = seaLevel + 17;
      if (pos.getY() > snowLevel) {
         float v = TEMPERATURE_NOISE.get(pos.getX() / 8.0F, pos.getZ() / 8.0F) * 8.0F;
         return adjustedTemperature - (v + pos.getY() - snowLevel) * 0.05F / 40.0F;
      } else {
         return adjustedTemperature;
      }
   }

   @Deprecated
   private float getTemperature(final BlockPos pos, final int seaLevel) {
      long key = pos.asLong();
      Long2FloatLinkedOpenHashMap cache = this.temperatureCache.get();
      float cached = cache.get(key);
      if (!Float.isNaN(cached)) {
         return cached;
      } else {
         float temp = this.getHeightAdjustedTemperature(pos, seaLevel);
         if (cache.size() == 1024) {
            cache.removeFirstFloat();
         }

         cache.put(key, temp);
         return temp;
      }
   }

   public boolean shouldFreeze(final LevelReader level, final BlockPos pos) {
      return this.shouldFreeze(level, pos, true);
   }

   public boolean shouldFreeze(final LevelReader level, final BlockPos pos, final boolean checkNeighbors) {
      if (this.warmEnoughToRain(pos, level.getSeaLevel())) {
         return false;
      } else {
         if (level.isInsideBuildHeight(pos.getY()) && level.getBrightness(LightLayer.BLOCK, pos) < 10) {
            BlockState blockState = level.getBlockState(pos);
            FluidState fluidState = level.getFluidState(pos);
            if (fluidState.is(Fluids.WATER) && blockState.getBlock() instanceof LiquidBlock) {
               if (!checkNeighbors) {
                  return true;
               }

               boolean surroundedByWater = level.isWaterAt(pos.west())
                  && level.isWaterAt(pos.east())
                  && level.isWaterAt(pos.north())
                  && level.isWaterAt(pos.south());
               if (!surroundedByWater) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public boolean coldEnoughToSnow(final BlockPos pos, final int seaLevel) {
      return !this.warmEnoughToRain(pos, seaLevel);
   }

   public boolean warmEnoughToRain(final BlockPos pos, final int seaLevel) {
      return this.getTemperature(pos, seaLevel) >= 0.15F;
   }

   public boolean shouldMeltFrozenOceanIcebergSlightly(final BlockPos pos, final int seaLevel) {
      return this.getTemperature(pos, seaLevel) > 0.1F;
   }

   public boolean shouldSnow(final LevelReader level, final BlockPos pos) {
      if (this.getPrecipitationAt(pos, level.getSeaLevel()) != Biome$Precipitation.SNOW) {
         return false;
      } else {
         if (level.isInsideBuildHeight(pos.getY()) && level.getBrightness(LightLayer.BLOCK, pos) < 10) {
            BlockState state = level.getBlockState(pos);
            if ((state.isAir() || state.is(Blocks.SNOW)) && Blocks.SNOW.defaultBlockState().canSurvive(level, pos)) {
               return true;
            }
         }

         return false;
      }
   }

   public BiomeGenerationSettings getGenerationSettings() {
      return this.generationSettings;
   }

   public int getGrassColor(final double x, final double z) {
      int baseGrassColor = this.getBaseGrassColor();
      return this.specialEffects.grassColorModifier().modifyColor(x, z, baseGrassColor);
   }

   private int getBaseGrassColor() {
      Optional<Integer> colorOverride = this.specialEffects.grassColorOverride();
      return colorOverride.isPresent() ? colorOverride.get() : this.getGrassColorFromTexture();
   }

   private int getGrassColorFromTexture() {
      double temp = Mth.clamp(this.climateSettings.temperature, 0.0F, 1.0F);
      double rain = Mth.clamp(this.climateSettings.downfall, 0.0F, 1.0F);
      return GrassColor.get(temp, rain);
   }

   public int getFoliageColor() {
      return this.specialEffects.foliageColorOverride().orElseGet(this::getFoliageColorFromTexture);
   }

   private int getFoliageColorFromTexture() {
      double temp = Mth.clamp(this.climateSettings.temperature, 0.0F, 1.0F);
      double rain = Mth.clamp(this.climateSettings.downfall, 0.0F, 1.0F);
      return FoliageColor.get(temp, rain);
   }

   public int getDryFoliageColor() {
      return this.specialEffects.dryFoliageColorOverride().orElseGet(this::getDryFoliageColorFromTexture);
   }

   private int getDryFoliageColorFromTexture() {
      double temp = Mth.clamp(this.climateSettings.temperature, 0.0F, 1.0F);
      double rain = Mth.clamp(this.climateSettings.downfall, 0.0F, 1.0F);
      return DryFoliageColor.get(temp, rain);
   }

   public float getBaseTemperature() {
      return this.climateSettings.temperature;
   }

   public EnvironmentAttributeMap getAttributes() {
      return this.attributes;
   }

   public BiomeSpecialEffects getSpecialEffects() {
      return this.specialEffects;
   }

   public int getWaterColor() {
      return this.specialEffects.waterColor();
   }
}
