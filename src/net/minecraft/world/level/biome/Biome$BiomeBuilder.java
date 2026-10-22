package net.minecraft.world.level.biome;

import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributeMap$Builder;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.attribute.modifier.AttributeModifier;
import net.minecraft.world.attribute.modifier.MobSpawnSettingsModifier;
import org.jspecify.annotations.Nullable;

public class Biome$BiomeBuilder {
   private boolean hasPrecipitation = true;
   @Nullable
   private Float temperature;
   private Biome$TemperatureModifier temperatureModifier = Biome$TemperatureModifier.NONE;
   @Nullable
   private Float downfall;
   private final EnvironmentAttributeMap$Builder attributes = EnvironmentAttributeMap.builder();
   @Nullable
   private BiomeSpecialEffects specialEffects;
   @Nullable
   private BiomeGenerationSettings generationSettings;

   public Biome$BiomeBuilder hasPrecipitation(final boolean hasPrecipitation) {
      this.hasPrecipitation = hasPrecipitation;
      return this;
   }

   public Biome$BiomeBuilder temperature(final float temperature) {
      this.temperature = temperature;
      return this;
   }

   public Biome$BiomeBuilder downfall(final float downfall) {
      this.downfall = downfall;
      return this;
   }

   public Biome$BiomeBuilder putAttributes(final EnvironmentAttributeMap attributes) {
      this.attributes.putAll(attributes);
      return this;
   }

   public Biome$BiomeBuilder putAttributes(final EnvironmentAttributeMap$Builder attributes) {
      return this.putAttributes(attributes.build());
   }

   public <Value> Biome$BiomeBuilder setAttribute(final EnvironmentAttribute<Value> attribute, final Value value) {
      this.attributes.set(attribute, value);
      return this;
   }

   public <Value, Parameter> Biome$BiomeBuilder modifyAttribute(
      final EnvironmentAttribute<Value> attribute, final AttributeModifier<Value, Parameter> modifier, final Parameter value
   ) {
      this.attributes.modify(attribute, modifier, value);
      return this;
   }

   public Biome$BiomeBuilder specialEffects(final BiomeSpecialEffects specialEffects) {
      this.specialEffects = specialEffects;
      return this;
   }

   public Biome$BiomeBuilder mobSpawnSettings(final MobSpawnSettings mobSpawnSettings) {
      this.modifyAttribute(EnvironmentAttributes.NATURAL_MOB_SPAWNS, MobSpawnSettingsModifier.overlay(), mobSpawnSettings);
      return this;
   }

   public Biome$BiomeBuilder generationSettings(final BiomeGenerationSettings generationSettings) {
      this.generationSettings = generationSettings;
      return this;
   }

   public Biome$BiomeBuilder temperatureAdjustment(final Biome$TemperatureModifier temperatureModifier) {
      this.temperatureModifier = temperatureModifier;
      return this;
   }

   public Biome build() {
      if (this.temperature != null && this.downfall != null && this.specialEffects != null && this.generationSettings != null) {
         return new Biome(
            new Biome$ClimateSettings(this.hasPrecipitation, this.temperature, this.temperatureModifier, this.downfall),
            this.attributes.build(),
            this.specialEffects,
            this.generationSettings
         );
      } else {
         throw new IllegalStateException("You are missing parameters to build a proper biome\n" + this);
      }
   }

   @Override
   public String toString() {
      return "BiomeBuilder{\nhasPrecipitation="
         + this.hasPrecipitation
         + ",\ntemperature="
         + this.temperature
         + ",\ntemperatureModifier="
         + this.temperatureModifier
         + ",\ndownfall="
         + this.downfall
         + ",\nspecialEffects="
         + this.specialEffects
         + ",\ngenerationSettings="
         + this.generationSettings
         + ",\n}";
   }
}
