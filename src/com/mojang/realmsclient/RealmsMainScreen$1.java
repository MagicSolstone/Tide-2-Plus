package com.mojang.realmsclient;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class RealmsMainScreen$1 extends Thread {
   RealmsMainScreen$1(final RealmsMainScreen this$0, String name, final RealmsServer val$server) {
      this.val$server = val$serverx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(name);
   }

   @Override
   public void run() {
      try {
         RealmsClient client = RealmsClient.getOrCreate();
         client.uninviteMyselfFrom(this.val$server.id);
         RealmsMainScreen.access$000(this.this$0).execute(RealmsMainScreen::refreshServerList);
      } catch (RealmsServiceException var2) {
         RealmsMainScreen.LOGGER.error("Couldn't configure world", var2);
         RealmsMainScreen.access$200(this.this$0)
            .execute(() -> RealmsMainScreen.access$100(this.this$0).gui.setScreen(new RealmsGenericErrorScreen(var2, this.this$0)));
      }
   }
}
