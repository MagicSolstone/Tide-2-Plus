package net.minecraft.client.telemetry;

import com.mojang.authlib.minecraft.TelemetryEvent;
import com.mojang.authlib.minecraft.TelemetrySession;
import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record TelemetryEventInstance(TelemetryEventType type, TelemetryPropertyMap properties) {
   public static final Codec<TelemetryEventInstance> CODEC = TelemetryEventType.CODEC.dispatchStable(TelemetryEventInstance::type, TelemetryEventType::codec);

   public TelemetryEventInstance(TelemetryEventType type, TelemetryPropertyMap properties) {
      properties.propertySet().forEach(property -> {
         if (!type.contains((TelemetryProperty<?>)property)) {
            throw new IllegalArgumentException("Property '" + property.id() + "' not expected for event: '" + type.id() + "'");
         }
      });
      this.type = type;
      this.properties = properties;
   }

   public TelemetryEvent export(final TelemetrySession session) {
      return this.type.export(session, this.properties);
   }
}
