package net.minecraft.client.gui.screens.achievement;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
class StatsScreen$ItemStatisticsList$HeaderEntry$StatSortButton extends ImageButton {
   private final Identifier sprite;

   private StatsScreen$ItemStatisticsList$HeaderEntry$StatSortButton(
      final StatsScreen$ItemStatisticsList$HeaderEntry this$2, final int column, final Identifier sprite
   ) {
      super(
         18,
         18,
         new WidgetSprites(StatsScreen.HEADER_SPRITE, StatsScreen.SLOT_SPRITE),
         button -> this$2.this$1.sortByColumn(this$2.this$1.getColumn(column)),
         this$2.this$1.getColumn(column).getDisplayName()
      );
      this.sprite = sprite;
      this.setTooltip(Tooltip.create(this.getMessage()));
   }

   @Override
   public void extractContents(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
      Identifier background = this.sprites.get(this.isActive(), this.isHoveredOrFocused());
      graphics.blitSprite(RenderPipelines.GUI_TEXTURED, background, this.getX(), this.getY(), this.width, this.height);
      graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, this.getX(), this.getY(), this.width, this.height);
   }
}
