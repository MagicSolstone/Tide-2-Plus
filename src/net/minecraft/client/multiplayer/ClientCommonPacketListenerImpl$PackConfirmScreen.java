package net.minecraft.client.multiplayer;

import com.google.common.collect.ImmutableList;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.server.DownloadedPackSource;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class ClientCommonPacketListenerImpl$PackConfirmScreen extends ConfirmScreen {
   private final List<ClientCommonPacketListenerImpl$PackConfirmScreen$PendingRequest> requests;
   @Nullable
   private final Screen parentScreen;

   private ClientCommonPacketListenerImpl$PackConfirmScreen(
      final Minecraft this$0,
      @Nullable final Screen minecraft,
      final List<ClientCommonPacketListenerImpl$PackConfirmScreen$PendingRequest> parentScreen,
      final boolean requests,
      @Nullable final Component required,
      final Component prompt
   ) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(
         result -> {
            minecraft.gui.setScreen(parentScreen);
            DownloadedPackSource packSource = minecraft.getDownloadedPackSource();
            if (result) {
               if (this$0x.serverData != null) {
                  this$0x.serverData.setResourcePackStatus(ServerData$ServerPackStatus.ENABLED);
               }

               packSource.allowServerPacks();
            } else {
               packSource.rejectServerPacks();
               if (required) {
                  this$0x.connection.disconnect(Component.translatable("multiplayer.requiredTexturePrompt.disconnect"));
               } else if (this$0x.serverData != null) {
                  this$0x.serverData.setResourcePackStatus(ServerData$ServerPackStatus.DISABLED);
               }
            }

            for (ClientCommonPacketListenerImpl$PackConfirmScreen$PendingRequest request : requests) {
               packSource.pushPack(request.id, request.url, request.hash);
            }

            if (this$0x.serverData != null) {
               ServerList.saveSingleServer(this$0x.serverData);
            }
         },
         required ? Component.translatable("multiplayer.requiredTexturePrompt.line1") : Component.translatable("multiplayer.texturePrompt.line1"),
         ClientCommonPacketListenerImpl.preparePackPrompt(
            required
               ? Component.translatable("multiplayer.requiredTexturePrompt.line2").withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD)
               : Component.translatable("multiplayer.texturePrompt.line2"),
            prompt
         ),
         required ? CommonComponents.GUI_PROCEED : CommonComponents.GUI_YES,
         required ? CommonComponents.GUI_DISCONNECT : CommonComponents.GUI_NO
      );
      this.requests = requests;
      this.parentScreen = parentScreen;
   }

   public ClientCommonPacketListenerImpl$PackConfirmScreen update(
      final Minecraft minecraft, final UUID id, final URL url, final String hash, final boolean required, @Nullable final Component prompt
   ) {
      List<ClientCommonPacketListenerImpl$PackConfirmScreen$PendingRequest> extendedRequests = ImmutableList.builderWithExpectedSize(this.requests.size() + 1)
         .addAll(this.requests)
         .add(new ClientCommonPacketListenerImpl$PackConfirmScreen$PendingRequest(id, url, hash))
         .build();
      return new ClientCommonPacketListenerImpl$PackConfirmScreen(this.this$0, minecraft, this.parentScreen, (boolean)extendedRequests, required, prompt);
   }
}
