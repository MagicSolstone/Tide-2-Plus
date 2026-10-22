package net.minecraft.client.gui.components.debug;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.device.DeviceInfo;
import com.mojang.renderpearl.api.device.DeviceType;
import java.util.List;
import java.util.Locale;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

@Environment(EnvType.CLIENT)
public class DebugEntrySystemSpecs implements DebugScreenEntry {
   private static final Identifier GROUP = Identifier.withDefaultNamespace("system");
   @Nullable
   private static String cpuInfo;

   public static String getCpuInfo() {
      if (cpuInfo == null) {
         cpuInfo = "<unknown>";

         try {
            CentralProcessor processor = new SystemInfo().getHardware().getProcessor();
            cpuInfo = String.format(Locale.ROOT, "%dx %s", processor.getLogicalProcessorCount(), processor.getProcessorIdentifier().getName())
               .replaceAll("\\s+", " ");
         } catch (Throwable var1) {
         }
      }

      return cpuInfo;
   }

   @Override
   public void display(
      final DebugScreenDisplayer displayer,
      @Nullable final Level serverOrClientLevel,
      @Nullable final LevelChunk clientChunk,
      @Nullable final LevelChunk serverChunk
   ) {
      DeviceInfo deviceInfo = RenderSystem.getDevice().getDeviceInfo();
      Window window = Minecraft.getInstance().getWindow();
      displayer.addToGroup(
         GROUP,
         List.of(
            String.format(Locale.ROOT, "Java: %s", System.getProperty("java.version")),
            String.format(Locale.ROOT, "CPU: %s", getCpuInfo()),
            String.format(Locale.ROOT, "Display: %dx%d (%s)", window.getWidth(), window.getHeight(), deviceInfo.vendorName()),
            String.format(Locale.ROOT, "Window: %dx%d (%.2fx pixel density)", window.getScreenWidth(), window.getScreenHeight(), window.getPixelDensity()),
            String.format(Locale.ROOT, "%s%s", deviceInfo.name(), this.typeName(deviceInfo.type())),
            String.format(Locale.ROOT, "%s %s", deviceInfo.backendName(), this.firstLine(deviceInfo.driverInfo()))
         )
      );
   }

   private String firstLine(final String value) {
      return value.lines().findFirst().orElse(value);
   }

   private String typeName(final DeviceType type) {
      return switch (type) {
         case OTHER -> "";
         case INTEGRATED -> " (iGPU)";
         case DISCRETE -> " (dGPU)";
         case VIRTUAL -> " (vGPU)";
         case CPU -> " (software)";
      };
   }

   @Override
   public boolean isAllowed(final boolean reducedDebugInfo) {
      return true;
   }
}
