package net.minecraft.client.multiplayer;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class ClientPacketListener$2 extends ClientCommonPacketListenerImpl$CommonDialogAccess {
   ClientPacketListener$2(final ClientPacketListener this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(this$0);
   }

   @Override
   public void runCommand(final String command, @Nullable final Screen activeScreen) {
      this.this$0.sendUnattendedCommand(command, activeScreen);
   }
}
