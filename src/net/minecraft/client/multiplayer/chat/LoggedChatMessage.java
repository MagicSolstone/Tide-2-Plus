package net.minecraft.client.multiplayer.chat;

import com.mojang.authlib.GameProfile;
import java.time.Instant;
import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;

@Environment(EnvType.CLIENT)
public interface LoggedChatMessage extends LoggedChatEvent {
   static LoggedChatMessage$Player player(final GameProfile profile, final PlayerChatMessage message, final ChatTrustLevel trustLevel) {
      return new LoggedChatMessage$Player(profile, message, trustLevel);
   }

   static LoggedChatMessage$System system(final Component message, final Instant timeStamp) {
      return new LoggedChatMessage$System(message, timeStamp);
   }

   Component toContentComponent();

   default Component toNarrationComponent() {
      return this.toContentComponent();
   }

   boolean canReport(UUID reportedPlayerId);
}
