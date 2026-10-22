package net.minecraft.client.gui.components.debug;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class DebugEntryVersion implements DebugScreenEntry {
   @Override
   public void display(
      final DebugScreenDisplayer displayer, @Nullable final Level level, @Nullable final LevelChunk clientChunk, @Nullable final LevelChunk serverChunk
   ) {
      displayer.addPriorityLine(
         "Minecraft "
            + SharedConstants.getCurrentVersion().name()
            + " ("
            + Minecraft.getInstance().getLaunchedVersion()
            + "/"
            + ClientBrandRetriever.getClientModName()
            + ")"
      );
   }

   @Override
   public boolean isAllowed(final boolean reducedDebugInfo) {
      return true;
   }
}
