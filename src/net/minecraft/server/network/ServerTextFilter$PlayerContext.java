package net.minecraft.server.network;

import com.google.common.collect.ImmutableList;
import com.mojang.authlib.GameProfile;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.util.Util;
import net.minecraft.util.thread.ConsecutiveExecutor;

public class ServerTextFilter$PlayerContext implements TextFilter {
   protected final GameProfile profile;
   protected final Executor streamExecutor;

   protected ServerTextFilter$PlayerContext(final ServerTextFilter this$0, final GameProfile profile) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.profile = profile;
      ConsecutiveExecutor streamProcessor = new ConsecutiveExecutor(this$0.workerPool, "chat stream for " + profile.name());
      this.streamExecutor = streamProcessor::schedule;
   }

   @Override
   public CompletableFuture<List<FilteredText>> processMessageBundle(final List<String> messages) {
      List<CompletableFuture<FilteredText>> requests = messages.stream()
         .map(message -> this.this$0.requestMessageProcessing(this.profile, message, this.this$0.chatIgnoreStrategy, this.streamExecutor))
         .collect(ImmutableList.toImmutableList());
      return Util.sequenceFailFast(requests).exceptionally(e -> ImmutableList.of());
   }

   @Override
   public CompletableFuture<FilteredText> processStreamMessage(final String message) {
      return this.this$0.requestMessageProcessing(this.profile, message, this.this$0.chatIgnoreStrategy, this.streamExecutor);
   }
}
