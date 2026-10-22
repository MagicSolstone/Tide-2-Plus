package net.minecraft.commands.arguments;

import io.netty.buffer.ByteBuf;
import java.util.List;
import java.util.Objects;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.SignableCommand;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ArgumentSignatures(List<ArgumentSignatures$Entry> entries) {
   public static final ArgumentSignatures EMPTY = new ArgumentSignatures(List.of());
   private static final int MAX_ARGUMENT_COUNT = 8;
   private static final int MAX_ARGUMENT_NAME_LENGTH = 16;
   public static final StreamCodec<ByteBuf, ArgumentSignatures> STREAM_CODEC = StreamCodec.composite(
      ArgumentSignatures$Entry.STREAM_CODEC.apply(ByteBufCodecs.list(8)), ArgumentSignatures::entries, ArgumentSignatures::new
   );

   public static ArgumentSignatures signCommand(final SignableCommand<?> command, final ArgumentSignatures$Signer signer) {
      List<ArgumentSignatures$Entry> entries = command.arguments().stream().map(argument -> {
         MessageSignature signature = signer.sign(argument.value());
         return signature != null ? new ArgumentSignatures$Entry(argument.name(), signature) : null;
      }).filter(Objects::nonNull).toList();
      return new ArgumentSignatures(entries);
   }
}
