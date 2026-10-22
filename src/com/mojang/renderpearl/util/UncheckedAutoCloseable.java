package com.mojang.renderpearl.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public interface UncheckedAutoCloseable extends AutoCloseable {
   @Override
   void close();

   static void safeClose(@Nullable final UncheckedAutoCloseable closeable) {
      if (closeable != null) {
         closeable.close();
      }
   }
}
