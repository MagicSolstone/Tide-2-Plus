package net.minecraft.server.network;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface TextFilter {
   TextFilter DUMMY = new TextFilter$1();

   default void join() {
   }

   default void leave() {
   }

   CompletableFuture<FilteredText> processStreamMessage(String message);

   CompletableFuture<List<FilteredText>> processMessageBundle(List<String> messages);
}
