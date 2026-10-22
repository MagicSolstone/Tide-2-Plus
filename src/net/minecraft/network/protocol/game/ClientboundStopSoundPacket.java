package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import org.jspecify.annotations.Nullable;

public class ClientboundStopSoundPacket implements Packet<ClientGamePacketListener> {
   public static final StreamCodec<FriendlyByteBuf, ClientboundStopSoundPacket> STREAM_CODEC = Packet.codec(
      ClientboundStopSoundPacket::write, ClientboundStopSoundPacket::new
   );
   private static final int HAS_SOURCE = 1;
   private static final int HAS_SOUND = 2;
   @Nullable
   private final Identifier name;
   @Nullable
   private final SoundSource source;

   public ClientboundStopSoundPacket(@Nullable final Identifier name, @Nullable final SoundSource source) {
      this.name = name;
      this.source = source;
   }

   private ClientboundStopSoundPacket(final FriendlyByteBuf input) {
      int flags = input.readByte();
      if ((flags & 1) > 0) {
         this.source = input.readEnum(SoundSource.class);
      } else {
         this.source = null;
      }

      if ((flags & 2) > 0) {
         this.name = input.readIdentifier();
      } else {
         this.name = null;
      }
   }

   private void write(final FriendlyByteBuf output) {
      if (this.source != null) {
         if (this.name != null) {
            output.writeByte(3);
            output.writeEnum(this.source);
            output.writeIdentifier(this.name);
         } else {
            output.writeByte(1);
            output.writeEnum(this.source);
         }
      } else if (this.name != null) {
         output.writeByte(2);
         output.writeIdentifier(this.name);
      } else {
         output.writeByte(0);
      }
   }

   @Override
   public PacketType<ClientboundStopSoundPacket> type() {
      return GamePacketTypes.CLIENTBOUND_STOP_SOUND;
   }

   public void handle(final ClientGamePacketListener listener) {
      listener.handleStopSoundEvent(this);
   }

   @Nullable
   public Identifier getName() {
      return this.name;
   }

   @Nullable
   public SoundSource getSource() {
      return this.source;
   }
}
