package net.minecraft.network.chat;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Base64;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.SignatureUpdater;
import net.minecraft.util.SignatureValidator;
import org.jspecify.annotations.Nullable;

public record MessageSignature(byte[] bytes) {
   public static final Codec<MessageSignature> CODEC = ExtraCodecs.BASE64_STRING.xmap(MessageSignature::new, MessageSignature::bytes);
   public static final StreamCodec<ByteBuf, MessageSignature> STREAM_CODEC = StreamCodec.of(MessageSignature::write, MessageSignature::read);
   public static final int BYTES = 256;

   public MessageSignature(byte[] bytes) {
      Preconditions.checkState(bytes.length == 256, "Invalid message signature size");
      this.bytes = bytes;
   }

   private static MessageSignature read(final ByteBuf input) {
      byte[] bytes = new byte[256];
      input.readBytes(bytes);
      return new MessageSignature(bytes);
   }

   private static void write(final ByteBuf output, final MessageSignature signature) {
      output.writeBytes(signature.bytes);
   }

   public boolean verify(final SignatureValidator signature, final SignatureUpdater updater) {
      return signature.validate(updater, this.bytes);
   }

   public ByteBuffer asByteBuffer() {
      return ByteBuffer.wrap(this.bytes);
   }

   @Override
   public boolean equals(final Object o) {
      return this == o || o instanceof MessageSignature that && Arrays.equals(this.bytes, that.bytes);
   }

   @Override
   public int hashCode() {
      return Arrays.hashCode(this.bytes);
   }

   @Override
   public String toString() {
      return Base64.getEncoder().encodeToString(this.bytes);
   }

   public static String describe(@Nullable final MessageSignature signature) {
      return signature == null ? "<no signature>" : signature.toString();
   }

   public MessageSignature$Packed pack(final MessageSignatureCache cache) {
      int packedId = cache.pack(this);
      return packedId != -1 ? new MessageSignature$Packed(packedId) : new MessageSignature$Packed(this);
   }

   public int checksum() {
      return Arrays.hashCode(this.bytes);
   }
}
