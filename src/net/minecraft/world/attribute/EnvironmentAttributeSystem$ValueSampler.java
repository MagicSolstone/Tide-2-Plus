package net.minecraft.world.attribute;

import java.util.List;
import java.util.Objects;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

class EnvironmentAttributeSystem$ValueSampler<Value> {
   private final EnvironmentAttribute<Value> attribute;
   private final Value baseValue;
   private final List<EnvironmentAttributeLayer<Value>> layers;
   private final boolean isAffectedByPosition;
   @Nullable
   private Value cachedTickValue;
   private int cacheTickId;

   private EnvironmentAttributeSystem$ValueSampler(
      final EnvironmentAttribute<Value> attribute,
      final Value baseValue,
      final List<EnvironmentAttributeLayer<Value>> layers,
      final boolean isAffectedByPosition
   ) {
      this.attribute = attribute;
      this.baseValue = baseValue;
      this.layers = layers;
      this.isAffectedByPosition = isAffectedByPosition;
   }

   public void invalidateTickCache() {
      this.cachedTickValue = null;
      this.cacheTickId++;
   }

   public Value getDimensionValue() {
      if (this.cachedTickValue != null) {
         return this.cachedTickValue;
      } else {
         Value result = this.computeValueNotPositional();
         this.cachedTickValue = result;
         return result;
      }
   }

   public Value getValue(final Vec3 pos, @Nullable final SpatialAttributeInterpolator biomeInterpolator) {
      return !this.isAffectedByPosition ? this.getDimensionValue() : this.computeValuePositional(pos, biomeInterpolator);
   }

   private Value computeValuePositional(final Vec3 pos, @Nullable final SpatialAttributeInterpolator biomeInterpolator) {
      Value result = this.baseValue;

      for (EnvironmentAttributeLayer<Value> layer : this.layers) {
         result = (Value)(switch (layer) {
            case EnvironmentAttributeLayer$Constant<Value> constantLayer -> constantLayer.applyConstant(result);
            case EnvironmentAttributeLayer$TimeBased<Value> timeBasedLayer -> timeBasedLayer.applyTimeBased(result, this.cacheTickId);
            case EnvironmentAttributeLayer$Positional<Value> positionalLayer -> positionalLayer.applyPositional(
               result, Objects.requireNonNull(pos), biomeInterpolator
            );
            default -> throw new MatchException(null, null);
         });
      }

      return this.attribute.sanitizeValue(result);
   }

   private Value computeValueNotPositional() {
      Value result = this.baseValue;

      for (EnvironmentAttributeLayer<Value> layer : this.layers) {
         result = (Value)(switch (layer) {
            case EnvironmentAttributeLayer$Constant<Value> constantLayer -> constantLayer.applyConstant(result);
            case EnvironmentAttributeLayer$TimeBased<Value> timeBasedLayer -> timeBasedLayer.applyTimeBased(result, this.cacheTickId);
            case EnvironmentAttributeLayer$Positional<Value> ignored -> result;
            default -> throw new MatchException(null, null);
         });
      }

      return this.attribute.sanitizeValue(result);
   }
}
