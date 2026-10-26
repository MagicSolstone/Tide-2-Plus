package com.li64.tide.client.gui;

import com.li64.tide.client.Gui26;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;

/** The tackle bench look, shared by the Angling Table's tabs: a wooden frame with brass corners, and hints in empty slots. */
public final class BenchStyle {
    private BenchStyle() {}

    private static final int EDGE = 0xFF2E1D10, WOOD = 0xFF6B4527, WOOD_LIGHT = 0xFF8A5E37, GRAIN = 0xFF5A3A20,
            BRASS = 0xFFC9A44A, BRASS_DARK = 0xFF8C6E2A;

    /** Multiplied into the bench's grey panel texture as it is drawn: the grey turns to oiled wood, the slots to darker wood. */
    public static final int WOOD_TINT = 0xFFFFCE89;

    /** The panel texture, drawn in wood (draw it after the screen's own background, which dims everything under it). */
    public static void panel(GuiGraphicsExtractor g, net.minecraft.resources.Identifier texture, int x, int y, int w, int h) {
        g.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, texture, x, y, 0, 0, w, h, 256, 256, WOOD_TINT);
    }

    /** The panel's colours once tinted (the grey texture times WOOD_TINT), for drawing more panel by hand. */
    public static final int PANEL = 0xFFC6A06A, SLOT = 0xFF8B704A, SLOT_SHADOW = 0xFF372C1D, SLOT_LIGHT = 0xFFFFCE89;

    /**
     * More of the panel below its texture (x, w: the panel; from y0 down to y1, its bottom edge): the same edges as the
     * texture, a dark outline, a light left side and a shaded right side and bottom, so the panel reads as one piece.
     */
    public static void panelBelow(GuiGraphicsExtractor g, int x, int y0, int w, int y1) {
        int x1 = x + w;
        g.fill(x, y0, x1, y1 - 1, PANEL);
        g.fill(x, y0, x + 1, y1 - 2, EDGE_DARK);                 // outline: left, right, bottom (corners cut like the texture's)
        g.fill(x1 - 1, y0, x1, y1 - 2, EDGE_DARK);
        g.fill(x + 2, y1 - 1, x1 - 2, y1, EDGE_DARK);
        g.fill(x + 1, y1 - 2, x + 2, y1 - 1, EDGE_DARK);
        g.fill(x1 - 2, y1 - 2, x1 - 1, y1 - 1, EDGE_DARK);
        g.fill(x + 1, y0, x + 3, y1 - 3, SLOT_LIGHT);            // the light left side
        g.fill(x1 - 3, y0, x1 - 1, y1 - 2, EDGE_SHADE);          // the shaded right side and bottom
        g.fill(x + 3, y1 - 3, x1 - 1, y1 - 1, EDGE_SHADE);
        g.fill(x + 2, y1 - 2, x + 3, y1 - 1, EDGE_SHADE);
    }

    private static final int EDGE_DARK = 0xFF000000, EDGE_SHADE = 0xFF55452D;      // black and 555555 x WOOD_TINT

    /** A slot drawn like the texture's: shadow top-left, light bottom-right (sx, sy: the item's top-left). */
    public static void slot(GuiGraphicsExtractor g, int sx, int sy) {
        g.fill(sx - 1, sy - 1, sx + 17, sy + 17, SLOT_LIGHT);
        g.fill(sx - 1, sy - 1, sx + 16, sy + 16, SLOT_SHADOW);
        g.fill(sx, sy, sx + 16, sy + 16, SLOT);
    }

    /** A wooden frame just outside the screen's own panel (x, y, w, h: the panel). */
    public static void frame(GuiGraphicsExtractor g, int x, int y, int w, int h) {
        int x0 = x - 5, y0 = y - 5, x1 = x + w + 5, y1 = y + h + 5;
        g.fill(x0, y0, x1, y1, EDGE);
        g.fill(x0 + 1, y0 + 1, x1 - 1, y1 - 1, WOOD);
        g.fill(x0 + 1, y0 + 1, x1 - 1, y0 + 2, WOOD_LIGHT);
        g.fill(x0 + 1, y0 + 1, x0 + 2, y1 - 1, WOOD_LIGHT);
        // a little grain along the long sides
        for (int i = x0 + 6; i < x1 - 6; i += 11) {
            g.fill(i, y0 + 2, i + 5, y0 + 3, GRAIN);
            g.fill(i + 3, y1 - 3, i + 8, y1 - 2, GRAIN);
        }
        for (int j = y0 + 8; j < y1 - 8; j += 13) {
            g.fill(x0 + 2, j, x0 + 3, j + 5, GRAIN);
            g.fill(x1 - 3, j + 4, x1 - 2, j + 9, GRAIN);
        }
        // brass corner plates
        for (int[] c : new int[][]{{x0, y0}, {x1 - 7, y0}, {x0, y1 - 7}, {x1 - 7, y1 - 7}}) {
            g.fill(c[0], c[1], c[0] + 7, c[1] + 7, BRASS_DARK);
            g.fill(c[0] + 1, c[1] + 1, c[0] + 6, c[1] + 6, BRASS);
            g.fill(c[0] + 3, c[1] + 3, c[0] + 4, c[1] + 4, BRASS_DARK);
        }
    }

    /** In an empty slot: a faded picture of what goes there. Under it, a small label. (sx, sy: the slot's top-left on screen.) */
    public static void hint(GuiGraphicsExtractor g, Font font, int sx, int sy, ItemStack ghost, String label, boolean empty) {
        if (empty && !ghost.isEmpty()) {
            g.item(ghost, sx, sy);
            g.fill(sx, sy, sx + 16, sy + 16, 0xB08B704A);      // the slot's own colour once tinted to wood (8B8B8B x WOOD_TINT)
        }
        if (label.isEmpty()) return;
        g.pose().pushMatrix();
        g.pose().translate(sx + 8 - font.width(label) * 0.25f, sy + 17.5f);
        g.pose().scale(0.5f, 0.5f);
        Gui26.text(g, font, label, 0, 0, 0xFF4A3A2A, false);
        g.pose().popMatrix();
    }
}
