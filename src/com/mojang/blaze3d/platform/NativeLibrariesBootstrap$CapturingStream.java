package com.mojang.blaze3d.platform;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class NativeLibrariesBootstrap$CapturingStream extends OutputStream {
   @Nullable
   private ByteArrayOutputStream buffer;

   private NativeLibrariesBootstrap$CapturingStream() {
   }

   @Override
   public void write(final byte[] b, final int off, final int len) {
      if (this.buffer != null) {
         this.buffer.write(b, off, len);
      }
   }

   @Override
   public void write(final int b) {
      if (this.buffer != null) {
         this.buffer.write(b);
      }
   }
}
