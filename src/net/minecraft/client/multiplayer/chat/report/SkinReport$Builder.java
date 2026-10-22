package net.minecraft.client.multiplayer.chat.report;

import com.mojang.authlib.minecraft.report.AbuseReport;
import com.mojang.authlib.minecraft.report.AbuseReportLimits;
import com.mojang.authlib.minecraft.report.ReportedEntity;
import com.mojang.datafixers.util.Either;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.ClientAsset$DownloadedTexture;
import net.minecraft.world.entity.player.PlayerSkin;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class SkinReport$Builder extends Report$Builder<SkinReport> {
   public SkinReport$Builder(final SkinReport report, final AbuseReportLimits limits) {
      super(report, limits);
   }

   public SkinReport$Builder(final UUID reportedProfileId, final Supplier<PlayerSkin> skin, final AbuseReportLimits limits) {
      super(new SkinReport(UUID.randomUUID(), Instant.now(), reportedProfileId, skin), limits);
   }

   @Override
   public boolean hasContent() {
      return StringUtils.isNotEmpty(this.comments()) || this.reason() != null;
   }

   @Nullable
   @Override
   public Report$CannotBuildReason checkBuildable() {
      if (this.report.reason == null) {
         return Report$CannotBuildReason.NO_REASON;
      } else {
         return this.report.comments.length() > this.limits.maxOpinionCommentsLength() ? Report$CannotBuildReason.COMMENT_TOO_LONG : super.checkBuildable();
      }
   }

   @Override
   public Either<Report$Result, Report$CannotBuildReason> build(final ReportingContext reportingContext) {
      Report$CannotBuildReason error = this.checkBuildable();
      if (error != null) {
         return Either.right(error);
      } else {
         String reason = Objects.requireNonNull(this.report.reason).backendName();
         ReportedEntity reportedEntity = new ReportedEntity(this.report.reportedProfileId);
         PlayerSkin skin = this.report.skinGetter.get();
         String skinUrl = skin.body() instanceof ClientAsset$DownloadedTexture downloadedTexture ? downloadedTexture.url() : null;
         AbuseReport abuseReport = AbuseReport.skin(this.report.comments, reason, skinUrl, reportedEntity, this.report.createdAt);
         return Either.left(new Report$Result(this.report.reportId, ReportType.SKIN, abuseReport));
      }
   }
}
