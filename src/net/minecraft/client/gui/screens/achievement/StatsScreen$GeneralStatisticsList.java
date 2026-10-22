package net.minecraft.client.gui.screens.achievement;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.Comparator;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;

@Environment(EnvType.CLIENT)
class StatsScreen$GeneralStatisticsList extends ObjectSelectionList<StatsScreen$GeneralStatisticsList$Entry> {
   public StatsScreen$GeneralStatisticsList(final StatsScreen this$0, final Minecraft minecraft) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(minecraft, this$0x.width, this$0x.layout.getContentHeight(), 33, 14);
      ObjectArrayList<Stat<Identifier>> stats = new ObjectArrayList(Stats.CUSTOM.iterator());
      stats.sort(Comparator.comparing(k -> I18n.get(StatsScreen.getTranslationKey((Stat<Identifier>)k))));
      ObjectListIterator var4 = stats.iterator();

      while (var4.hasNext()) {
         Stat<Identifier> stat = (Stat<Identifier>)var4.next();
         this.addEntry(new StatsScreen$GeneralStatisticsList$Entry(this, stat));
      }
   }

   @Override
   public int getRowWidth() {
      return 280;
   }

   @Override
   protected void extractListBackground(final GuiGraphicsExtractor graphics) {
   }

   @Override
   protected void extractListSeparators(final GuiGraphicsExtractor graphics) {
   }
}
