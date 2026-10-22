package net.minecraft.network.chat;

import io.netty.buffer.ByteBuf;
import java.util.BitSet;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record LastSeenMessages$Update(int offset, BitSet acknowledged, byte checksum) {
   public static final byte IGNORE_CHECKSUM = 0;
   public static final StreamCodec<ByteBuf, LastSeenMessages$Update> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.VAR_INT,
      LastSeenMessages$Update::offset,
      ByteBufCodecs.fixedBitSet(20),
      LastSeenMessages$Update::acknowledged,
      ByteBufCodecs.BYTE,
      LastSeenMessages$Update::checksum,
      LastSeenMessages$Update::new
   );

   public boolean verifyChecksum(final LastSeenMessages lastSeen) {
      return this.checksum == 0 || this.checksum == lastSeen.computeChecksum();
   }
}
