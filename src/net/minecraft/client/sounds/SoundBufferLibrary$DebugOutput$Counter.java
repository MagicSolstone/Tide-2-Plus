package net.minecraft.client.sounds;

import javax.sound.sampled.AudioFormat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class SoundBufferLibrary$DebugOutput$Counter implements SoundBufferLibrary$DebugOutput {
   private int totalCount;
   private long totalSize;

   @Override
   public void accountBuffer(final Identifier id, final int size, final AudioFormat format) {
      this.totalCount++;
      this.totalSize += size;
   }

   public int totalCount() {
      return this.totalCount;
   }

   public long totalSize() {
      return this.totalSize;
   }
}
