package net.minecraft.client.sounds;

import java.io.FilterInputStream;
import java.io.InputStream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class LoopingAudioStream$NoCloseBuffer extends FilterInputStream {
   private LoopingAudioStream$NoCloseBuffer(final InputStream in) {
      super(in);
   }

   @Override
   public void close() {
   }
}
