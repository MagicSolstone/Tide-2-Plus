package net.minecraft.client.multiplayer.chat.report;

import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.chat.ChatLog;
import net.minecraft.client.multiplayer.chat.LoggedChatMessage$Player;
import net.minecraft.network.chat.PlayerChatMessage;

@Environment(EnvType.CLIENT)
public class ChatReportContextBuilder {
   private final int leadingCount;
   private final List<ChatReportContextBuilder$Collector> activeCollectors = new ArrayList<>();

   public ChatReportContextBuilder(final int leadingCount) {
      this.leadingCount = leadingCount;
   }

   public void collectAllContext(final ChatLog chatLog, final IntCollection roots, final ChatReportContextBuilder$Handler handler) {
      IntSortedSet uncollectedRoots = new IntRBTreeSet(roots);

      for (int id = uncollectedRoots.lastInt(); id >= chatLog.start() && (this.isActive() || !uncollectedRoots.isEmpty()); id--) {
         if (chatLog.lookup(id) instanceof LoggedChatMessage$Player event) {
            boolean context = this.acceptContext(event.message());
            if (uncollectedRoots.remove(id)) {
               this.trackContext(event.message());
               handler.accept(id, event);
            } else if (context) {
               handler.accept(id, event);
            }
         }
      }
   }

   public void trackContext(final PlayerChatMessage message) {
      this.activeCollectors.add(new ChatReportContextBuilder$Collector(this, message));
   }

   public boolean acceptContext(final PlayerChatMessage message) {
      boolean collected = false;
      Iterator<ChatReportContextBuilder$Collector> iterator = this.activeCollectors.iterator();

      while (iterator.hasNext()) {
         ChatReportContextBuilder$Collector collector = iterator.next();
         if (collector.accept(message)) {
            collected = true;
            if (collector.isComplete()) {
               iterator.remove();
            }
         }
      }

      return collected;
   }

   public boolean isActive() {
      return !this.activeCollectors.isEmpty();
   }
}
