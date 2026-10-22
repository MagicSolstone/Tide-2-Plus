package net.minecraft.commands.arguments;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ArgumentSignatures$Entry(String name, MessageSignature signature) {
   public static final StreamCodec<ByteBuf, ArgumentSignatures$Entry> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.stringUtf8(16),
      ArgumentSignatures$Entry::name,
      MessageSignature.STREAM_CODEC,
      ArgumentSignatures$Entry::signature,
      ArgumentSignatures$Entry::new
   );
}
