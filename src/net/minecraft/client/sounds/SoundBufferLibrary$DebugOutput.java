package net.minecraft.client.sounds;

import javax.sound.sampled.AudioFormat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public interface SoundBufferLibrary$DebugOutput {
   void accountBuffer(Identifier id, int size, AudioFormat format);
}
