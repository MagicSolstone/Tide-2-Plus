package com.li64.tide.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

/** GUI helpers for 26.x. Text colours there are ARGB: a colour without alpha (0x404040) draws nothing,
 * so these treat a zero alpha as fully opaque, the way 1.21.1 did. */
public final class Gui26 {
    private Gui26() {}

    public static int opaque(int color) {
        return (color & 0xFF000000) == 0 ? color | 0xFF000000 : color;
    }

    public static void text(GuiGraphicsExtractor g, Font font, Component text, int x, int y, int color, boolean shadow) {
        g.text(font, text, x, y, opaque(color), shadow);
    }

    public static void text(GuiGraphicsExtractor g, Font font, Component text, int x, int y, int color) {
        g.text(font, text, x, y, opaque(color), true);
    }

    public static void text(GuiGraphicsExtractor g, Font font, String text, int x, int y, int color, boolean shadow) {
        g.text(font, text, x, y, opaque(color), shadow);
    }

    public static void text(GuiGraphicsExtractor g, Font font, String text, int x, int y, int color) {
        g.text(font, text, x, y, opaque(color), true);
    }

    public static void text(GuiGraphicsExtractor g, Font font, FormattedCharSequence text, int x, int y, int color, boolean shadow) {
        g.text(font, text, x, y, opaque(color), shadow);
    }

    public static void text(GuiGraphicsExtractor g, Font font, FormattedCharSequence text, int x, int y, int color) {
        g.text(font, text, x, y, opaque(color), true);
    }
}
