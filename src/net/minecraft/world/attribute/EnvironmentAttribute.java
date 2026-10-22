package net.minecraft.world.attribute;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Util;

public class EnvironmentAttribute<Value> {
   private final AttributeType<Value> type;
   private final Value defaultValue;
   private final AttributeRange<Value> valueRange;
   private final boolean isSyncable;
   private final boolean isPositional;
   private final boolean isSpatiallyInterpolated;
   private final boolean fullResolutionBiomes;

   private EnvironmentAttribute(
      final AttributeType<Value> type,
      final Value defaultValue,
      final AttributeRange<Value> valueRange,
      final boolean isSyncable,
      final boolean isPositional,
      final boolean isSpatiallyInterpolated,
      final boolean fullResolutionBiomes
   ) {
      this.type = type;
      this.defaultValue = defaultValue;
      this.valueRange = valueRange;
      this.isSyncable = isSyncable;
      this.isPositional = isPositional;
      this.isSpatiallyInterpolated = isSpatiallyInterpolated;
      this.fullResolutionBiomes = fullResolutionBiomes;
   }

   public static <Value> EnvironmentAttribute$Builder<Value> builder(final AttributeType<Value> type) {
      return new EnvironmentAttribute$Builder<>(type);
   }

   public AttributeType<Value> type() {
      return this.type;
   }

   public Value defaultValue() {
      return this.defaultValue;
   }

   public Codec<Value> valueCodec() {
      return this.type.valueCodec().validate(this.valueRange::validate);
   }

   public Value sanitizeValue(final Value value) {
      return this.valueRange.sanitize(value);
   }

   public boolean isSyncable() {
      return this.isSyncable;
   }

   public boolean isPositional() {
      return this.isPositional;
   }

   public boolean isSpatiallyInterpolated() {
      return this.isSpatiallyInterpolated;
   }

   public boolean isFullResolutionBiomes() {
      return this.fullResolutionBiomes;
   }

   @Override
   public String toString() {
      return Util.getRegisteredName(BuiltInRegistries.ENVIRONMENT_ATTRIBUTE, this);
   }
}
