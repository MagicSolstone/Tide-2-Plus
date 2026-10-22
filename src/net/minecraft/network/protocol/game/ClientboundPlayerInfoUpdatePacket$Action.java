package net.minecraft.network.protocol.game;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import io.netty.buffer.ByteBuf;
import java.util.Objects;
import java.util.function.IntFunction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.RemoteChatSession$Data;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;
import net.minecraft.world.level.GameType;

public enum ClientboundPlayerInfoUpdatePacket$Action {
   ADD_PLAYER(0, (entry, input) -> {
      String name = ByteBufCodecs.PLAYER_NAME.decode(input);
      PropertyMap properties = ByteBufCodecs.GAME_PROFILE_PROPERTIES.decode(input);
      entry.profile = new GameProfile(entry.profileId, name, properties);
   }, (output, entry) -> {
      GameProfile profile = Objects.requireNonNull(entry.profile());
      ByteBufCodecs.PLAYER_NAME.encode(output, profile.name());
      ByteBufCodecs.GAME_PROFILE_PROPERTIES.encode(output, profile.properties());
   }),
   INITIALIZE_CHAT(
      1,
      (entry, input) -> entry.chatSession = input.readNullable(RemoteChatSession$Data.STREAM_CODEC),
      (output, entry) -> output.writeNullable(entry.chatSession, RemoteChatSession$Data.STREAM_CODEC)
   ),
   UPDATE_GAME_MODE(
      2, (entry, input) -> entry.gameMode = GameType.STREAM_CODEC.decode(input), (output, entry) -> GameType.STREAM_CODEC.encode(output, entry.gameMode)
   ),
   UPDATE_LISTED(3, (entry, input) -> entry.listed = input.readBoolean(), (output, entry) -> output.writeBoolean(entry.listed())),
   UPDATE_LATENCY(4, (entry, input) -> entry.latency = input.readVarInt(), (output, entry) -> output.writeVarInt(entry.latency())),
   UPDATE_DISPLAY_NAME(
      5,
      (entry, input) -> entry.displayName = FriendlyByteBuf.readNullable(input, ComponentSerialization.TRUSTED_STREAM_CODEC),
      (output, entry) -> FriendlyByteBuf.writeNullable(output, entry.displayName(), ComponentSerialization.TRUSTED_STREAM_CODEC)
   ),
   UPDATE_LIST_ORDER(6, (entry, input) -> entry.listOrder = input.readVarInt(), (output, entry) -> output.writeVarInt(entry.listOrder)),
   UPDATE_HAT(7, (entry, input) -> entry.showHat = input.readBoolean(), (output, entry) -> output.writeBoolean(entry.showHat));

   private final int id;
   private final ClientboundPlayerInfoUpdatePacket$Action$Reader reader;
   private final ClientboundPlayerInfoUpdatePacket$Action$Writer writer;
   private static final IntFunction<ClientboundPlayerInfoUpdatePacket$Action> BY_ID = ByIdMap.continuous(a -> a.id, values(), ByIdMap$OutOfBoundsStrategy.ZERO);
   public static final StreamCodec<ByteBuf, ClientboundPlayerInfoUpdatePacket$Action> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, a -> a.id);

   private ClientboundPlayerInfoUpdatePacket$Action(
      final int id, final ClientboundPlayerInfoUpdatePacket$Action$Reader reader, final ClientboundPlayerInfoUpdatePacket$Action$Writer writer
   ) {
      this.id = id;
      this.reader = reader;
      this.writer = writer;
   }
}
