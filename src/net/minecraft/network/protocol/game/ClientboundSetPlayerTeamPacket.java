package net.minecraft.network.protocol.game;

import io.netty.buffer.ByteBuf;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.world.scores.PlayerTeam;
import org.jspecify.annotations.Nullable;

public class ClientboundSetPlayerTeamPacket implements Packet<ClientGamePacketListener> {
   private static final StreamCodec<ByteBuf, List<String>> PLAYER_LIST_STREAM_CODEC = ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list());
   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSetPlayerTeamPacket> STREAM_CODEC = Packet.codec(
      ClientboundSetPlayerTeamPacket::write, ClientboundSetPlayerTeamPacket::new
   );
   private static final int METHOD_ADD = 0;
   private static final int METHOD_REMOVE = 1;
   private static final int METHOD_CHANGE = 2;
   private static final int METHOD_JOIN = 3;
   private static final int METHOD_LEAVE = 4;
   private final int method;
   private final String name;
   private final List<String> players;
   private final Optional<ClientboundSetPlayerTeamPacket$Parameters> parameters;

   private ClientboundSetPlayerTeamPacket(
      final String name, final int method, final Optional<ClientboundSetPlayerTeamPacket$Parameters> parameters, final Collection<String> players
   ) {
      this.name = name;
      this.method = method;
      this.parameters = parameters;
      this.players = List.copyOf(players);
   }

   public static ClientboundSetPlayerTeamPacket createAddOrModifyPacket(final PlayerTeam team, final boolean createNew) {
      return new ClientboundSetPlayerTeamPacket(
         team.getName(),
         createNew ? 0 : 2,
         Optional.of(new ClientboundSetPlayerTeamPacket$Parameters(team)),
         (Collection<String>)(createNew ? team.getPlayers() : List.of())
      );
   }

   public static ClientboundSetPlayerTeamPacket createRemovePacket(final PlayerTeam team) {
      return new ClientboundSetPlayerTeamPacket(team.getName(), 1, Optional.empty(), List.of());
   }

   public static ClientboundSetPlayerTeamPacket createPlayerPacket(
      final PlayerTeam team, final String player, final ClientboundSetPlayerTeamPacket$Action action
   ) {
      return new ClientboundSetPlayerTeamPacket(team.getName(), action == ClientboundSetPlayerTeamPacket$Action.ADD ? 3 : 4, Optional.empty(), List.of(player));
   }

   private ClientboundSetPlayerTeamPacket(final RegistryFriendlyByteBuf input) {
      this.name = input.readUtf();
      this.method = input.readByte();
      if (shouldHaveParameters(this.method)) {
         this.parameters = Optional.of(ClientboundSetPlayerTeamPacket$Parameters.STREAM_CODEC.decode(input));
      } else {
         this.parameters = Optional.empty();
      }

      if (shouldHavePlayerList(this.method)) {
         this.players = PLAYER_LIST_STREAM_CODEC.decode(input);
      } else {
         this.players = List.of();
      }
   }

   private void write(final RegistryFriendlyByteBuf output) {
      output.writeUtf(this.name);
      output.writeByte(this.method);
      if (shouldHaveParameters(this.method)) {
         ClientboundSetPlayerTeamPacket$Parameters.STREAM_CODEC
            .encode(output, this.parameters.orElseThrow(() -> new IllegalStateException("Parameters not present, but method is" + this.method)));
      }

      if (shouldHavePlayerList(this.method)) {
         PLAYER_LIST_STREAM_CODEC.encode(output, this.players);
      }
   }

   private static boolean shouldHavePlayerList(final int method) {
      return method == 0 || method == 3 || method == 4;
   }

   private static boolean shouldHaveParameters(final int method) {
      return method == 0 || method == 2;
   }

   @Nullable
   public ClientboundSetPlayerTeamPacket$Action getPlayerAction() {
      return switch (this.method) {
         case 0, 3 -> ClientboundSetPlayerTeamPacket$Action.ADD;
         default -> null;
         case 4 -> ClientboundSetPlayerTeamPacket$Action.REMOVE;
      };
   }

   @Nullable
   public ClientboundSetPlayerTeamPacket$Action getTeamAction() {
      return switch (this.method) {
         case 0 -> ClientboundSetPlayerTeamPacket$Action.ADD;
         case 1 -> ClientboundSetPlayerTeamPacket$Action.REMOVE;
         default -> null;
      };
   }

   @Override
   public PacketType<ClientboundSetPlayerTeamPacket> type() {
      return GamePacketTypes.CLIENTBOUND_SET_PLAYER_TEAM;
   }

   public void handle(final ClientGamePacketListener listener) {
      listener.handleSetPlayerTeamPacket(this);
   }

   public String getName() {
      return this.name;
   }

   public Collection<String> getPlayers() {
      return this.players;
   }

   public Optional<ClientboundSetPlayerTeamPacket$Parameters> getParameters() {
      return this.parameters;
   }
}
