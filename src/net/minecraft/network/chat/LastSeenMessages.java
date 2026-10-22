package net.minecraft.network.chat;

import com.google.common.primitives.Ints;
import com.mojang.serialization.Codec;
import java.security.SignatureException;
import java.util.List;
import net.minecraft.util.SignatureUpdater$Output;

public record LastSeenMessages(List<MessageSignature> entries) {
   public static final Codec<LastSeenMessages> CODEC = MessageSignature.CODEC.listOf().xmap(LastSeenMessages::new, LastSeenMessages::entries);
   public static final LastSeenMessages EMPTY = new LastSeenMessages(List.of());
   public static final int LAST_SEEN_MESSAGES_MAX_LENGTH = 20;

   public void updateSignature(final SignatureUpdater$Output output) throws SignatureException {
      output.update(Ints.toByteArray(this.entries.size()));

      for (MessageSignature entry : this.entries) {
         output.update(entry.bytes());
      }
   }

   public LastSeenMessages$Packed pack(final MessageSignatureCache cache) {
      return new LastSeenMessages$Packed(this.entries.stream().map(entry -> entry.pack(cache)).toList());
   }

   public byte computeChecksum() {
      int checksum = 1;

      for (MessageSignature entry : this.entries) {
         checksum = 31 * checksum + entry.checksum();
      }

      byte checksumByte = (byte)checksum;
      return checksumByte == 0 ? 1 : checksumByte;
   }
}
