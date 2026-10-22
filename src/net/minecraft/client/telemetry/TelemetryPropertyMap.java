package net.minecraft.client.telemetry;

import com.mojang.serialization.MapCodec;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class TelemetryPropertyMap {
   private final Map<TelemetryProperty<?>, Object> entries;

   private TelemetryPropertyMap(final Map<TelemetryProperty<?>, Object> entries) {
      this.entries = entries;
   }

   public static TelemetryPropertyMap$Builder builder() {
      return new TelemetryPropertyMap$Builder();
   }

   public static MapCodec<TelemetryPropertyMap> createCodec(final List<TelemetryProperty<?>> properties) {
      return new TelemetryPropertyMap$1(properties);
   }

   @Nullable
   public <T> T get(final TelemetryProperty<T> property) {
      return (T)this.entries.get(property);
   }

   @Override
   public String toString() {
      return this.entries.toString();
   }

   public Set<TelemetryProperty<?>> propertySet() {
      return this.entries.keySet();
   }
}
