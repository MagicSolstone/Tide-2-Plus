package com.li64.tide.client.gui.overlays;

import com.li64.tide.client.Gui26;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;

/**
 * A short card above the hotbar after each catch (and for small level-ups): the fish, its quality and size,
 * what was special, and the XP. Stays about ten seconds (a line in chat keeps it after), then fades; a new catch replaces it.
 */
public final class CatchBannerOverlay {
    private CatchBannerOverlay() {}

    private static final long SHOW_MS = 10000, FADE_MS = 1000;
    private static ItemStack icon = ItemStack.EMPTY;
    private static String title = "", detail = "", footer = "", weight = "";
    private static int color = 0xFFFFFF;
    private static long shownAt = 0;

    public static void show(ItemStack icon, String title, int color, String detail, String footer, String weight) {
        CatchBannerOverlay.weight = weight;
        CatchBannerOverlay.icon = icon; CatchBannerOverlay.title = title; CatchBannerOverlay.color = color;
        CatchBannerOverlay.detail = detail; CatchBannerOverlay.footer = footer;
        shownAt = System.currentTimeMillis();
    }

    public static void render(GuiGraphicsExtractor g, float dt) {
        long age = System.currentTimeMillis() - shownAt;
        if (shownAt == 0 || age > SHOW_MS) return;
        float alpha = age < SHOW_MS - FADE_MS ? 1f : (SHOW_MS - age) / (float) FADE_MS;
        int a = Math.max(8, Math.round(alpha * 255));
        Font font = Minecraft.getInstance().font;
        String[] details = detail.isEmpty() ? new String[0] : detail.split("\n");
        String head = weight.isEmpty() ? title : title + "  ";
        String[] foot = footer.isEmpty() ? new String[0] : footer.split("\n");
        int textW = font.width(head) + font.width(weight);
        for (String f : foot) textW = Math.max(textW, font.width(f));
        for (String d : details) textW = Math.max(textW, font.width(d));
        int w = textW + (icon.isEmpty() ? 12 : 32), h = 8 + 10 + details.length * 10 + foot.length * 10;
        int x = (g.guiWidth() - w) / 2, y = g.guiHeight() - 72 - h;
        g.fill(x, y, x + w, y + h, (Math.round(alpha * 0xB0) << 24) | 0x101820);
        g.fill(x, y, x + 2, y + h, (a << 24) | (color & 0xFFFFFF));
        int tx = x + 6;
        if (!icon.isEmpty()) { g.item(icon, x + 6, y + (h - 16) / 2); tx += 20; }
        int ty = y + 4;
        Gui26.text(g, font, head, tx, ty, (a << 24) | (color & 0xFFFFFF), true);
        if (!weight.isEmpty()) Gui26.text(g, font, weight, tx + font.width(head), ty, (a << 24) | 0xFFC83A, true);   // the weight, in gold: the number to brag about
        ty += 10;
        for (int i = 0; i < details.length; i++) {
            boolean effect = details[i].startsWith("*"), extra = details[i].startsWith("~");      // what a rod did (purple), items that came up (yellow), else what was special
            String line = effect || extra ? details[i].substring(1) : details[i];
            Gui26.text(g, font, line, tx, ty, (a << 24) | (effect ? 0xD08AFF : extra ? 0xFFE08A : 0xE8E8E8), true);
            ty += 10;
        }
        // the XP (where it came from in grey, after it), then a level-up in gold
        for (String f : foot) {
            if (f.startsWith("Level up")) Gui26.text(g, font, f, tx, ty, (a << 24) | 0xFFD24A, true);
            else {
                int cut = f.indexOf(" (");
                String main = cut < 0 ? f : f.substring(0, cut), why = cut < 0 ? "" : f.substring(cut);
                Gui26.text(g, font, main, tx, ty, (a << 24) | 0x7FD8FF, true);
                if (!why.isEmpty()) Gui26.text(g, font, why, tx + font.width(main), ty, (a << 24) | 0x9AA8B4, true);
            }
            ty += 10;
        }
    }
}
