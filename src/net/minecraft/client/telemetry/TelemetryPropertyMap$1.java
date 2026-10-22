package net.minecraft.client.telemetry;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import java.util.List;
import java.util.stream.Stream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class TelemetryPropertyMap$1 extends MapCodec<TelemetryPropertyMap> {
   TelemetryPropertyMap$1(final List val$properties) {
      this.val$properties = val$propertiesx;
   }

   public <T> RecordBuilder<T> encode(final TelemetryPropertyMap input, final DynamicOps<T> ops, final RecordBuilder<T> prefix) {
      RecordBuilder<T> result = prefix;

      for (TelemetryProperty<?> property : this.val$properties) {
         result = this.encodeProperty(input, result, property);
      }

      return result;
   }

   private <T, V> RecordBuilder<T> encodeProperty(final TelemetryPropertyMap input, final RecordBuilder<T> result, final TelemetryProperty<V> property) {
      V value = input.get(property);
      return value != null ? result.add(property.id(), value, property.codec()) : result;
   }

   public <T> DataResult<TelemetryPropertyMap> decode(final DynamicOps<T> ops, final MapLike<T> input) {
      DataResult<TelemetryPropertyMap$Builder> result = DataResult.success(new TelemetryPropertyMap$Builder());

      for (TelemetryProperty<?> property : this.val$properties) {
         result = this.decodeProperty(result, ops, input, property);
      }

      return result.map(TelemetryPropertyMap$Builder::build);
   }

   private <T, V> DataResult<TelemetryPropertyMap$Builder> decodeProperty(
      final DataResult<TelemetryPropertyMap$Builder> result, final DynamicOps<T> ops, final MapLike<T> input, final TelemetryProperty<V> property
   ) {
      T value = (T)input.get(property.id());
      if (value != null) {
         DataResult<V> parse = property.codec().parse(ops, value);
         return result.apply2stable((b, v) -> b.put(property, (V)v), parse);
      } else {
         return result;
      }
   }

   public <T> Stream<T> keys(final DynamicOps<T> ops) {
      return this.val$properties.stream().map(TelemetryProperty::id).map(ops::createString);
   }
}
