package net.minecraft.client.multiplayer.chat.report;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Objects;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.PlayerChatMessage;

@Environment(EnvType.CLIENT)
class ChatReportContextBuilder$Collector {
   private final Set<MessageSignature> lastSeenSignatures;
   private PlayerChatMessage lastChainMessage;
   private boolean collectingChain;
   private int count;

   private ChatReportContextBuilder$Collector(final ChatReportContextBuilder this$0, final PlayerChatMessage fromMessage) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.collectingChain = true;
      this.lastSeenSignatures = new ObjectOpenHashSet(fromMessage.signedBody().lastSeen().entries());
      this.lastChainMessage = fromMessage;
   }

   private boolean accept(final PlayerChatMessage message) {
      if (message.equals(this.lastChainMessage)) {
         return false;
      } else {
         boolean selected = this.lastSeenSignatures.remove(message.signature());
         if (this.collectingChain && this.lastChainMessage.sender().equals(message.sender())) {
            if (this.lastChainMessage.link().isDescendantOf(message.link())) {
               selected = true;
               this.lastChainMessage = message;
            } else {
               this.collectingChain = false;
            }
         }

         if (selected) {
            this.count++;
         }

         return selected;
      }
   }

   private boolean isComplete() {
      return this.count >= this.this$0.leadingCount || !this.collectingChain && this.lastSeenSignatures.isEmpty();
   }
}
