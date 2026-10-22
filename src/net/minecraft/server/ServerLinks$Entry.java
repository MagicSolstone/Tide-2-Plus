package net.minecraft.server;

import com.mojang.datafixers.util.Either;
import java.net.URI;
import net.minecraft.network.chat.Component;

public record ServerLinks$Entry(Either<ServerLinks$KnownLinkType, Component> type, URI link) {
   public static ServerLinks$Entry knownType(final ServerLinks$KnownLinkType type, final URI link) {
      return new ServerLinks$Entry(Either.left(type), link);
   }

   public static ServerLinks$Entry custom(final Component displayName, final URI link) {
      return new ServerLinks$Entry(Either.right(displayName), link);
   }

   public Component displayName() {
      return (Component)this.type.map(ServerLinks$KnownLinkType::displayName, r -> r);
   }
}
