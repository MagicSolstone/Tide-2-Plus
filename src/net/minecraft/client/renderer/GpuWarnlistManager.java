package net.minecraft.client.renderer;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.regex.Pattern;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.StrictJsonParser;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.profiling.Zone;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class GpuWarnlistManager extends SimplePreparableReloadListener<GpuWarnlistManager$Preparations> {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final Identifier GPU_WARNLIST_LOCATION = Identifier.withDefaultNamespace("gpu_warnlist.json");
   private ImmutableMap<String, String> warnings = ImmutableMap.of();
   private boolean showWarning;
   private boolean warningDismissed;

   public boolean hasWarnings() {
      return !this.warnings.isEmpty();
   }

   public boolean willShowWarning() {
      return this.hasWarnings() && !this.warningDismissed;
   }

   public void showWarning() {
      this.showWarning = true;
   }

   public void dismissWarning() {
      this.warningDismissed = true;
   }

   public boolean isShowingWarning() {
      return this.showWarning && !this.warningDismissed;
   }

   public void resetWarnings() {
      this.showWarning = false;
      this.warningDismissed = false;
   }

   @Nullable
   public String getRendererWarnings() {
      return (String)this.warnings.get("renderer");
   }

   @Nullable
   public String getVersionWarnings() {
      return (String)this.warnings.get("version");
   }

   @Nullable
   public String getVendorWarnings() {
      return (String)this.warnings.get("vendor");
   }

   @Nullable
   public String getAllWarnings() {
      StringBuilder sb = new StringBuilder();
      this.warnings.forEach((k, v) -> sb.append(k).append(": ").append(v));
      return sb.isEmpty() ? null : sb.toString();
   }

   protected GpuWarnlistManager$Preparations prepare(final ResourceManager manager, final ProfilerFiller profiler) {
      List<Pattern> rendererPatterns = Lists.newArrayList();
      List<Pattern> versionPatterns = Lists.newArrayList();
      List<Pattern> vendorPatterns = Lists.newArrayList();
      JsonObject root = parseJson(manager, profiler);
      if (root != null) {
         try (Zone ignored = profiler.zone("compile_regex")) {
            compilePatterns(root.getAsJsonArray("renderer"), rendererPatterns);
            compilePatterns(root.getAsJsonArray("version"), versionPatterns);
            compilePatterns(root.getAsJsonArray("vendor"), vendorPatterns);
         }
      }

      return new GpuWarnlistManager$Preparations(rendererPatterns, versionPatterns, vendorPatterns);
   }

   protected void apply(final GpuWarnlistManager$Preparations preparations, final ResourceManager manager, final ProfilerFiller profiler) {
      this.warnings = preparations.apply();
   }

   private static void compilePatterns(final JsonArray jsonArray, final List<Pattern> patternList) {
      jsonArray.forEach(e -> patternList.add(Pattern.compile(e.getAsString(), 2)));
   }

   @Nullable
   private static JsonObject parseJson(final ResourceManager manager, final ProfilerFiller profiler) {
      try {
         JsonObject var4;
         try (
            Zone ignored = profiler.zone("parse_json");
            Reader resource = manager.openAsReader(GPU_WARNLIST_LOCATION);
         ) {
            var4 = StrictJsonParser.parse(resource).getAsJsonObject();
         }

         return var4;
      } catch (JsonSyntaxException | IOException var10) {
         LOGGER.warn("Failed to load GPU warnlist", var10);
         return null;
      }
   }
}
