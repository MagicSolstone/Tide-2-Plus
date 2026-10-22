package net.minecraft.client;

import java.io.PrintWriter;
import java.io.StringWriter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.CrashReportCategory;

@Environment(EnvType.CLIENT)
class ResourceLoadStateTracker$RecoveryInfo {
   private final Throwable error;

   private ResourceLoadStateTracker$RecoveryInfo(final Throwable error) {
      this.error = error;
   }

   public void fillCrashInfo(final CrashReportCategory category) {
      category.setDetail("Recovery", "Yes");
      category.setDetail("Recovery reason", () -> {
         StringWriter writer = new StringWriter();
         this.error.printStackTrace(new PrintWriter(writer));
         return writer.toString();
      });
   }
}
