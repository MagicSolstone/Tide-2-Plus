package net.minecraft.client.telemetry.events;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record GameLoadTimesEvent$Measurement(int millis) {
   public static final Codec<GameLoadTimesEvent$Measurement> CODEC = Codec.INT.xmap(GameLoadTimesEvent$Measurement::new, o -> o.millis);
}
