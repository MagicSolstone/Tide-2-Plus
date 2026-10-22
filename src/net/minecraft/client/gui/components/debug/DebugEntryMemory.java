package net.minecraft.client.gui.components.debug;

import java.util.List;
import java.util.Locale;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class DebugEntryMemory implements DebugScreenEntry {
   private static final Identifier GROUP = Identifier.withDefaultNamespace("memory");
   private final DebugEntryMemory$AllocationRateCalculator allocationRateCalculator = new DebugEntryMemory$AllocationRateCalculator();

   @Override
   public void display(
      final DebugScreenDisplayer displayer,
      @Nullable final Level serverOrClientLevel,
      @Nullable final LevelChunk clientChunk,
      @Nullable final LevelChunk serverChunk
   ) {
      long max = Runtime.getRuntime().maxMemory();
      long total = Runtime.getRuntime().totalMemory();
      long free = Runtime.getRuntime().freeMemory();
      long used = total - free;
      displayer.addToGroup(
         GROUP,
         List.of(
            String.format(Locale.ROOT, "Mem: %2d%% %03d/%03dMiB", used * 100L / max, bytesToMebibytes(used), bytesToMebibytes(max)),
            String.format(Locale.ROOT, "Allocation rate: %03dMiB/s", bytesToMebibytes(this.allocationRateCalculator.bytesAllocatedPerSecond(used))),
            String.format(Locale.ROOT, "Allocated: %2d%% %03dMiB", total * 100L / max, bytesToMebibytes(total))
         )
      );
   }

   private static long bytesToMebibytes(final long used) {
      return used / 1024L / 1024L;
   }

   @Override
   public boolean isAllowed(final boolean reducedDebugInfo) {
      return true;
   }
}
