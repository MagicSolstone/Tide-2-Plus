package net.minecraft.client.main;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class Main$2 extends Authenticator {
   Main$2(final String val$proxyUser, final String val$proxyPass) {
      this.val$proxyUser = val$proxyUserx;
      this.val$proxyPass = val$proxyPassx;
   }

   @Override
   protected PasswordAuthentication getPasswordAuthentication() {
      return new PasswordAuthentication(this.val$proxyUser, this.val$proxyPass.toCharArray());
   }
}
