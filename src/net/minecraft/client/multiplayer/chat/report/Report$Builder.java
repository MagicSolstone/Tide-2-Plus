package net.minecraft.client.multiplayer.chat.report;

import com.mojang.authlib.minecraft.report.AbuseReportLimits;
import com.mojang.datafixers.util.Either;
import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public abstract class Report$Builder<R extends Report> {
   protected final R report;
   protected final AbuseReportLimits limits;

   protected Report$Builder(final R report, final AbuseReportLimits limits) {
      this.report = report;
      this.limits = limits;
   }

   public R report() {
      return this.report;
   }

   public UUID reportedProfileId() {
      return this.report.reportedProfileId;
   }

   public String comments() {
      return this.report.comments;
   }

   public boolean attested() {
      return this.report().attested;
   }

   public void setComments(final String comments) {
      this.report.comments = comments;
   }

   @Nullable
   public ReportReason reason() {
      return this.report.reason;
   }

   public void setReason(final ReportReason reason) {
      this.report.reason = reason;
   }

   public void setAttested(final boolean attested) {
      this.report.attested = attested;
   }

   public abstract boolean hasContent();

   @Nullable
   public Report$CannotBuildReason checkBuildable() {
      return !this.report().attested ? Report$CannotBuildReason.NOT_ATTESTED : null;
   }

   public abstract Either<Report$Result, Report$CannotBuildReason> build(ReportingContext reportingContext);
}
