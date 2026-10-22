package net.minecraft.client.gui.narration;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class ScreenNarrationCollector$Output implements NarrationElementOutput {
   private final int depth;
   private final NarrationTrigger narrationTrigger;

   private ScreenNarrationCollector$Output(final ScreenNarrationCollector this$0, final int depth, final NarrationTrigger narrationTrigger) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.depth = depth;
      this.narrationTrigger = narrationTrigger;
   }

   @Override
   public void add(final NarratedElementType type, final NarrationThunk<?> contents) {
      this.this$0
         .entries
         .computeIfAbsent(new ScreenNarrationCollector$EntryKey(type, this.depth), k -> new ScreenNarrationCollector$NarrationEntry())
         .update(this.this$0.generation, contents);
   }

   @Override
   public NarrationElementOutput nest() {
      return new ScreenNarrationCollector$Output(this.this$0, this.depth + 1, this.narrationTrigger);
   }

   @Override
   public NarrationTrigger narrationTrigger() {
      return this.narrationTrigger;
   }
}
