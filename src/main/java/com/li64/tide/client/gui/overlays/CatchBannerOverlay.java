package com.li64.tide.client.gui.overlays;

import com.li64.tide.client.Gui26;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;

/**
 * A short card above the hotbar after each catch (and for small level-ups): the fish, its quality and size,
 * what was special, and the XP. Stays about five seconds, then fades; a new catch replaces it.
 */
public final class CatchBannerOverlay {
    private CatchBannerOverlay() {}

    private static final long SHOW_MS = 5000, FADE_MS = 800;
    private static ItemStack icon = ItemStack.EMPTY;
    private static String title = "", detail = "", footer = "";
    private static int color = 0xFFFFFF;
    private static long shownAt = 0;

    public static void show(ItemStack icon, String title, int color, String detail, String footer) {
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
        int textW = Math.max(font.width(title), Math.max(font.width(detail), font.width(footer)));
        int w = textW + (icon.isEmpty() ? 12 : 32), h = 8 + 10 + (detail.isEmpty() ? 0 : 10) + (footer.isEmpty() ? 0 : 10);
        int x = (g.guiWidth() - w) / 2, y = g.guiHeight() - 72 - h;
        g.fill(x, y, x + w, y + h, (Math.round(alpha * 0xB0) << 24) | 0x101820);
        g.fill(x, y, x + 2, y + h, (a << 24) | (color & 0xFFFFFF));
        int tx = x + 6;
        if (!icon.isEmpty()) { g.item(icon, x + 6, y + (h - 16) / 2); tx += 20; }
        int ty = y + 4;
        Gui26.text(g, font, title, tx, ty, (a << 24) | (color & 0xFFFFFF), true);
        ty += 10;
        if (!detail.isEmpty()) { Gui26.text(g, font, detail, tx, ty, (a << 24) | 0xE8E8E8, true); ty += 10; }
        if (!footer.isEmpty()) Gui26.text(g, font, footer, tx, ty, (a << 24) | 0x7FD8FF, true);
    }
}
