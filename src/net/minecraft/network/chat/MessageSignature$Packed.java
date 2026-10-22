package net.minecraft.network.chat;

import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.minecraft.network.VarInt;
import net.minecraft.network.codec.StreamCodec;
import org.jspecify.annotations.Nullable;

public record MessageSignature$Packed(int id, @Nullable MessageSignature fullSignature) {
   public static final int FULL_SIGNATURE = -1;
   public static final StreamCodec<ByteBuf, MessageSignature$Packed> STREAM_CODEC = StreamCodec.of(
      MessageSignature$Packed::write, MessageSignature$Packed::read
   );

   public MessageSignature$Packed(final MessageSignature signature) {
      this(-1, signature);
   }

   public MessageSignature$Packed(final int id) {
      this(id, null);
   }

   private static MessageSignature$Packed read(final ByteBuf input) {
      int id = VarInt.read(input) - 1;
      return id == -1 ? new MessageSignature$Packed(MessageSignature.read(input)) : new MessageSignature$Packed(id);
   }

   private static void write(final ByteBuf output, final MessageSignature$Packed packed) {
      VarInt.write(output, packed.id() + 1);
      if (packed.fullSignature() != null) {
         MessageSignature.write(output, packed.fullSignature());
      }
   }

   public Optional<MessageSignature> unpack(final MessageSignatureCache cache) {
      return this.fullSignature != null ? Optional.of(this.fullSignature) : Optional.ofNullable(cache.unpack(this.id));
   }
}
