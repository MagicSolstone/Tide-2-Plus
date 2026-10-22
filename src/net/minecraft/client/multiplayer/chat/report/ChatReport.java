package net.minecraft.client.multiplayer.chat.report;

import com.mojang.authlib.minecraft.report.AbuseReportLimits;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.time.Instant;
import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.reporting.ChatReportScreen;

@Environment(EnvType.CLIENT)
public class ChatReport extends Report {
   private final IntSet reportedMessages = new IntOpenHashSet();

   private ChatReport(final UUID reportId, final Instant createdAt, final UUID reportedProfileId) {
      super(reportId, createdAt, reportedProfileId);
   }

   public void toggleReported(final int id, final AbuseReportLimits limits) {
      if (this.reportedMessages.contains(id)) {
         this.reportedMessages.remove(id);
      } else if (this.reportedMessages.size() < limits.maxReportedMessageCount()) {
         this.reportedMessages.add(id);
      }
   }

   public ChatReport copy() {
      ChatReport result = new ChatReport(this.reportId, this.createdAt, this.reportedProfileId);
      result.reportedMessages.addAll(this.reportedMessages);
      result.comments = this.comments;
      result.reason = this.reason;
      result.attested = this.attested;
      return result;
   }

   @Override
   public Screen createScreen(final Screen lastScreen, final ReportingContext context) {
      return new ChatReportScreen(lastScreen, context, this);
   }
}
