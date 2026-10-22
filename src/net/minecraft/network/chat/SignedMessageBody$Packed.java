package net.minecraft.network.chat;

import io.netty.buffer.ByteBuf;
import java.time.Instant;
import java.util.Optional;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record SignedMessageBody$Packed(String content, Instant timeStamp, long salt, LastSeenMessages$Packed lastSeen) {
   public static final StreamCodec<ByteBuf, SignedMessageBody$Packed> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.stringUtf8(256),
      SignedMessageBody$Packed::content,
      ByteBufCodecs.INSTANT,
      SignedMessageBody$Packed::timeStamp,
      ByteBufCodecs.LONG,
      SignedMessageBody$Packed::salt,
      LastSeenMessages$Packed.STREAM_CODEC,
      SignedMessageBody$Packed::lastSeen,
      SignedMessageBody$Packed::new
   );

   public Optional<SignedMessageBody> unpack(final MessageSignatureCache cache) {
      return this.lastSeen.unpack(cache).map(lastSeen -> new SignedMessageBody(this.content, this.timeStamp, this.salt, lastSeen));
   }
}
