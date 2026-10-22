package net.minecraft.server.level;

import java.util.function.Predicate;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;

public interface ServerEntity$Synchronizer {
   void sendToTrackingPlayers(Packet<? super ClientGamePacketListener> packet);

   void sendToTrackingPlayersAndSelf(Packet<? super ClientGamePacketListener> packet);

   void sendToTrackingPlayersFiltered(Packet<? super ClientGamePacketListener> packet, Predicate<ServerPlayer> predicate);
}
