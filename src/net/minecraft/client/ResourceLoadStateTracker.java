package net.minecraft.client;

import com.google.common.collect.ImmutableList;
import com.mojang.logging.LogUtils;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.server.packs.PackResources;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class ResourceLoadStateTracker {
   private static final Logger LOGGER = LogUtils.getLogger();
   @Nullable
   private ResourceLoadStateTracker$ReloadState reloadState;
   private int reloadCount;

   public void startReload(final ResourceLoadStateTracker$ReloadReason reloadReason, final List<PackResources> packs) {
      this.reloadCount++;
      if (this.reloadState != null && !this.reloadState.finished) {
         LOGGER.warn("Reload already ongoing, replacing");
      }

      this.reloadState = new ResourceLoadStateTracker$ReloadState(
         reloadReason, packs.stream().map(PackResources::packId).collect(ImmutableList.toImmutableList())
      );
   }

   public void startRecovery(final Throwable reason) {
      if (this.reloadState == null) {
         LOGGER.warn("Trying to signal reload recovery, but nothing was started");
         this.reloadState = new ResourceLoadStateTracker$ReloadState(ResourceLoadStateTracker$ReloadReason.UNKNOWN, ImmutableList.of());
      }

      this.reloadState.recoveryReloadInfo = new ResourceLoadStateTracker$RecoveryInfo(reason);
   }

   public void finishReload() {
      if (this.reloadState == null) {
         LOGGER.warn("Trying to finish reload, but nothing was started");
      } else {
         this.reloadState.finished = true;
      }
   }

   public void fillCrashReport(final CrashReport report) {
      CrashReportCategory category = report.addCategory("Last reload");
      category.setDetail("Reload number", this.reloadCount);
      if (this.reloadState != null) {
         this.reloadState.fillCrashInfo(category);
      }
   }
}
