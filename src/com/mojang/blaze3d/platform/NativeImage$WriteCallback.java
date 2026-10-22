package com.mojang.blaze3d.platform;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;
import org.lwjgl.stb.STBIWriteCallback;

@Environment(EnvType.CLIENT)
class NativeImage$WriteCallback extends STBIWriteCallback {
   private final WritableByteChannel output;
   @Nullable
   private IOException exception;

   private NativeImage$WriteCallback(final WritableByteChannel output) {
      this.output = output;
   }

   public void invoke(final long context, final long data, final int size) {
      ByteBuffer dataBuf = getData(data, size);

      try {
         this.output.write(dataBuf);
      } catch (IOException var8) {
         this.exception = var8;
      }
   }

   public void throwIfException() throws IOException {
      if (this.exception != null) {
         throw this.exception;
      }
   }
}
