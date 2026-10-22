package net.minecraft.client.main;

import java.net.Proxy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.User;

@Environment(EnvType.CLIENT)
public class GameConfig$UserData {
   public final User user;
   public final Proxy proxy;

   public GameConfig$UserData(final User user, final Proxy proxy) {
      this.user = user;
      this.proxy = proxy;
   }
}
