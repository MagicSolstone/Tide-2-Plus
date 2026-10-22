package net.minecraft.client.gui.components.debug;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.clock.ClockManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.timeline.Timelines;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class DebugEntryDayCount implements DebugScreenEntry {
   @Override
   public void display(
      final DebugScreenDisplayer displayer,
      @Nullable final Level serverOrClientLevel,
      @Nullable final LevelChunk clientChunk,
      @Nullable final LevelChunk serverChunk
   ) {
      if (serverOrClientLevel != null) {
         ClockManager clockManager = serverOrClientLevel.clockManager();
         serverOrClientLevel.registryAccess()
            .get(Timelines.OVERWORLD_DAY)
            .ifPresent(timeline -> displayer.addLine("Day #" + timeline.value().getPeriodCount(clockManager)));
      }
   }
}
