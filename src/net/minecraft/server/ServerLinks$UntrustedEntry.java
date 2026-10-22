package net.minecraft.server;

import com.mojang.datafixers.util.Either;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ServerLinks$UntrustedEntry(Either<ServerLinks$KnownLinkType, Component> type, String link) {
   public static final StreamCodec<ByteBuf, ServerLinks$UntrustedEntry> STREAM_CODEC = StreamCodec.composite(
      ServerLinks.TYPE_STREAM_CODEC,
      ServerLinks$UntrustedEntry::type,
      ByteBufCodecs.STRING_UTF8,
      ServerLinks$UntrustedEntry::link,
      ServerLinks$UntrustedEntry::new
   );
}
