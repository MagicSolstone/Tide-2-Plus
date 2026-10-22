package net.minecraft.world.attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.world.clock.ClockManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.timeline.Timeline;

public class EnvironmentAttributeSystem$Builder {
   private final Map<EnvironmentAttribute<?>, List<EnvironmentAttributeLayer<?>>> layersByAttribute = new HashMap<>();

   private EnvironmentAttributeSystem$Builder() {
   }

   public EnvironmentAttributeSystem$Builder addDefaultLayers(final Level level) {
      this.addStaticLayers(level);
      this.addDynamicLayers(level);
      return this;
   }

   public EnvironmentAttributeSystem$Builder addStaticLayers(final LevelAccessor level) {
      EnvironmentAttributeSystem.addStaticLayers(this, level);
      return this;
   }

   public EnvironmentAttributeSystem$Builder addDynamicLayers(final Level level) {
      EnvironmentAttributeSystem.addDynamicLayers(this, level);
      return this;
   }

   public EnvironmentAttributeSystem$Builder addConstantLayer(final EnvironmentAttributeMap attributeMap) {
      for (EnvironmentAttribute<?> attribute : attributeMap.keySet()) {
         this.addConstantEntry(attribute, attributeMap);
      }

      return this;
   }

   private <Value> EnvironmentAttributeSystem$Builder addConstantEntry(final EnvironmentAttribute<Value> attribute, final EnvironmentAttributeMap attributeMap) {
      EnvironmentAttributeMap$Entry<Value, ?> entry = attributeMap.get(attribute);
      if (entry == null) {
         throw new IllegalArgumentException("Missing attribute " + attribute);
      } else {
         return this.addConstantLayer(attribute, entry::applyModifier);
      }
   }

   public <Value> EnvironmentAttributeSystem$Builder addConstantLayer(
      final EnvironmentAttribute<Value> attribute, final EnvironmentAttributeLayer$Constant<Value> layer
   ) {
      return this.addLayer(attribute, layer);
   }

   public <Value> EnvironmentAttributeSystem$Builder addTimeBasedLayer(
      final EnvironmentAttribute<Value> attribute, final EnvironmentAttributeLayer$TimeBased<Value> layer
   ) {
      return this.addLayer(attribute, layer);
   }

   public <Value> EnvironmentAttributeSystem$Builder addPositionalLayer(
      final EnvironmentAttribute<Value> attribute, final EnvironmentAttributeLayer$Positional<Value> layer
   ) {
      return this.addLayer(attribute, layer);
   }

   private <Value> EnvironmentAttributeSystem$Builder addLayer(final EnvironmentAttribute<Value> attribute, final EnvironmentAttributeLayer<Value> layer) {
      this.layersByAttribute.computeIfAbsent(attribute, t -> new ArrayList<>()).add(layer);
      return this;
   }

   public EnvironmentAttributeSystem$Builder addTimelineLayer(final Holder<Timeline> timeline, final ClockManager clockManager) {
      for (EnvironmentAttribute<?> attribute : timeline.value().attributes()) {
         this.addTimelineLayerForAttribute(timeline, attribute, clockManager);
      }

      return this;
   }

   private <Value> void addTimelineLayerForAttribute(
      final Holder<Timeline> timeline, final EnvironmentAttribute<Value> attribute, final ClockManager clockManager
   ) {
      this.addTimeBasedLayer(attribute, timeline.value().createTrackSampler(attribute, clockManager));
   }

   public EnvironmentAttributeSystem build() {
      return new EnvironmentAttributeSystem(this.layersByAttribute);
   }
}
