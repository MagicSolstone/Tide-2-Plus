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
 * A dog-ear in a graded fish's quality colour, in the top-left corner of its icon (raw or cooked), so the quality
 * shows at a glance in inventories, the hotbar, the satchel and the journal. Top-left because fish art runs from the
 * bottom-left up to the top-right, so that corner is empty; the bottom-right is the stack count's. Average fish (most
 * of them) get none, so the mark means something; Poor ones get none either: they look dull (their own art).
 */
@Mixin(GuiGraphicsExtractor.class)
public abstract class GuiGraphicsQualityMixin {
    @Inject(method = "itemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At("TAIL"))
    private void tide$qualityMark(Font font, ItemStack stack, int x, int y, String text, CallbackInfo ci) {
        if (stack.isEmpty()) return;
        Integer q = TideItemData.FISH_QUALITY.getOrDefault(stack, null);
        if (q == null || q <= CatchQuality.AVERAGE.ordinal()) return;   // Poor fish look dull instead (their own art)
        GuiGraphicsExtractor g = (GuiGraphicsExtractor) (Object) this;
        int colour = 0xFF000000 | CatchQuality.byId(q).color;
        // a dog-ear: a little triangle filling the top-left corner, with a dark edge along its diagonal
        for (int dy = 0; dy < 4; dy++) {
            g.fill(x, y + dy, x + 4 - dy, y + dy + 1, colour);
            g.fill(x + 4 - dy, y + dy, x + 5 - dy, y + dy + 1, 0xB0000000);
        }
        g.fill(x, y + 4, x + 1, y + 5, 0xB0000000);
    }
}
