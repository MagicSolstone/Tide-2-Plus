package net.minecraft.client.telemetry;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.StringRepresentable;

@Environment(EnvType.CLIENT)
public enum TelemetryProperty$GameMode implements StringRepresentable {
   SURVIVAL("survival", 0),
   CREATIVE("creative", 1),
   ADVENTURE("adventure", 2),
   SPECTATOR("spectator", 6),
   HARDCORE("hardcore", 99);

   public static final Codec<TelemetryProperty$GameMode> CODEC = StringRepresentable.fromEnum(TelemetryProperty$GameMode::values);
   private final String key;
   private final int id;

   private TelemetryProperty$GameMode(final String key, final int id) {
      this.key = key;
      this.id = id;
   }

   public int id() {
      return this.id;
   }

   @Override
   public String getSerializedName() {
      return this.key;
   }
}
