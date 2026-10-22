package net.minecraft.client.multiplayer;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class ClientConfigurationPacketListenerImpl$2 extends ClientCommonPacketListenerImpl$CommonDialogAccess {
   ClientConfigurationPacketListenerImpl$2(final ClientConfigurationPacketListenerImpl this$0) {
      Objects.requireNonNull(this$0);
      super(this$0);
   }

   @Override
   public void runCommand(final String command, @Nullable final Screen activeScreen) {
      ClientConfigurationPacketListenerImpl.LOGGER.warn("Commands are not supported in configuration phase, trying to run '{}'", command);
   }
}
