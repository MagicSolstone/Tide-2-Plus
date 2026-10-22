package net.minecraft.network.protocol.game;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import net.minecraft.network.SkipPacketDecoderException;
import net.minecraft.network.codec.StreamCodec;

class ServerboundCommandSuggestionPacket$1 implements StreamCodec<ByteBuf, ServerboundCommandSuggestionPacket> {
   private final StreamCodec<ByteBuf, ServerboundCommandSuggestionPacket> downstream = ServerboundCommandSuggestionPacket.createStreamCodec(256);

   public ServerboundCommandSuggestionPacket decode(final ByteBuf input) {
      try {
         return this.downstream.decode(input);
      } catch (DecoderException var3) {
         throw new SkipPacketDecoderException(var3);
      }
   }

   public void encode(final ByteBuf output, final ServerboundCommandSuggestionPacket packet) {
      ServerboundCommandSuggestionPacket.STREAM_CODEC.encode(output, packet);
   }
}
