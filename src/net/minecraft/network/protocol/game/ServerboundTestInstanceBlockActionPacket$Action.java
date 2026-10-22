package net.minecraft.network.protocol.game;

import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;

public enum ServerboundTestInstanceBlockActionPacket$Action {
   INIT(0),
   QUERY(1),
   SET(2),
   RESET(3),
   SAVE(4),
   EXPORT(5),
   RUN(6);

   private static final IntFunction<ServerboundTestInstanceBlockActionPacket$Action> BY_ID = ByIdMap.continuous(
      e -> e.id, values(), ByIdMap$OutOfBoundsStrategy.ZERO
   );
   public static final StreamCodec<ByteBuf, ServerboundTestInstanceBlockActionPacket$Action> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, e -> e.id);
   private final int id;

   private ServerboundTestInstanceBlockActionPacket$Action(final int id) {
      this.id = id;
   }
}
