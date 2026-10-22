package net.minecraft.client.gui.screens.achievement;

import java.util.Comparator;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.stats.StatType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

@Environment(EnvType.CLIENT)
class StatsScreen$ItemStatisticsList$ItemRowComparator implements Comparator<StatsScreen$ItemStatisticsList$ItemRow> {
   private StatsScreen$ItemStatisticsList$ItemRowComparator(final StatsScreen$ItemStatisticsList this$1) {
      Objects.requireNonNull(this$1x);
      this.this$1 = this$1x;
      super();
   }

   public int compare(final StatsScreen$ItemStatisticsList$ItemRow one, final StatsScreen$ItemStatisticsList$ItemRow two) {
      Item item1 = one.getItem();
      Item item2 = two.getItem();
      int key1;
      int key2;
      if (this.this$1.sortColumn == null) {
         key1 = 0;
         key2 = 0;
      } else if (this.this$1.blockColumns.contains(this.this$1.sortColumn)) {
         StatType<Block> type = (StatType<Block>)this.this$1.sortColumn;
         key1 = item1 instanceof BlockItem blockItem ? this.this$1.this$0.stats.getValue(type, blockItem.getBlock()) : -1;
         key2 = item2 instanceof BlockItem blockItemx ? this.this$1.this$0.stats.getValue(type, blockItemx.getBlock()) : -1;
      } else {
         StatType<Item> type = (StatType<Item>)this.this$1.sortColumn;
         key1 = this.this$1.this$0.stats.getValue(type, item1);
         key2 = this.this$1.this$0.stats.getValue(type, item2);
      }

      return key1 == key2 ? this.this$1.sortOrder * Integer.compare(Item.getId(item1), Item.getId(item2)) : this.this$1.sortOrder * Integer.compare(key1, key2);
   }
}
