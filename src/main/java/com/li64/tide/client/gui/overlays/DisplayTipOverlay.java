package com.li64.tide.client.gui.overlays;

import com.li64.tide.client.Gui26;
import com.li64.tide.data.angler.CatchQuality;
import com.li64.tide.data.angler.FishWeight;
import com.li64.tide.data.item.TideItemData;
import com.li64.tide.registries.blocks.entities.FishDisplayBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

/** Sneaking while looking at a fish on the wall: a small tip under the crosshair with its name, quality and weight. */
public final class DisplayTipOverlay {
    private DisplayTipOverlay() {}

    public static void render(GuiGraphicsExtractor g, float dt) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null || !mc.player.isShiftKeyDown() || mc.gui.screen() != null) return;
        if (!(mc.hitResult instanceof BlockHitResult hit) || hit.getType() != HitResult.Type.BLOCK) return;
        if (!(mc.level.getBlockEntity(hit.getBlockPos()) instanceof FishDisplayBlockEntity display) || display.isEmpty()) return;
        ItemStack fish = display.getDisplayStack();

        Integer qi = TideItemData.FISH_QUALITY.getOrDefault(fish, null);
        CatchQuality q = qi == null ? null : CatchQuality.byId(qi);
        String name = Component.translatable(fish.getItem().getDescriptionId()).getString();
        String title = q == null || q == CatchQuality.AVERAGE ? name : q.label + " " + name;
        double grams = TideItemData.FISH_WEIGHT.getOrDefault(fish, 0.0);
        String weight = grams > 0 ? FishWeight.imperial(grams) : "";

        Font font = mc.font;
        int w = Math.max(font.width(title), font.width(weight)) + 10, h = weight.isEmpty() ? 14 : 24;
        int x = (g.guiWidth() - w) / 2, y = g.guiHeight() / 2 + 14;
        g.fill(x, y, x + w, y + h, 0xA0101820);
        g.fill(x, y, x + 2, y + h, 0xFF000000 | (q == null ? 0xFFFFFF : q.color & 0xFFFFFF));
        Gui26.text(g, font, title, x + 6, y + 3, 0xFF000000 | (q == null ? 0xFFFFFF : q.color & 0xFFFFFF), true);
        if (!weight.isEmpty()) Gui26.text(g, font, weight, x + 6, y + 13, 0xFFFFC83A, true);
    }
}
