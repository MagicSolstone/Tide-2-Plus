package net.minecraft.client.multiplayer;

import java.util.Objects;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.dialog.DialogConnectionAccess;
import net.minecraft.core.Holder;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.ServerboundCustomClickActionPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.ServerLinks;
import net.minecraft.server.dialog.Dialog;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public abstract class ClientCommonPacketListenerImpl$CommonDialogAccess implements DialogConnectionAccess {
   protected ClientCommonPacketListenerImpl$CommonDialogAccess(final ClientCommonPacketListenerImpl this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public void disconnect(final Component message) {
      this.this$0.connection.disconnect(message);
      this.this$0.connection.handleDisconnection();
   }

   @Override
   public void openDialog(final Holder<Dialog> dialog, @Nullable final Screen activeScreen) {
      this.this$0.showDialog(dialog, this, activeScreen);
   }

   @Override
   public void sendCustomAction(final Identifier id, final Optional<Tag> payload) {
      this.this$0.send(new ServerboundCustomClickActionPacket(id, payload));
   }

   @Override
   public ServerLinks serverLinks() {
      return this.this$0.serverLinks();
   }
}
