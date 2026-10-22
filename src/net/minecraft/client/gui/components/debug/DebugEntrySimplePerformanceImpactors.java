package net.minecraft.client.gui.components.debug;

import java.util.Locale;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.TextureFilteringMethod;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class DebugEntrySimplePerformanceImpactors implements DebugScreenEntry {
   @Override
   public void display(
      final DebugScreenDisplayer displayer,
      @Nullable final Level serverOrClientLevel,
      @Nullable final LevelChunk clientChunk,
      @Nullable final LevelChunk serverChunk
   ) {
      Minecraft minecraft = Minecraft.getInstance();
      Options options = minecraft.options;
      displayer.addLine(
         String.format(
            Locale.ROOT,
            "%s%sB: %d",
            options.improvedTransparency().get() ? "improved-transparency " : "",
            options.cloudStatus().get() == CloudStatus.OFF ? "" : (options.cloudStatus().get() == CloudStatus.FAST ? "fast-clouds " : "fancy-clouds "),
            options.biomeBlendRadius().get()
         )
      );
      TextureFilteringMethod filteringMethod = options.textureFiltering().get();
      if (filteringMethod == TextureFilteringMethod.ANISOTROPIC) {
         displayer.addLine(String.format(Locale.ROOT, "Filtering: %s %dx", filteringMethod.caption().getString(), options.maxAnisotropyValue()));
      } else {
         displayer.addLine(String.format(Locale.ROOT, "Filtering: %s", filteringMethod.caption().getString()));
      }

      boolean isMultiDrawIndirect = minecraft.levelRenderer.isChunkRenderingUsingMultiDrawIndirect();
      displayer.addLine(String.format(Locale.ROOT, "Terrain Rendering: %s", isMultiDrawIndirect ? "multidrawindirect" : "naive"));
   }

   @Override
   public boolean isAllowed(final boolean reducedDebugInfo) {
      return true;
   }
}
