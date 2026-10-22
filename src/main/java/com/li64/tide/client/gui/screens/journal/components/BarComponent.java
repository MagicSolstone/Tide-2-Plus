package com.li64.tide.client.gui.screens.journal.components;

import com.li64.tide.Tide;
import com.li64.tide.client.gui.screens.journal.ProfileComponent;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.Identifier;

public abstract class BarComponent extends ProfileComponent {
    protected static final Identifier EMPTY_BAR = Tide.resource("textures/gui/journal/empty_bar.png");
    protected static final Identifier DIVIDER = Tide.resource("textures/gui/journal/bar_divider.png");
    protected static final Identifier CURSOR = Tide.resource("textures/gui/journal/bar_cursor.png");
    protected static final int BAR_WIDTH = 129;
    protected static final int BAR_HEIGHT = 9;
    protected static final int BAR_X = 22;
    protected static final int BAR_Y = 11;
    protected static final int DIV_WIDTH = 3;
    protected static final int DIV_HEIGHT = 11;

    protected void renderFilledBar(Identifier bar, GuiGraphicsExtractor graphics,
                                   int x, int y, float min, float max) {
        int minX = Math.max((int)(min * BAR_WIDTH), 0);
        int maxX = Math.min((int)(max * BAR_WIDTH), BAR_WIDTH);
        int width = maxX - minX;
        graphics.blit(bar, x + minX, y, minX, 0, width, BAR_HEIGHT, BAR_WIDTH, BAR_HEIGHT);

        // render dividers on time bar
        graphics.blit(DIVIDER, x + minX - 1, y - 1, 0, 0, DIV_WIDTH, DIV_HEIGHT, DIV_WIDTH, DIV_HEIGHT);
        graphics.blit(DIVIDER, x + maxX - 1, y - 1, 0, 0, DIV_WIDTH, DIV_HEIGHT, DIV_WIDTH, DIV_HEIGHT);
    }
}
