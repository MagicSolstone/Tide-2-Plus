package net.minecraft.realms;

import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.dto.RealmsServer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class RealmsConnect {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final Screen onlineScreen;
   private volatile boolean aborted;
   @Nullable
   private Connection connection;

   public RealmsConnect(final Screen onlineScreen) {
      this.onlineScreen = onlineScreen;
   }

   public void connect(final RealmsServer server, final ServerAddress hostAndPort) {
      Minecraft minecraft = Minecraft.getInstance();
      minecraft.prepareForMultiplayer();
      minecraft.getNarrator().saySystemNow(Component.translatable("mco.connect.success"));
      String hostname = hostAndPort.getHost();
      int port = hostAndPort.getPort();
      new RealmsConnect$1(this, "Realms-connect-task", hostname, port, minecraft, server).start();
   }

   public void abort() {
      this.aborted = true;
      if (this.connection != null && this.connection.isConnected()) {
         this.connection.disconnect(Component.translatable("disconnect.genericReason"));
         this.connection.handleDisconnection();
      }
   }

   public void tick() {
      if (this.connection != null) {
         if (this.connection.isConnected()) {
            this.connection.tick();
         } else {
            this.connection.handleDisconnection();
         }
      }
   }
}
