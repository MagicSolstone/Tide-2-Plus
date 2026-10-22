package com.mojang.blaze3d.platform;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.commons.io.output.TeeOutputStream;

@Environment(EnvType.CLIENT)
class NativeLibrariesBootstrap$CapturingPrintStream extends PrintStream {
   private final NativeLibrariesBootstrap$CapturingStream collector;

   public NativeLibrariesBootstrap$CapturingPrintStream(final OutputStream out) {
      NativeLibrariesBootstrap$CapturingStream logStream = new NativeLibrariesBootstrap$CapturingStream();
      super(new TeeOutputStream(out, logStream), false, StandardCharsets.UTF_8);
      this.collector = logStream;
   }

   public synchronized void startCapturing() {
      this.collector.buffer = new ByteArrayOutputStream();
   }

   public synchronized String stopCapturing() {
      ByteArrayOutputStream buffer = this.collector.buffer;
      this.collector.buffer = null;
      return buffer != null ? buffer.toString(StandardCharsets.UTF_8) : "";
   }
}
