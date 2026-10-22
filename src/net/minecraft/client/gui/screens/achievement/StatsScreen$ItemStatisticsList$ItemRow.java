package net.minecraft.client.gui.screens.achievement;

import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stat;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class StatsScreen$ItemStatisticsList$ItemRow extends StatsScreen$ItemStatisticsList$Entry {
   private final Item item;
   private final StatsScreen$ItemStatisticsList$ItemRow$ItemRowWidget itemRowWidget;

   private StatsScreen$ItemStatisticsList$ItemRow(final StatsScreen$ItemStatisticsList this$1, final Item item) {
      Objects.requireNonNull(this$1x);
      this.this$1 = this$1x;
      super();
      this.item = item;
      this.itemRowWidget = new StatsScreen$ItemStatisticsList$ItemRow$ItemRowWidget(this, item.getDefaultInstance());
   }

   protected Item getItem() {
      return this.item;
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      this.itemRowWidget.setPosition(this.getContentX(), this.getContentY());
      this.itemRowWidget.extractRenderState(graphics, mouseX, mouseY, a);
      StatsScreen$ItemStatisticsList itemStatsList = this.this$1;
      int index = itemStatsList.children().indexOf(this);

      for (int col = 0; col < itemStatsList.blockColumns.size(); col++) {
         Stat<Block> stat;
         if (this.item instanceof BlockItem blockItem) {
            stat = itemStatsList.blockColumns.get(col).get(blockItem.getBlock());
         } else {
            stat = null;
         }

         this.extractStat(graphics, stat, this.getContentX() + this.this$1.getColumnX(col), this.getContentYMiddle() - 9 / 2, index % 2 == 0);
      }

      for (int col = 0; col < itemStatsList.itemColumns.size(); col++) {
         this.extractStat(
            graphics,
            itemStatsList.itemColumns.get(col).get(this.item),
            this.getContentX() + this.this$1.getColumnX(col + itemStatsList.blockColumns.size()),
            this.getContentYMiddle() - 9 / 2,
            index % 2 == 0
         );
      }
   }

   protected void extractStat(final GuiGraphicsExtractor graphics, @Nullable final Stat<?> stat, final int x, final int y, final boolean shaded) {
      Component msg = (Component)(stat == null ? StatsScreen.NO_VALUE_DISPLAY : Component.literal(stat.format(this.this$1.this$0.stats.getValue(stat))));
      graphics.text(StatsScreen.access$600(this.this$1.this$0), msg, x - StatsScreen.access$700(this.this$1.this$0).width(msg), y, shaded ? -1 : -4539718);
   }

   @Override
   public List<? extends NarratableEntry> narratables() {
      return List.of(this.itemRowWidget);
   }

   @Override
   public List<? extends GuiEventListener> children() {
      return List.of(this.itemRowWidget);
   }
}
