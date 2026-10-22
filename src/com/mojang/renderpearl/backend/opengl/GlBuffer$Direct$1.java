package com.mojang.renderpearl.backend.opengl;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class GlBuffer$Direct$1 implements Runnable {
   private boolean closed;

   GlBuffer$Direct$1(final GlBuffer$Direct this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.closed = false;
   }

   @Override
   public void run() {
      if (!this.closed) {
         this.closed = true;
         this.this$0.unmap();
      }
   }
}
