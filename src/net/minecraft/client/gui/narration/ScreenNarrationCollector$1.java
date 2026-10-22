package net.minecraft.client.gui.narration;

import java.util.Objects;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class ScreenNarrationCollector$1 implements Consumer<String> {
   private boolean firstEntry;

   ScreenNarrationCollector$1(final ScreenNarrationCollector this$0, final StringBuilder val$result) {
      this.val$result = val$resultx;
      Objects.requireNonNull(this$0);
      super();
      this.firstEntry = true;
   }

   public void accept(final String s) {
      if (!this.firstEntry) {
         this.val$result.append(". ");
      }

      this.firstEntry = false;
      this.val$result.append(s);
   }
}
