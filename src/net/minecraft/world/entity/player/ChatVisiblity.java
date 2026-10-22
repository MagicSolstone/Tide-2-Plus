package net.minecraft.world.entity.player;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;

public enum ChatVisiblity {
   FULL(0, "options.chat.visibility.full"),
   SYSTEM(1, "options.chat.visibility.system"),
   HIDDEN(2, "options.chat.visibility.hidden");

   private static final IntFunction<ChatVisiblity> BY_ID = ByIdMap.continuous(v -> v.id, values(), ByIdMap$OutOfBoundsStrategy.WRAP);
   public static final Codec<ChatVisiblity> LEGACY_CODEC = Codec.INT.xmap(BY_ID::apply, v -> v.id);
   public static final StreamCodec<ByteBuf, ChatVisiblity> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, v -> v.id);
   private final int id;
   private final Component caption;

   private ChatVisiblity(final int id, final String key) {
      this.id = id;
      this.caption = Component.translatable(key);
   }

   public Component caption() {
      return this.caption;
   }
}
