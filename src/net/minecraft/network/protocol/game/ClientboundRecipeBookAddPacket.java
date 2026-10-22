package net.minecraft.network.protocol.game;

import java.util.List;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;

public record ClientboundRecipeBookAddPacket(List<ClientboundRecipeBookAddPacket$Entry> entries, boolean replace) implements Packet<ClientGamePacketListener> {
   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundRecipeBookAddPacket> STREAM_CODEC = StreamCodec.composite(
      ClientboundRecipeBookAddPacket$Entry.STREAM_CODEC.apply(ByteBufCodecs.list()),
      ClientboundRecipeBookAddPacket::entries,
      ByteBufCodecs.BOOL,
      ClientboundRecipeBookAddPacket::replace,
      ClientboundRecipeBookAddPacket::new
   );

   @Override
   public PacketType<ClientboundRecipeBookAddPacket> type() {
      return GamePacketTypes.CLIENTBOUND_RECIPE_BOOK_ADD;
   }

   public void handle(final ClientGamePacketListener listener) {
      listener.handleRecipeBookAdd(this);
   }
}
