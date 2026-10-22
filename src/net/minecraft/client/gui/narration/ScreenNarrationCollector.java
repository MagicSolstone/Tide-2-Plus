package net.minecraft.client.gui.narration;

import com.google.common.collect.Maps;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ScreenNarrationCollector {
   private int generation;
   private final Map<ScreenNarrationCollector$EntryKey, ScreenNarrationCollector$NarrationEntry> entries = Maps.newTreeMap(
      Comparator.<ScreenNarrationCollector$EntryKey, NarratedElementType>comparing(e -> e.type).thenComparing(e -> e.depth)
   );

   public void update(final Consumer<NarrationElementOutput> updater, final NarrationTrigger narrationTrigger) {
      this.generation++;
      updater.accept(new ScreenNarrationCollector$Output(this, 0, narrationTrigger));
   }

   public String collectNarrationText(final boolean force) {
      StringBuilder result = new StringBuilder();
      Consumer<String> appender = new ScreenNarrationCollector$1(this, result);
      this.entries.forEach((k, v) -> {
         if (v.generation == this.generation && (force || !v.alreadyNarrated)) {
            v.contents.getText(appender);
            v.alreadyNarrated = true;
         }
      });
      return result.toString();
   }
}
