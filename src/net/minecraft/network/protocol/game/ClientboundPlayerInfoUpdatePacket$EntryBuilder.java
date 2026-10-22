package net.minecraft.network.protocol.game;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.RemoteChatSession$Data;
import net.minecraft.world.level.GameType;
import org.jspecify.annotations.Nullable;

class ClientboundPlayerInfoUpdatePacket$EntryBuilder {
   private final UUID profileId;
   @Nullable
   private GameProfile profile;
   private boolean listed;
   private int latency;
   private GameType gameMode = GameType.DEFAULT_MODE;
   @Nullable
   private Component displayName;
   private boolean showHat;
   private int listOrder;
   @Nullable
   private RemoteChatSession$Data chatSession;

   private ClientboundPlayerInfoUpdatePacket$EntryBuilder(final UUID profileId) {
      this.profileId = profileId;
   }

   private ClientboundPlayerInfoUpdatePacket$Entry build() {
      return new ClientboundPlayerInfoUpdatePacket$Entry(
         this.profileId, this.profile, this.listed, this.latency, this.gameMode, this.displayName, this.showHat, this.listOrder, this.chatSession
      );
   }
}
