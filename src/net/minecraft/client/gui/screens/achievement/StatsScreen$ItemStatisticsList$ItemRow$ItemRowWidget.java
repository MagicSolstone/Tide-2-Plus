package net.minecraft.client.gui.screens.achievement;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ItemDisplayWidget;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
class StatsScreen$ItemStatisticsList$ItemRow$ItemRowWidget extends ItemDisplayWidget {
   private StatsScreen$ItemStatisticsList$ItemRow$ItemRowWidget(final StatsScreen$ItemStatisticsList$ItemRow this$2, final ItemStack itemStack) {
      Objects.requireNonNull(this$2x);
      this.this$2 = this$2x;
      super(StatsScreen$ItemStatisticsList.access$800(this$2x.this$1), 1, 1, 18, 18, itemStack.getHoverName(), itemStack, false, true);
   }

   @Override
   protected void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
      graphics.blitSprite(RenderPipelines.GUI_TEXTURED, StatsScreen.SLOT_SPRITE, this.this$2.getContentX(), this.this$2.getContentY(), 18, 18);
      super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
   }

   @Override
   protected void extractTooltip(final GuiGraphicsExtractor graphics, final int x, final int y) {
      super.extractTooltip(graphics, this.this$2.getContentX() + 18, this.this$2.getContentY() + 18);
   }
}
