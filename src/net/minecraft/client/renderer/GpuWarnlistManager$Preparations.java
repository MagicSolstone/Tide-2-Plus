package net.minecraft.client.renderer;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.device.DeviceInfo;
import com.mojang.renderpearl.api.device.GpuDevice;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class GpuWarnlistManager$Preparations {
   private final List<Pattern> rendererPatterns;
   private final List<Pattern> versionPatterns;
   private final List<Pattern> vendorPatterns;

   private GpuWarnlistManager$Preparations(final List<Pattern> rendererPatterns, final List<Pattern> versionPatterns, final List<Pattern> vendorPatterns) {
      this.rendererPatterns = rendererPatterns;
      this.versionPatterns = versionPatterns;
      this.vendorPatterns = vendorPatterns;
   }

   private static String matchAny(final List<Pattern> patterns, final String input) {
      List<String> allMatches = Lists.newArrayList();

      for (Pattern pattern : patterns) {
         Matcher matcher = pattern.matcher(input);

         while (matcher.find()) {
            allMatches.add(matcher.group());
         }
      }

      return String.join(", ", allMatches);
   }

   private ImmutableMap<String, String> apply() {
      Builder<String, String> map = new Builder();
      GpuDevice device = RenderSystem.getDevice();
      DeviceInfo deviceInfo = device.getDeviceInfo();
      if (deviceInfo.backendName().equals("OpenGL")) {
         String rendererFails = matchAny(this.rendererPatterns, deviceInfo.name());
         if (!rendererFails.isEmpty()) {
            map.put("renderer", rendererFails);
         }

         String versionFails = matchAny(this.versionPatterns, deviceInfo.driverInfo());
         if (!versionFails.isEmpty()) {
            map.put("version", versionFails);
         }

         String vendorFails = matchAny(this.vendorPatterns, deviceInfo.vendorName());
         if (!vendorFails.isEmpty()) {
            map.put("vendor", vendorFails);
         }
      }

      return map.build();
   }
}
