package net.minecraft.world.attribute;

import com.google.common.annotations.VisibleForTesting;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import net.minecraft.SharedConstants;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Mth;
import net.minecraft.world.clock.ClockManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.timeline.Timeline;
import org.jspecify.annotations.Nullable;

public class EnvironmentAttributeSystem implements EnvironmentAttributeReader {
   private final Map<EnvironmentAttribute<?>, EnvironmentAttributeSystem$ValueSampler<?>> attributeSamplers = new Reference2ObjectOpenHashMap();

   private EnvironmentAttributeSystem(final Map<EnvironmentAttribute<?>, List<EnvironmentAttributeLayer<?>>> layersByAttribute) {
      layersByAttribute.forEach(
         (attribute, layers) -> this.attributeSamplers
            .put(
               (EnvironmentAttribute<?>)attribute,
               this.bakeLayerSampler((EnvironmentAttribute<?>)attribute, (List<? extends EnvironmentAttributeLayer<?>>)layers)
            )
      );
   }

   private <Value> EnvironmentAttributeSystem$ValueSampler<Value> bakeLayerSampler(
      final EnvironmentAttribute<Value> attribute, final List<? extends EnvironmentAttributeLayer<?>> untypedLayers
   ) {
      List<EnvironmentAttributeLayer<Value>> layers = new ArrayList<>((Collection<? extends EnvironmentAttributeLayer<Value>>)untypedLayers);
      Value constantBaseValue = attribute.defaultValue();

      while (!layers.isEmpty()) {
         if (!(layers.getFirst() instanceof EnvironmentAttributeLayer$Constant<Value> constantLayer)) {
            break;
         }

         constantBaseValue = constantLayer.applyConstant(constantBaseValue);
         layers.removeFirst();
      }

      boolean isAffectedByPosition = layers.stream().anyMatch(layer -> layer instanceof EnvironmentAttributeLayer$Positional);
      return new EnvironmentAttributeSystem$ValueSampler<>(attribute, constantBaseValue, List.copyOf(layers), isAffectedByPosition);
   }

   public static EnvironmentAttributeSystem$Builder builder() {
      return new EnvironmentAttributeSystem$Builder();
   }

   private static void addStaticLayers(final EnvironmentAttributeSystem$Builder builder, final LevelAccessor level) {
      RegistryAccess registries = level.registryAccess();
      BiomeManager biomeManager = level.getBiomeManager();
      addDimensionLayer(builder, level.dimensionType());
      addBiomeLayer(builder, registries.lookupOrThrow(Registries.BIOME), biomeManager);
   }

   private static void addDynamicLayers(final EnvironmentAttributeSystem$Builder builder, final Level level) {
      ClockManager clockManager = level.clockManager();
      level.dimensionType().timelines().forEach(timeline -> builder.addTimelineLayer((Holder<Timeline>)timeline, clockManager));
      if (level.canHaveWeather()) {
         WeatherAttributes.addBuiltinLayers(builder, WeatherAttributes$WeatherAccess.from(level));
      }
   }

   private static void addDimensionLayer(final EnvironmentAttributeSystem$Builder builder, final DimensionType dimensionType) {
      builder.addConstantLayer(dimensionType.attributes());
   }

   private static void addBiomeLayer(final EnvironmentAttributeSystem$Builder builder, final HolderLookup<Biome> biomes, final BiomeManager biomeManager) {
      Stream<EnvironmentAttribute<?>> attributesProvidedByBiomes = biomes.listElements()
         .flatMap(biome -> biome.value().getAttributes().keySet().stream())
         .distinct();
      attributesProvidedByBiomes.forEach(attribute -> addBiomeLayerForAttribute(builder, (EnvironmentAttribute<?>)attribute, biomeManager));
   }

   private static <Value> void addBiomeLayerForAttribute(
      final EnvironmentAttributeSystem$Builder builder, final EnvironmentAttribute<Value> attribute, final BiomeManager biomeManager
   ) {
      builder.addPositionalLayer(
         attribute,
         (baseValue, pos, biomeWeights) -> {
            if (biomeWeights != null && attribute.isSpatiallyInterpolated()) {
               return biomeWeights.applyAttributeLayer(attribute, baseValue);
            } else {
               Holder<Biome> biome = attribute.isFullResolutionBiomes()
                  ? biomeManager.getBiome(Mth.floor(pos.x), Mth.floor(pos.y), Mth.floor(pos.z))
                  : biomeManager.getNoiseBiomeAtPosition(pos.x, pos.y, pos.z);
               return biome.value().getAttributes().applyModifier(attribute, baseValue);
            }
         }
      );
   }

   public void invalidateTickCache() {
      this.attributeSamplers.values().forEach(EnvironmentAttributeSystem$ValueSampler::invalidateTickCache);
   }

   @Nullable
   private <Value> EnvironmentAttributeSystem$ValueSampler<Value> getValueSampler(final EnvironmentAttribute<Value> attribute) {
      return (EnvironmentAttributeSystem$ValueSampler<Value>)this.attributeSamplers.get(attribute);
   }

   @Override
   public <Value> Value getDimensionValue(final EnvironmentAttribute<Value> attribute) {
      if (SharedConstants.IS_RUNNING_IN_IDE && attribute.isPositional()) {
         throw new IllegalStateException("Position must always be provided for positional attribute " + attribute);
      } else {
         EnvironmentAttributeSystem$ValueSampler<Value> sampler = this.getValueSampler(attribute);
         return sampler == null ? attribute.defaultValue() : sampler.getDimensionValue();
      }
   }

   @Override
   public <Value> Value getValue(final EnvironmentAttribute<Value> attribute, final Vec3 pos, @Nullable final SpatialAttributeInterpolator biomeInterpolator) {
      EnvironmentAttributeSystem$ValueSampler<Value> sampler = this.getValueSampler(attribute);
      return sampler == null ? attribute.defaultValue() : sampler.getValue(pos, biomeInterpolator);
   }

   @VisibleForTesting
   <Value> Value getConstantBaseValue(final EnvironmentAttribute<Value> attribute) {
      EnvironmentAttributeSystem$ValueSampler<Value> sampler = this.getValueSampler(attribute);
      return sampler != null ? sampler.baseValue : attribute.defaultValue();
   }

   @VisibleForTesting
   boolean isAffectedByPosition(final EnvironmentAttribute<?> attribute) {
      EnvironmentAttributeSystem$ValueSampler<?> sampler = this.getValueSampler(attribute);
      return sampler != null && sampler.isAffectedByPosition;
   }
}
