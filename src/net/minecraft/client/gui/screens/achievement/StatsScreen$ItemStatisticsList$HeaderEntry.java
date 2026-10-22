package net.minecraft.client.gui.screens.achievement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
class StatsScreen$ItemStatisticsList$HeaderEntry extends StatsScreen$ItemStatisticsList$Entry {
   private static final Identifier BLOCK_MINED_SPRITE = Identifier.withDefaultNamespace("statistics/block_mined");
   private static final Identifier ITEM_BROKEN_SPRITE = Identifier.withDefaultNamespace("statistics/item_broken");
   private static final Identifier ITEM_CRAFTED_SPRITE = Identifier.withDefaultNamespace("statistics/item_crafted");
   private static final Identifier ITEM_USED_SPRITE = Identifier.withDefaultNamespace("statistics/item_used");
   private static final Identifier ITEM_PICKED_UP_SPRITE = Identifier.withDefaultNamespace("statistics/item_picked_up");
   private static final Identifier ITEM_DROPPED_SPRITE = Identifier.withDefaultNamespace("statistics/item_dropped");
   private final StatsScreen$ItemStatisticsList$HeaderEntry$StatSortButton blockMined;
   private final StatsScreen$ItemStatisticsList$HeaderEntry$StatSortButton itemBroken;
   private final StatsScreen$ItemStatisticsList$HeaderEntry$StatSortButton itemCrafted;
   private final StatsScreen$ItemStatisticsList$HeaderEntry$StatSortButton itemUsed;
   private final StatsScreen$ItemStatisticsList$HeaderEntry$StatSortButton itemPickedUp;
   private final StatsScreen$ItemStatisticsList$HeaderEntry$StatSortButton itemDropped;
   private final List<AbstractWidget> children;

   private StatsScreen$ItemStatisticsList$HeaderEntry(final StatsScreen$ItemStatisticsList this$1) {
      Objects.requireNonNull(this$1x);
      this.this$1 = this$1x;
      super();
      this.children = new ArrayList<>();
      this.blockMined = new StatsScreen$ItemStatisticsList$HeaderEntry$StatSortButton(this, 0, BLOCK_MINED_SPRITE);
      this.itemBroken = new StatsScreen$ItemStatisticsList$HeaderEntry$StatSortButton(this, 1, ITEM_BROKEN_SPRITE);
      this.itemCrafted = new StatsScreen$ItemStatisticsList$HeaderEntry$StatSortButton(this, 2, ITEM_CRAFTED_SPRITE);
      this.itemUsed = new StatsScreen$ItemStatisticsList$HeaderEntry$StatSortButton(this, 3, ITEM_USED_SPRITE);
      this.itemPickedUp = new StatsScreen$ItemStatisticsList$HeaderEntry$StatSortButton(this, 4, ITEM_PICKED_UP_SPRITE);
      this.itemDropped = new StatsScreen$ItemStatisticsList$HeaderEntry$StatSortButton(this, 5, ITEM_DROPPED_SPRITE);
      this.children.addAll(List.of(this.blockMined, this.itemBroken, this.itemCrafted, this.itemUsed, this.itemPickedUp, this.itemDropped));
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      this.blockMined.setPosition(this.getContentX() + this.this$1.getColumnX(0) - 18, this.getContentY() + 1);
      this.blockMined.extractRenderState(graphics, mouseX, mouseY, a);
      this.itemBroken.setPosition(this.getContentX() + this.this$1.getColumnX(1) - 18, this.getContentY() + 1);
      this.itemBroken.extractRenderState(graphics, mouseX, mouseY, a);
      this.itemCrafted.setPosition(this.getContentX() + this.this$1.getColumnX(2) - 18, this.getContentY() + 1);
      this.itemCrafted.extractRenderState(graphics, mouseX, mouseY, a);
      this.itemUsed.setPosition(this.getContentX() + this.this$1.getColumnX(3) - 18, this.getContentY() + 1);
      this.itemUsed.extractRenderState(graphics, mouseX, mouseY, a);
      this.itemPickedUp.setPosition(this.getContentX() + this.this$1.getColumnX(4) - 18, this.getContentY() + 1);
      this.itemPickedUp.extractRenderState(graphics, mouseX, mouseY, a);
      this.itemDropped.setPosition(this.getContentX() + this.this$1.getColumnX(5) - 18, this.getContentY() + 1);
      this.itemDropped.extractRenderState(graphics, mouseX, mouseY, a);
      if (this.this$1.sortColumn != null) {
         int offset = this.this$1.getColumnX(this.this$1.getColumnIndex(this.this$1.sortColumn)) - 36;
         Identifier sprite = this.this$1.sortOrder == 1 ? StatsScreen.SORT_UP_SPRITE : StatsScreen.SORT_DOWN_SPRITE;
         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, this.getContentX() + offset, this.getContentY() + 1, 18, 18);
      }
   }

   @Override
   public List<? extends GuiEventListener> children() {
      return this.children;
   }

   @Override
   public List<? extends NarratableEntry> narratables() {
      return this.children;
   }
}
