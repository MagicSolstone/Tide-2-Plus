package net.minecraft.network.protocol.game;

import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;

public enum ClientboundCustomChatCompletionsPacket$Action {
   ADD(0),
   REMOVE(1),
   SET(2);

   private static final IntFunction<ClientboundCustomChatCompletionsPacket$Action> BY_ID = ByIdMap.continuous(
      a -> a.id, values(), ByIdMap$OutOfBoundsStrategy.ZERO
   );
   public static final StreamCodec<ByteBuf, ClientboundCustomChatCompletionsPacket$Action> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, a -> a.id);
   private final int id;

   private ClientboundCustomChatCompletionsPacket$Action(final int id) {
      this.id = id;
   }
}
