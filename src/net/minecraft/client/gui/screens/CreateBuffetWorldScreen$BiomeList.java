package net.minecraft.client.gui.screens;

import com.ibm.icu.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.ObjectSelectionList;

@Environment(EnvType.CLIENT)
class CreateBuffetWorldScreen$BiomeList extends ObjectSelectionList<CreateBuffetWorldScreen$BiomeList$Entry> {
   private CreateBuffetWorldScreen$BiomeList(final CreateBuffetWorldScreen this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x.minecraft, this$0x.width, this$0x.layout.getContentHeight(), this$0x.layout.getHeaderHeight(), 15);
      this.filterEntries("");
   }

   private void filterEntries(final String filter) {
      Collator localeCollator = Collator.getInstance(Locale.getDefault());
      String lowercaseFilter = filter.toLowerCase(Locale.ROOT);
      List<CreateBuffetWorldScreen$BiomeList$Entry> list = this.this$0
         .biomes
         .listElements()
         .map(x$0 -> new CreateBuffetWorldScreen$BiomeList$Entry(this, x$0))
         .sorted(Comparator.comparing(e -> e.name.getString(), localeCollator))
         .filter(entry -> filter.isEmpty() || entry.name.getString().toLowerCase(Locale.ROOT).contains(lowercaseFilter))
         .toList();
      this.replaceEntries(list);
      this.refreshScrollAmount();
   }

   public void setSelected(final CreateBuffetWorldScreen$BiomeList$Entry selected) {
      super.setSelected(selected);
      if (selected != null) {
         this.this$0.biome = selected.biome;
      }

      this.this$0.updateButtonValidity();
   }
}
