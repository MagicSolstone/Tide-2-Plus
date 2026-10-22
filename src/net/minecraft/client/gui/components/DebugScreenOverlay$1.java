package net.minecraft.client.gui.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
class DebugScreenOverlay$1 implements DebugScreenDisplayer {
   DebugScreenOverlay$1(final DebugScreenOverlay this$0, final List val$leftLines, final List val$rightLines, final List val$regularLines, final Map val$groups) {
      this.val$leftLines = val$leftLinesx;
      this.val$rightLines = val$rightLinesx;
      this.val$regularLines = val$regularLinesx;
      this.val$groups = val$groupsx;
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public void addPriorityLine(final String line) {
      if (this.val$leftLines.size() > this.val$rightLines.size()) {
         this.val$rightLines.add(line);
      } else {
         this.val$leftLines.add(line);
      }
   }

   @Override
   public void addLine(final String line) {
      this.val$regularLines.add(line);
   }

   @Override
   public void addToGroup(final Identifier group, final Collection<String> lines) {
      this.val$groups.computeIfAbsent(group, k -> new ArrayList<>()).addAll(lines);
   }

   @Override
   public void addToGroup(final Identifier group, final String lines) {
      this.val$groups.computeIfAbsent(group, k -> new ArrayList<>()).add(lines);
   }
}
