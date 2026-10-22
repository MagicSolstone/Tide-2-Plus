package net.minecraft.network.protocol.game;

import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;

public enum ClientboundLevelParticlesPacket$RandomizationType {
   DEFAULT(0),
   ALTERNATIVE(1),
   ALTERNATIVE_WITH_SPEED(2);

   private static final IntFunction<ClientboundLevelParticlesPacket$RandomizationType> BY_ID = ByIdMap.continuous(
      h -> h.id, values(), ByIdMap$OutOfBoundsStrategy.ZERO
   );
   public static final StreamCodec<ByteBuf, ClientboundLevelParticlesPacket$RandomizationType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, h -> h.id);
   private final int id;

   private ClientboundLevelParticlesPacket$RandomizationType(final int id) {
      this.id = id;
   }

   public boolean isAlternative() {
      return this != DEFAULT;
   }
}
