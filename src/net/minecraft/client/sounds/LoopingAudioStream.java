package net.minecraft.client.sounds;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import javax.sound.sampled.AudioFormat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class LoopingAudioStream implements AudioStream {
   private final LoopingAudioStream$AudioStreamProvider provider;
   private AudioStream stream;
   private final BufferedInputStream bufferedInputStream;

   public LoopingAudioStream(final LoopingAudioStream$AudioStreamProvider provider, final InputStream originalInputStream) throws IOException {
      this.provider = provider;
      this.bufferedInputStream = new BufferedInputStream(originalInputStream);
      this.bufferedInputStream.mark(Integer.MAX_VALUE);
      this.stream = provider.create(new LoopingAudioStream$NoCloseBuffer(this.bufferedInputStream));
   }

   @Override
   public AudioFormat getFormat() {
      return this.stream.getFormat();
   }

   @Override
   public ByteBuffer read(final int expectedSize) throws IOException {
      ByteBuffer result = this.stream.read(expectedSize);
      if (!result.hasRemaining()) {
         this.stream.close();
         this.bufferedInputStream.reset();
         this.stream = this.provider.create(new LoopingAudioStream$NoCloseBuffer(this.bufferedInputStream));
         result = this.stream.read(expectedSize);
      }

      return result;
   }

   @Override
   public void close() throws IOException {
      this.stream.close();
      this.bufferedInputStream.close();
   }
}
