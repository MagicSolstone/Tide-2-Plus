package com.li64.tide.mixin;

import com.li64.tide.data.angler.CatchQuality;
import com.li64.tide.data.item.TideItemData;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * A small diamond in a graded fish's quality colour, in the top-right corner of its icon (raw or cooked), so the
 * quality shows at a glance in inventories, the hotbar, the satchel and the journal. The bottom-right is the stack
 * count's. Average fish (most of them) get none, so the mark means something.
 */
@Mixin(GuiGraphicsExtractor.class)
public abstract class GuiGraphicsQualityMixin {
    @Inject(method = "itemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At("TAIL"))
    private void tide$qualityMark(Font font, ItemStack stack, int x, int y, String text, CallbackInfo ci) {
        if (stack.isEmpty()) return;
        Integer q = TideItemData.FISH_QUALITY.getOrDefault(stack, null);
        if (q == null || q == CatchQuality.AVERAGE.ordinal()) return;
        GuiGraphicsExtractor g = (GuiGraphicsExtractor) (Object) this;
        int colour = 0xFF000000 | CatchQuality.byId(q).color;
        int cx = x + 13, cy = y + 3;                      // the diamond's centre: 5 wide, 5 tall, with a dark outline
        for (int dy = -3; dy <= 3; dy++) {
            int w = 3 - Math.abs(dy);
            g.fill(cx - w, cy + dy, cx + w + 1, cy + dy + 1, 0xC0000000);
        }
        for (int dy = -2; dy <= 2; dy++) {
            int w = 2 - Math.abs(dy);
            g.fill(cx - w, cy + dy, cx + w + 1, cy + dy + 1, colour);
        }
    }
}
