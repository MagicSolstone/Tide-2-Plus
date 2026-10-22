package net.minecraft.client;

import com.mojang.blaze3d.platform.ClientShutdownWatchdog;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.main.GameConfig;

@Environment(EnvType.CLIENT)
class Minecraft$1 implements Runnable {
   private boolean threadStarted;

   Minecraft$1(final Minecraft this$0, final GameConfig val$gameConfig) {
      this.val$gameConfig = val$gameConfigx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public void run() {
      if (!this.threadStarted) {
         this.threadStarted = true;
         ClientShutdownWatchdog.startShutdownWatchdog("window close callback", false, this.this$0, this.val$gameConfig, this.this$0.gameThread.threadId());
      }
   }
}
