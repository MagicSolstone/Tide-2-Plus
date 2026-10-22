package net.minecraft.client.gui.screens.options;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.SortedMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.resources.language.LanguageInfo;

@Environment(EnvType.CLIENT)
class LanguageSelectScreen$LanguageSelectionList extends ObjectSelectionList<LanguageSelectScreen$LanguageSelectionList$Entry> {
   public LanguageSelectScreen$LanguageSelectionList(final LanguageSelectScreen this$0, final Minecraft minecraft) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(minecraft, this$0x.width, this$0x.height - 33 - 53, 33, 18);
      String selectedLanguage = this$0x.languageManager.getSelected();
      this$0x.languageManager.getLanguages().forEach((code, info) -> {
         LanguageSelectScreen$LanguageSelectionList$Entry entry = new LanguageSelectScreen$LanguageSelectionList$Entry(this, code, info);
         this.addEntry(entry);
         if (selectedLanguage.equals(code)) {
            this.setSelected(entry);
         }
      });
      if (this.getSelected() != null) {
         this.centerScrollOn(this.getSelected());
      }
   }

   private void filterEntries(final String filter) {
      SortedMap<String, LanguageInfo> languages = this.this$0.languageManager.getLanguages();
      List<LanguageSelectScreen$LanguageSelectionList$Entry> filteredEntries = languages.entrySet()
         .stream()
         .filter(
            entry -> filter.isEmpty()
               || entry.getValue().name().toLowerCase(Locale.ROOT).contains(filter.toLowerCase(Locale.ROOT))
               || entry.getValue().region().toLowerCase(Locale.ROOT).contains(filter.toLowerCase(Locale.ROOT))
         )
         .map(entry -> new LanguageSelectScreen$LanguageSelectionList$Entry(this, entry.getKey(), entry.getValue()))
         .toList();
      this.replaceEntries(filteredEntries);
      this.refreshScrollAmount();
   }

   @Override
   public int getRowWidth() {
      return super.getRowWidth() + 50;
   }
}
