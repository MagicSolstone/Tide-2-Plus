package net.minecraft.server.network;

import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;

class ServerCommandSuggestionsProvider$Request {
   private final String command;
   private final CompletableFuture<Suggestions> future = new CompletableFuture<>();

   private ServerCommandSuggestionsProvider$Request(final String command) {
      this.command = command;
   }
}
