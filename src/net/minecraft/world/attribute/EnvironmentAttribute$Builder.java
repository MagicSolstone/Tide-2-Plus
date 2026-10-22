package net.minecraft.world.attribute;

import java.util.Objects;
import org.jspecify.annotations.Nullable;

public class EnvironmentAttribute$Builder<Value> {
   private final AttributeType<Value> type;
   @Nullable
   private Value defaultValue;
   private AttributeRange<Value> valueRange = AttributeRange.any();
   private boolean isSyncable = false;
   private boolean isPositional = true;
   private boolean isSpatiallyInterpolated = false;
   private boolean fullResolutionBiomes = false;

   public EnvironmentAttribute$Builder(final AttributeType<Value> type) {
      this.type = type;
   }

   public EnvironmentAttribute$Builder<Value> defaultValue(final Value defaultValue) {
      this.defaultValue = defaultValue;
      return this;
   }

   public EnvironmentAttribute$Builder<Value> valueRange(final AttributeRange<Value> valueRange) {
      this.valueRange = valueRange;
      return this;
   }

   public EnvironmentAttribute$Builder<Value> syncable() {
      this.isSyncable = true;
      return this;
   }

   public EnvironmentAttribute$Builder<Value> notPositional() {
      this.isPositional = false;
      return this;
   }

   public EnvironmentAttribute$Builder<Value> spatiallyInterpolated() {
      this.isSpatiallyInterpolated = true;
      return this;
   }

   @Deprecated
   public EnvironmentAttribute$Builder<Value> fullResolutionBiomes() {
      this.fullResolutionBiomes = true;
      return this;
   }

   public EnvironmentAttribute<Value> build() {
      return new EnvironmentAttribute<>(
         this.type,
         Objects.requireNonNull(this.defaultValue, "Missing default value"),
         this.valueRange,
         this.isSyncable,
         this.isPositional,
         this.isSpatiallyInterpolated,
         this.fullResolutionBiomes
      );
   }
}
