package net.minecraft.network.protocol.game;

import com.google.common.base.MoreObjects;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.server.level.ServerPlayer;

public class ClientboundPlayerInfoUpdatePacket implements Packet<ClientGamePacketListener> {
   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundPlayerInfoUpdatePacket> STREAM_CODEC = Packet.codec(
      ClientboundPlayerInfoUpdatePacket::write, ClientboundPlayerInfoUpdatePacket::new
   );
   private final EnumSet<ClientboundPlayerInfoUpdatePacket$Action> actions;
   private final List<ClientboundPlayerInfoUpdatePacket$Entry> entries;

   public ClientboundPlayerInfoUpdatePacket(final EnumSet<ClientboundPlayerInfoUpdatePacket$Action> actions, final Collection<ServerPlayer> players) {
      this.actions = actions;
      this.entries = players.stream().map(ClientboundPlayerInfoUpdatePacket$Entry::new).toList();
   }

   public ClientboundPlayerInfoUpdatePacket(final ClientboundPlayerInfoUpdatePacket$Action action, final ServerPlayer player) {
      this.actions = EnumSet.of(action);
      this.entries = List.of(new ClientboundPlayerInfoUpdatePacket$Entry(player));
   }

   public static ClientboundPlayerInfoUpdatePacket createPlayerInitializing(final Collection<ServerPlayer> players) {
      EnumSet<ClientboundPlayerInfoUpdatePacket$Action> actions = EnumSet.of(
         ClientboundPlayerInfoUpdatePacket$Action.ADD_PLAYER,
         ClientboundPlayerInfoUpdatePacket$Action.INITIALIZE_CHAT,
         ClientboundPlayerInfoUpdatePacket$Action.UPDATE_GAME_MODE,
         ClientboundPlayerInfoUpdatePacket$Action.UPDATE_LISTED,
         ClientboundPlayerInfoUpdatePacket$Action.UPDATE_LATENCY,
         ClientboundPlayerInfoUpdatePacket$Action.UPDATE_DISPLAY_NAME,
         ClientboundPlayerInfoUpdatePacket$Action.UPDATE_HAT,
         ClientboundPlayerInfoUpdatePacket$Action.UPDATE_LIST_ORDER
      );
      return new ClientboundPlayerInfoUpdatePacket(actions, players);
   }

   private static StreamCodec<RegistryFriendlyByteBuf, ClientboundPlayerInfoUpdatePacket$Entry> entryCodec(
      final EnumSet<ClientboundPlayerInfoUpdatePacket$Action> actions
   ) {
      return StreamCodec.of((output, entry) -> {
         output.writeUUID(entry.profileId());

         for (ClientboundPlayerInfoUpdatePacket$Action action : actions) {
            action.writer.write(output, entry);
         }
      }, input -> {
         ClientboundPlayerInfoUpdatePacket$EntryBuilder builder = new ClientboundPlayerInfoUpdatePacket$EntryBuilder(input.readUUID());

         for (ClientboundPlayerInfoUpdatePacket$Action action : actions) {
            action.reader.read(builder, input);
         }

         return builder.build();
      });
   }

   private ClientboundPlayerInfoUpdatePacket(final RegistryFriendlyByteBuf input) {
      this.actions = input.readEnumSet(ClientboundPlayerInfoUpdatePacket$Action.class);
      this.entries = entryCodec(this.actions).apply(ByteBufCodecs.list()).decode(input);
   }

   private void write(final RegistryFriendlyByteBuf output) {
      output.writeEnumSet(this.actions, ClientboundPlayerInfoUpdatePacket$Action.class);
      entryCodec(this.actions).apply(ByteBufCodecs.list()).encode(output, this.entries);
   }

   @Override
   public PacketType<ClientboundPlayerInfoUpdatePacket> type() {
      return GamePacketTypes.CLIENTBOUND_PLAYER_INFO_UPDATE;
   }

   public void handle(final ClientGamePacketListener listener) {
      listener.handlePlayerInfoUpdate(this);
   }

   public EnumSet<ClientboundPlayerInfoUpdatePacket$Action> actions() {
      return this.actions;
   }

   public List<ClientboundPlayerInfoUpdatePacket$Entry> entries() {
      return this.entries;
   }

   public List<ClientboundPlayerInfoUpdatePacket$Entry> newEntries() {
      return this.actions.contains(ClientboundPlayerInfoUpdatePacket$Action.ADD_PLAYER) ? this.entries : List.of();
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this).add("actions", this.actions).add("entries", this.entries).toString();
   }
}
