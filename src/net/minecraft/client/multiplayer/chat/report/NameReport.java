package net.minecraft.client.multiplayer.chat.report;

import java.time.Instant;
import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.reporting.NameReportScreen;

@Environment(EnvType.CLIENT)
public class NameReport extends Report {
   private final String reportedName;

   private NameReport(final UUID reportId, final Instant createdAt, final UUID reportedProfileId, final String reportedName) {
      super(reportId, createdAt, reportedProfileId);
      this.reportedName = reportedName;
   }

   public String getReportedName() {
      return this.reportedName;
   }

   public NameReport copy() {
      NameReport result = new NameReport(this.reportId, this.createdAt, this.reportedProfileId, this.reportedName);
      result.comments = this.comments;
      result.attested = this.attested;
      return result;
   }

   @Override
   public Screen createScreen(final Screen lastScreen, final ReportingContext context) {
      return new NameReportScreen(lastScreen, context, this);
   }
}
