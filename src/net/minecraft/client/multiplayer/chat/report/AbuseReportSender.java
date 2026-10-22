package net.minecraft.client.multiplayer.chat.report;

import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.minecraft.report.AbuseReport;
import com.mojang.authlib.minecraft.report.AbuseReportLimits;
import com.mojang.datafixers.util.Unit;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface AbuseReportSender {
   static AbuseReportSender create(final ReportEnvironment environment, final UserApiService userApiService) {
      return new AbuseReportSender$Services(environment, userApiService);
   }

   CompletableFuture<Unit> send(UUID id, ReportType reportType, AbuseReport report);

   boolean isEnabled();

   default AbuseReportLimits reportLimits() {
      return AbuseReportLimits.DEFAULTS;
   }
}
