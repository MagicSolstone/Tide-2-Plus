package net.minecraft.client.gui.screens.achievement;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.stats.StatType;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class StatsScreen$ItemStatisticsList extends ContainerObjectSelectionList<StatsScreen$ItemStatisticsList$Entry> {
   private static final int SLOT_BG_SIZE = 18;
   private static final int SLOT_STAT_HEIGHT = 22;
   private static final int SLOT_BG_Y = 1;
   private static final int SORT_NONE = 0;
   private static final int SORT_DOWN = -1;
   private static final int SORT_UP = 1;
   protected final List<StatType<Block>> blockColumns;
   protected final List<StatType<Item>> itemColumns;
   protected final Comparator<StatsScreen$ItemStatisticsList$ItemRow> itemStatSorter;
   @Nullable
   protected StatType<?> sortColumn;
   protected int sortOrder;

   public StatsScreen$ItemStatisticsList(final StatsScreen this$0, final Minecraft minecraft) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(minecraft, this$0x.width, this$0x.layout.getContentHeight(), 33, 22);
      this.itemStatSorter = new StatsScreen$ItemStatisticsList$ItemRowComparator(this);
      this.blockColumns = Lists.newArrayList();
      this.blockColumns.add(Stats.BLOCK_MINED);
      this.itemColumns = Lists.newArrayList(new StatType[]{Stats.ITEM_BROKEN, Stats.ITEM_CRAFTED, Stats.ITEM_USED, Stats.ITEM_PICKED_UP, Stats.ITEM_DROPPED});
      Set<Item> items = Sets.newIdentityHashSet();

      for (Item item : BuiltInRegistries.ITEM) {
         boolean addToList = false;

         for (StatType<Item> type : this.itemColumns) {
            if (type.contains(item) && this$0x.stats.getValue(type.get(item)) > 0) {
               addToList = true;
            }
         }

         if (addToList) {
            items.add(item);
         }
      }

      for (Block block : BuiltInRegistries.BLOCK) {
         boolean addToList = false;

         for (StatType<Block> typex : this.blockColumns) {
            if (typex.contains(block) && this$0x.stats.getValue(typex.get(block)) > 0) {
               addToList = true;
            }
         }

         if (addToList) {
            items.add(block.asItem());
         }
      }

      items.remove(Items.AIR);
      if (!items.isEmpty()) {
         this.addEntry(new StatsScreen$ItemStatisticsList$HeaderEntry(this));

         for (Item item : items) {
            this.addEntry(new StatsScreen$ItemStatisticsList$ItemRow(this, item));
         }
      }
   }

   @Override
   protected void extractListBackground(final GuiGraphicsExtractor graphics) {
   }

   private int getColumnX(final int col) {
      return 75 + 40 * col;
   }

   @Override
   public int getRowWidth() {
      return 280;
   }

   private StatType<?> getColumn(final int i) {
      return i < this.blockColumns.size() ? this.blockColumns.get(i) : this.itemColumns.get(i - this.blockColumns.size());
   }

   private int getColumnIndex(final StatType<?> column) {
      int i = this.blockColumns.indexOf(column);
      if (i >= 0) {
         return i;
      } else {
         int j = this.itemColumns.indexOf(column);
         return j >= 0 ? j + this.blockColumns.size() : -1;
      }
   }

   protected void sortByColumn(final StatType<?> column) {
      if (column != this.sortColumn) {
         this.sortColumn = column;
         this.sortOrder = -1;
      } else if (this.sortOrder == -1) {
         this.sortOrder = 1;
      } else {
         this.sortColumn = null;
         this.sortOrder = 0;
      }

      this.sortItems(this.itemStatSorter);
   }

   protected void sortItems(final Comparator<StatsScreen$ItemStatisticsList$ItemRow> comparator) {
      List<StatsScreen$ItemStatisticsList$ItemRow> itemRows = this.getItemRows();
      itemRows.sort(comparator);
      this.clearEntriesExcept(this.children().getFirst());

      for (StatsScreen$ItemStatisticsList$ItemRow newChild : itemRows) {
         this.addEntry(newChild);
      }
   }

   private List<StatsScreen$ItemStatisticsList$ItemRow> getItemRows() {
      List<StatsScreen$ItemStatisticsList$ItemRow> itemRows = new ArrayList<>();
      this.children().forEach(entry -> {
         if (entry instanceof StatsScreen$ItemStatisticsList$ItemRow itemRow) {
            itemRows.add(itemRow);
         }
      });
      return itemRows;
   }

   @Override
   protected void extractListSeparators(final GuiGraphicsExtractor graphics) {
   }
}
