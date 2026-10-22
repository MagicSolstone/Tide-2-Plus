package net.minecraft.client.multiplayer.chat.report;

import com.mojang.authlib.minecraft.report.AbuseReport;
import com.mojang.authlib.minecraft.report.AbuseReportLimits;
import com.mojang.authlib.minecraft.report.ReportedEntity;
import com.mojang.datafixers.util.Either;
import java.time.Instant;
import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class NameReport$Builder extends Report$Builder<NameReport> {
   public NameReport$Builder(final NameReport report, final AbuseReportLimits limits) {
      super(report, limits);
   }

   public NameReport$Builder(final UUID reportedProfileId, final String reportedName, final AbuseReportLimits limits) {
      super(new NameReport(UUID.randomUUID(), Instant.now(), reportedProfileId, reportedName), limits);
   }

   @Override
   public boolean hasContent() {
      return StringUtils.isNotEmpty(this.comments());
   }

   @Nullable
   @Override
   public Report$CannotBuildReason checkBuildable() {
      return this.report.comments.length() > this.limits.maxOpinionCommentsLength() ? Report$CannotBuildReason.COMMENT_TOO_LONG : super.checkBuildable();
   }

   @Override
   public Either<Report$Result, Report$CannotBuildReason> build(final ReportingContext reportingContext) {
      Report$CannotBuildReason error = this.checkBuildable();
      if (error != null) {
         return Either.right(error);
      } else {
         ReportedEntity reportedEntity = new ReportedEntity(this.report.reportedProfileId);
         AbuseReport abuseReport = AbuseReport.name(this.report.comments, reportedEntity, this.report.createdAt);
         return Either.left(new Report$Result(this.report.reportId, ReportType.USERNAME, abuseReport));
      }
   }
}
