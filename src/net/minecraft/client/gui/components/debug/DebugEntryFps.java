package net.minecraft.client.gui.components.debug;

import com.mojang.blaze3d.platform.VideoMode;
import com.mojang.renderpearl.api.device.GpuSurface$Configuration;
import com.mojang.renderpearl.api.device.GpuSurface$PresentMode;
import java.util.Locale;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class DebugEntryFps implements DebugScreenEntry {
   @Override
   public void display(
      final DebugScreenDisplayer displayer,
      @Nullable final Level serverOrClientLevel,
      @Nullable final LevelChunk clientChunk,
      @Nullable final LevelChunk serverChunk
   ) {
      Minecraft minecraft = Minecraft.getInstance();
      int framerateLimit = minecraft.getFramerateLimitTracker().getFramerateLimit();
      Optional<GpuSurface$Configuration> surfaceConfiguration = minecraft.windowSurface().currentConfiguration();
      VideoMode activeMode = minecraft.getWindow().getActiveVideoMode();
      displayer.addPriorityLine(
         String.format(
            Locale.ROOT,
            "%d fps T: %s%s @%sHz",
            minecraft.getFps(),
            framerateLimit == 260 ? "inf" : framerateLimit,
            presentModeName(surfaceConfiguration.map(GpuSurface$Configuration::presentMode).orElse(null)),
            activeMode == null ? "0" : activeMode.refreshRateLabel()
         )
      );
   }

   @Override
   public boolean isAllowed(final boolean reducedDebugInfo) {
      return true;
   }

   private static String presentModeName(@Nullable final GpuSurface$PresentMode mode) {
      return switch (mode) {
         case null -> "";
         case IMMEDIATE -> " (immediate)";
         case MAILBOX -> " (mailbox)";
         case FIFO -> " (fifo)";
         case FIFO_RELAXED -> " (fifo relaxed)";
      };
   }
}
