package net.minecraft.client.multiplayer.chat.report;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.reporting.SkinReportScreen;
import net.minecraft.world.entity.player.PlayerSkin;

@Environment(EnvType.CLIENT)
public class SkinReport extends Report {
   private final Supplier<PlayerSkin> skinGetter;

   private SkinReport(final UUID reportId, final Instant createdAt, final UUID reportedProfileId, final Supplier<PlayerSkin> skinGetter) {
      super(reportId, createdAt, reportedProfileId);
      this.skinGetter = skinGetter;
   }

   public Supplier<PlayerSkin> getSkinGetter() {
      return this.skinGetter;
   }

   public SkinReport copy() {
      SkinReport result = new SkinReport(this.reportId, this.createdAt, this.reportedProfileId, this.skinGetter);
      result.comments = this.comments;
      result.reason = this.reason;
      result.attested = this.attested;
      return result;
   }

   @Override
   public Screen createScreen(final Screen lastScreen, final ReportingContext context) {
      return new SkinReportScreen(lastScreen, context, this);
   }
}
