package net.minecraft.client.telemetry;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.StringRepresentable;

@Environment(EnvType.CLIENT)
public enum TelemetryProperty$ServerType implements StringRepresentable {
   REALM("realm"),
   LOCAL("local"),
   OTHER("server");

   public static final Codec<TelemetryProperty$ServerType> CODEC = StringRepresentable.fromEnum(TelemetryProperty$ServerType::values);
   private final String key;

   private TelemetryProperty$ServerType(final String key) {
      this.key = key;
   }

   @Override
   public String getSerializedName() {
      return this.key;
   }
}
