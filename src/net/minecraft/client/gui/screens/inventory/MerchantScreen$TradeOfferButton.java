package net.minecraft.client.gui.screens.inventory;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button$OnPress;
import net.minecraft.client.gui.components.Button$Plain;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
class MerchantScreen$TradeOfferButton extends Button$Plain {
   private final int index;

   public MerchantScreen$TradeOfferButton(final MerchantScreen this$0, final int x, final int y, final int index, final Button$OnPress onPress) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(x, y, 88, 20, CommonComponents.EMPTY, onPress, DEFAULT_NARRATION);
      this.index = index;
      this.visible = false;
   }

   public int getIndex() {
      return this.index;
   }

   public void extractToolTip(final GuiGraphicsExtractor graphics, final int xm, final int ym) {
      if (this.isHovered && this.this$0.menu.getOffers().size() > this.index + this.this$0.scrollOff) {
         if (xm < this.getX() + 20) {
            ItemStack item = this.this$0.menu.getOffers().get(this.index + this.this$0.scrollOff).getCostA();
            graphics.setTooltipForNextFrame(MerchantScreen.access$000(this.this$0), item, xm, ym);
         } else if (xm < this.getX() + 50 && xm > this.getX() + 30) {
            ItemStack item = this.this$0.menu.getOffers().get(this.index + this.this$0.scrollOff).getCostB();
            if (!item.isEmpty()) {
               graphics.setTooltipForNextFrame(MerchantScreen.access$100(this.this$0), item, xm, ym);
            }
         } else if (xm > this.getX() + 65) {
            ItemStack item = this.this$0.menu.getOffers().get(this.index + this.this$0.scrollOff).getResult();
            graphics.setTooltipForNextFrame(MerchantScreen.access$200(this.this$0), item, xm, ym);
         }
      }
   }
}
