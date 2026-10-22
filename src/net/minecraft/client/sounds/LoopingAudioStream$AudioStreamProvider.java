package net.minecraft.client.sounds;

import java.io.IOException;
import java.io.InputStream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface LoopingAudioStream$AudioStreamProvider {
   AudioStream create(final InputStream inputStream) throws IOException;
}
