package net.minecraft.server.network;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

class TextFilter$1 implements TextFilter {
   @Override
   public CompletableFuture<FilteredText> processStreamMessage(final String message) {
      return CompletableFuture.completedFuture(FilteredText.passThrough(message));
   }

   @Override
   public CompletableFuture<List<FilteredText>> processMessageBundle(final List<String> messages) {
      return CompletableFuture.completedFuture(messages.stream().map(FilteredText::passThrough).collect(ImmutableList.toImmutableList()));
   }
}
