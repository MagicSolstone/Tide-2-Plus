package net.minecraft.client.gui.screens.debug;

import it.unimi.dsi.fastutil.floats.FloatComparators;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.debug.DebugEntryCategory;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class DebugOptionsScreen$OptionList extends ContainerObjectSelectionList<DebugOptionsScreen$AbstractOptionEntry> {
   private static final Comparator<Map.Entry<Identifier, DebugScreenEntry>> COMPARATOR = (o1, o2) -> {
      int byCategory = FloatComparators.NATURAL_COMPARATOR.compare(o1.getValue().category().sortKey(), o2.getValue().category().sortKey());
      return byCategory != 0 ? byCategory : o1.getKey().compareTo(o2.getKey());
   };
   private static final int ITEM_HEIGHT = 20;

   public DebugOptionsScreen$OptionList(final DebugOptionsScreen this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(Minecraft.getInstance(), this$0.width, this$0.layout.getContentHeight(), this$0.layout.getHeaderHeight(), 20);
      this.updateSearch("");
   }

   @Override
   public void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
      super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
   }

   @Override
   public int getRowWidth() {
      return 350;
   }

   public void refreshEntries() {
      this.children().forEach(DebugOptionsScreen$AbstractOptionEntry::refreshEntry);
   }

   public void updateSearch(final String value) {
      this.clearEntries();
      List<Map.Entry<Identifier, DebugScreenEntry>> all = new ArrayList<>(DebugScreenEntries.allEntries().entrySet());
      all.sort(COMPARATOR);
      DebugEntryCategory currentCategory = null;

      for (Map.Entry<Identifier, DebugScreenEntry> entry : all) {
         if (entry.getKey().getPath().contains(value)) {
            DebugEntryCategory newCategory = entry.getValue().category();
            if (!newCategory.equals(currentCategory)) {
               this.addEntry(new DebugOptionsScreen$CategoryEntry(this.this$0, newCategory.label()));
               currentCategory = newCategory;
            }

            this.addEntry(new DebugOptionsScreen$OptionEntry(this.this$0, entry.getKey()));
         }
      }

      this.notifyListUpdated();
   }

   private void notifyListUpdated() {
      this.refreshScrollAmount();
      this.this$0.triggerImmediateNarration(true);
   }
}
