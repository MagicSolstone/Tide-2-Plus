package net.minecraft.server;

import com.mojang.datafixers.util.Either;
import io.netty.buffer.ByteBuf;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ServerLinks(List<ServerLinks$Entry> entries) {
   public static final ServerLinks EMPTY = new ServerLinks(List.of());
   public static final StreamCodec<ByteBuf, Either<ServerLinks$KnownLinkType, Component>> TYPE_STREAM_CODEC = ByteBufCodecs.either(
      ServerLinks$KnownLinkType.STREAM_CODEC, ComponentSerialization.TRUSTED_CONTEXT_FREE_STREAM_CODEC
   );
   public static final StreamCodec<ByteBuf, List<ServerLinks$UntrustedEntry>> UNTRUSTED_LINKS_STREAM_CODEC = ServerLinks$UntrustedEntry.STREAM_CODEC
      .apply(ByteBufCodecs.list());

   public boolean isEmpty() {
      return this.entries.isEmpty();
   }

   public Optional<ServerLinks$Entry> findKnownType(final ServerLinks$KnownLinkType type) {
      return this.entries.stream().filter(e -> (Boolean)e.type.map(l -> l == type, r -> false)).findFirst();
   }

   public List<ServerLinks$UntrustedEntry> untrust() {
      return this.entries.stream().map(e -> new ServerLinks$UntrustedEntry(e.type, e.link.toString())).toList();
   }
}
