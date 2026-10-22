package net.minecraft.network.protocol.game;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.Optionull;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.RemoteChatSession;
import net.minecraft.network.chat.RemoteChatSession$Data;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.level.GameType;
import org.jspecify.annotations.Nullable;

public record ClientboundPlayerInfoUpdatePacket$Entry(
   UUID profileId,
   @Nullable GameProfile profile,
   boolean listed,
   int latency,
   GameType gameMode,
   @Nullable Component displayName,
   boolean showHat,
   int listOrder,
   @Nullable RemoteChatSession$Data chatSession
) {
   private ClientboundPlayerInfoUpdatePacket$Entry(final ServerPlayer player) {
      this(
         player.getUUID(),
         player.getGameProfile(),
         true,
         player.connection.latency(),
         player.gameMode(),
         player.getTabListDisplayName(),
         player.isModelPartShown(PlayerModelPart.HAT),
         player.getTabListOrder(),
         Optionull.map(player.getChatSession(), RemoteChatSession::asData)
      );
   }
}
