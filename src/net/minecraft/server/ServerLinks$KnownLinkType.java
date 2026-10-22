package net.minecraft.server;

import io.netty.buffer.ByteBuf;
import java.net.URI;
import java.util.function.IntFunction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;

public enum ServerLinks$KnownLinkType {
   BUG_REPORT(0, "report_bug"),
   COMMUNITY_GUIDELINES(1, "community_guidelines"),
   SUPPORT(2, "support"),
   STATUS(3, "status"),
   FEEDBACK(4, "feedback"),
   COMMUNITY(5, "community"),
   WEBSITE(6, "website"),
   FORUMS(7, "forums"),
   NEWS(8, "news"),
   ANNOUNCEMENTS(9, "announcements");

   private static final IntFunction<ServerLinks$KnownLinkType> BY_ID = ByIdMap.continuous(e -> e.id, values(), ByIdMap$OutOfBoundsStrategy.ZERO);
   public static final StreamCodec<ByteBuf, ServerLinks$KnownLinkType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, e -> e.id);
   private final int id;
   private final String name;

   private ServerLinks$KnownLinkType(final int id, final String name) {
      this.id = id;
      this.name = name;
   }

   private Component displayName() {
      return Component.translatable("known_server_link." + this.name);
   }

   public ServerLinks$Entry create(final URI link) {
      return ServerLinks$Entry.knownType(this, link);
   }
}
