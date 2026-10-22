package net.minecraft.client.gui.screens.achievement;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EntityType;

@Environment(EnvType.CLIENT)
class StatsScreen$MobsStatisticsList extends ObjectSelectionList<StatsScreen$MobsStatisticsList$MobRow> {
   public StatsScreen$MobsStatisticsList(final StatsScreen this$0, final Minecraft minecraft) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(minecraft, this$0x.width, this$0x.layout.getContentHeight(), 33, 9 * 4);

      for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
         if (this$0x.stats.getValue(Stats.ENTITY_KILLED.get(type)) > 0 || this$0x.stats.getValue(Stats.ENTITY_KILLED_BY.get(type)) > 0) {
            this.addEntry(new StatsScreen$MobsStatisticsList$MobRow(this, type));
         }
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
