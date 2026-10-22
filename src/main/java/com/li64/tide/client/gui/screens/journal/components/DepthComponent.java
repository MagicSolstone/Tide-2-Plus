package com.li64.tide.client.gui.screens.journal.components;

import com.li64.tide.Tide;
import com.li64.tide.client.gui.screens.journal.ProfileComponent;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class DepthComponent extends BarComponent {
    public static final int MIN_Y = -64; // min build height
    public static final int MAX_Y = 63; // sea level

    private static final Identifier DEPTH_BAR = Tide.resource("textures/gui/journal/depth_bar.png");
    private static final Component TITLE = Component.translatable("journal.info.depth.title");

    private final float minDepth;
    private final float maxDepth;

    public DepthComponent(float minDepth, float maxDepth) {
        this.minDepth = minDepth;
        this.maxDepth = maxDepth;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, Font font, int x, int y, int mouseX, int mouseY, float partialTick) {
        int center = x + ProfileComponent.AREA_WIDTH / 2;

        // render temperature bar
        graphics.drawString(font, TITLE, center - font.width(TITLE) / 2, y, ProfileComponent.TEXT_COLOR, false);
        graphics.blit(EMPTY_BAR, x + BAR_X, y + BAR_Y, 0, 0, BAR_WIDTH, BAR_HEIGHT, BAR_WIDTH, BAR_HEIGHT);
        renderFilledBar(DEPTH_BAR, graphics, x + BAR_X, y + BAR_Y, minDepth, maxDepth);

        // render temperature mouse cursor
        if (mouseX >= x + BAR_X && mouseX <= x + BAR_X + BAR_WIDTH
                && mouseY >= y + BAR_Y && mouseY <= y + BAR_Y + BAR_HEIGHT) {
            float depthFloat = Mth.clamp((float) (mouseX - (x + BAR_X)) / BAR_WIDTH, 0f, 1f);
            int depthMeters = Math.abs(floatToDepth(depthFloat) - MAX_Y);
            graphics.renderTooltip(font, Component.literal(depthMeters + " m"), mouseX, y + BAR_Y);
            graphics.blit(CURSOR, mouseX - 1, y + BAR_Y, 0, 0, 3, 9, 3, 9);
        }
    }

    public static float depthToFloat(int depth) {
        return Mth.clamp(Mth.inverseLerp(depth, MIN_Y, MAX_Y), 0f, 1f);
    }

    public static int floatToDepth(float depth) {
        return Mth.clamp((int) Mth.lerp(depth, MIN_Y, MAX_Y), MIN_Y, MAX_Y);
    }

    @Override
    public int getRequiredHeight() {
        return 24;
    }
}
