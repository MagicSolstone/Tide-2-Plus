package net.minecraft.client.multiplayer.chat.report;

import java.time.Instant;
import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public abstract class Report {
   protected final UUID reportId;
   protected final Instant createdAt;
   protected final UUID reportedProfileId;
   protected String comments = "";
   @Nullable
   protected ReportReason reason;
   protected boolean attested;

   public Report(final UUID reportId, final Instant createdAt, final UUID reportedProfileId) {
      this.reportId = reportId;
      this.createdAt = createdAt;
      this.reportedProfileId = reportedProfileId;
   }

   public boolean isReportedPlayer(final UUID playerId) {
      return playerId.equals(this.reportedProfileId);
   }

   public abstract Report copy();

   public abstract Screen createScreen(Screen lastScreen, ReportingContext context);
}
