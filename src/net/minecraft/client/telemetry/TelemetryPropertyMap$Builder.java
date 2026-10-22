package net.minecraft.client.telemetry;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class TelemetryPropertyMap$Builder {
   private final Map<TelemetryProperty<?>, Object> entries = new Reference2ObjectOpenHashMap();

   private TelemetryPropertyMap$Builder() {
   }

   public <T> TelemetryPropertyMap$Builder put(final TelemetryProperty<T> property, final T value) {
      this.entries.put(property, value);
      return this;
   }

   public <T> TelemetryPropertyMap$Builder putIfNotNull(final TelemetryProperty<T> property, @Nullable final T value) {
      if (value != null) {
         this.entries.put(property, value);
      }

      return this;
   }

   public TelemetryPropertyMap$Builder putAll(final TelemetryPropertyMap properties) {
      this.entries.putAll(properties.entries);
      return this;
   }

   public TelemetryPropertyMap build() {
      return new TelemetryPropertyMap(this.entries);
   }
}
