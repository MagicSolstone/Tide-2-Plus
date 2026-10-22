package net.minecraft.client.main;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.server.IntegratedServer;

@Environment(EnvType.CLIENT)
class Main$3 extends Thread {
   Main$3(String name) {
      super(name);
   }

   @Override
   public void run() {
      Minecraft instance = Minecraft.getInstance();
      if (instance != null) {
         IntegratedServer server = instance.getSingleplayerServer();
         if (server != null) {
            server.halt(true);
         }
      }
   }
}
