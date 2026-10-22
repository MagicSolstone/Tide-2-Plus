package net.minecraft.client.gui.components.debug;

import java.util.Locale;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class DebugEntryPlayerSpeed implements DebugScreenEntry {
   @Override
   public void display(
      final DebugScreenDisplayer displayer,
      @Nullable final Level serverOrClientLevel,
      @Nullable final LevelChunk clientChunk,
      @Nullable final LevelChunk serverChunk
   ) {
      if (Minecraft.getInstance().getCameraEntity() != null) {
         displayer.addToGroup(
            DebugEntryPosition.GROUP, String.format(Locale.ROOT, "Speed: %.3f blocks/tick", Minecraft.getInstance().getCameraEntity().getKnownSpeed().length())
         );
      }
   }
}
