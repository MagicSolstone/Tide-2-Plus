package net.minecraft.client.telemetry;

import com.mojang.authlib.minecraft.TelemetryPropertyContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface TelemetryProperty$Exporter<T> {
   void apply(TelemetryPropertyContainer output, String key, T value);
}
