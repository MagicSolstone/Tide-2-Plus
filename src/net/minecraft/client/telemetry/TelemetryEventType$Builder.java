package net.minecraft.client.telemetry;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class TelemetryEventType$Builder {
   private final String id;
   private final String exportKey;
   private final List<TelemetryProperty<?>> properties = new ArrayList<>();
   private boolean isOptIn;

   private TelemetryEventType$Builder(final String id, final String exportKey) {
      this.id = id;
      this.exportKey = exportKey;
   }

   public TelemetryEventType$Builder defineAll(final List<TelemetryProperty<?>> properties) {
      this.properties.addAll(properties);
      return this;
   }

   public <T> TelemetryEventType$Builder define(final TelemetryProperty<T> property) {
      this.properties.add(property);
      return this;
   }

   public TelemetryEventType$Builder optIn() {
      this.isOptIn = true;
      return this;
   }

   public TelemetryEventType register() {
      TelemetryEventType type = new TelemetryEventType(this.id, this.exportKey, List.copyOf(this.properties), this.isOptIn);
      if (TelemetryEventType.REGISTRY.putIfAbsent(this.id, type) != null) {
         throw new IllegalStateException("Duplicate TelemetryEventType with key: '" + this.id + "'");
      } else {
         return type;
      }
   }
}
