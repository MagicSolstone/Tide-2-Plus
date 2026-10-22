package net.minecraft.network.chat;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;
import net.minecraft.util.StringRepresentable;

public enum ChatTypeDecoration$Parameter implements StringRepresentable {
   SENDER(0, "sender", (content, chatType) -> chatType.name()),
   TARGET(1, "target", (content, chatType) -> chatType.targetName().orElse(CommonComponents.EMPTY)),
   CONTENT(2, "content", (content, chatType) -> content);

   private static final IntFunction<ChatTypeDecoration$Parameter> BY_ID = ByIdMap.continuous(p -> p.id, values(), ByIdMap$OutOfBoundsStrategy.ZERO);
   public static final Codec<ChatTypeDecoration$Parameter> CODEC = StringRepresentable.fromEnum(ChatTypeDecoration$Parameter::values);
   public static final StreamCodec<ByteBuf, ChatTypeDecoration$Parameter> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, p -> p.id);
   private final int id;
   private final String name;
   private final ChatTypeDecoration$Parameter$Selector selector;

   private ChatTypeDecoration$Parameter(final int id, final String name, final ChatTypeDecoration$Parameter$Selector selector) {
      this.id = id;
      this.name = name;
      this.selector = selector;
   }

   public Component select(final Component content, final ChatType$Bound chatType) {
      return this.selector.select(content, chatType);
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
