package net.minecraft.world.entity.animal.sniffer;

import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;

public enum Sniffer$State {
   IDLING(0),
   FEELING_HAPPY(1),
   SCENTING(2),
   SNIFFING(3),
   SEARCHING(4),
   DIGGING(5),
   RISING(6);

   public static final IntFunction<Sniffer$State> BY_ID = ByIdMap.continuous(Sniffer$State::id, values(), ByIdMap$OutOfBoundsStrategy.ZERO);
   public static final StreamCodec<ByteBuf, Sniffer$State> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Sniffer$State::id);
   private final int id;

   private Sniffer$State(final int id) {
      this.id = id;
   }

   public int id() {
      return this.id;
   }
}
