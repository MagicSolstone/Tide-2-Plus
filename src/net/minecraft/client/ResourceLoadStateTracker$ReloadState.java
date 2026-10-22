package net.minecraft.client;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.CrashReportCategory;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class ResourceLoadStateTracker$ReloadState {
   private final ResourceLoadStateTracker$ReloadReason reloadReason;
   private final List<String> packs;
   @Nullable
   private ResourceLoadStateTracker$RecoveryInfo recoveryReloadInfo;
   private boolean finished;

   private ResourceLoadStateTracker$ReloadState(final ResourceLoadStateTracker$ReloadReason reloadReason, final List<String> packs) {
      this.reloadReason = reloadReason;
      this.packs = packs;
   }

   public void fillCrashInfo(final CrashReportCategory category) {
      category.setDetail("Reload reason", this.reloadReason.name);
      category.setDetail("Finished", this.finished ? "Yes" : "No");
      category.setDetail("Packs", () -> String.join(", ", this.packs));
      if (this.recoveryReloadInfo != null) {
         this.recoveryReloadInfo.fillCrashInfo(category);
      }
   }
}
