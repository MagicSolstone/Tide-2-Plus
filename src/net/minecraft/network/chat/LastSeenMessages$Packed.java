package net.minecraft.network.chat;

import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record LastSeenMessages$Packed(List<MessageSignature$Packed> entries) {
   public static final LastSeenMessages$Packed EMPTY = new LastSeenMessages$Packed(List.of());
   public static final StreamCodec<ByteBuf, LastSeenMessages$Packed> STREAM_CODEC = StreamCodec.composite(
      MessageSignature$Packed.STREAM_CODEC.apply(ByteBufCodecs.list(20)), LastSeenMessages$Packed::entries, LastSeenMessages$Packed::new
   );

   public Optional<LastSeenMessages> unpack(final MessageSignatureCache cache) {
      List<MessageSignature> unpacked = new ArrayList<>(this.entries.size());

      for (MessageSignature$Packed packed : this.entries) {
         Optional<MessageSignature> entry = packed.unpack(cache);
         if (entry.isEmpty()) {
            return Optional.empty();
         }

         unpacked.add(entry.get());
      }

      return Optional.of(new LastSeenMessages(unpacked));
   }
}
