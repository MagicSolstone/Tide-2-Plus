package net.minecraft.client.gui.narration;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class ScreenNarrationCollector$NarrationEntry {
   private NarrationThunk<?> contents = NarrationThunk.EMPTY;
   private int generation = -1;
   private boolean alreadyNarrated;

   private ScreenNarrationCollector$NarrationEntry() {
   }

   public ScreenNarrationCollector$NarrationEntry update(final int generation, final NarrationThunk<?> contents) {
      if (!this.contents.equals(contents)) {
         this.contents = contents;
         this.alreadyNarrated = false;
      } else if (this.generation + 1 != generation) {
         this.alreadyNarrated = false;
      }

      this.generation = generation;
      return this;
   }
}
