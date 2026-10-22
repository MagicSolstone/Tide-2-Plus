package com.li64.tide.client.gui.screens.journal.components;

import com.li64.tide.Tide;
import com.li64.tide.client.gui.screens.journal.ProfileComponent;
import com.li64.tide.data.fishing.conditions.types.TimeRange;
import com.li64.tide.util.TideUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TimeComponent extends BarComponent {
    private static final Identifier TIME_BAR = Tide.resource("textures/gui/journal/time_bar.png");
    private static final Component TITLE = Component.translatable("journal.info.time.title");

    private final List<TimeRange> ranges;

    public TimeComponent(List<TimeRange> ranges) {
        this.ranges = ranges;
    }

    @Override
    public void render(@NotNull GuiGraphicsExtractor graphics, Font font, int x, int y, int mouseX, int mouseY, float partialTick) {
        int center = x + ProfileComponent.AREA_WIDTH / 2;

        // render empty time bar
        graphics.drawString(font, TITLE, center - font.width(TITLE) / 2, y, ProfileComponent.TEXT_COLOR, false);
        graphics.blit(EMPTY_BAR, x + BAR_X, y + BAR_Y, 0, 0, BAR_WIDTH, BAR_HEIGHT, BAR_WIDTH, BAR_HEIGHT);
        for (TimeRange range : ranges) {
            boolean flip = range.max() < range.min();
            renderFilledBar(TIME_BAR, graphics, x + BAR_X, y + BAR_Y,
                    ((float)(flip ? range.max() : range.min()) / 24000),
                    ((float)(flip ? range.min() : range.max()) / 24000));
        }

        // render time mouse cursor
        if (mouseX >= x + BAR_X && mouseX <= x + BAR_X + BAR_WIDTH
                && mouseY >= y + BAR_Y && mouseY <= y + BAR_Y + BAR_HEIGHT) {
            long time = (long) (Mth.clamp((float) (mouseX - (x + BAR_X)) / BAR_WIDTH, 0f, 1f) * 24000L);
            graphics.renderTooltip(font, Component.literal(TideUtils.ticksToRealTime(time, Tide.CLIENT_CONFIG.journal.useAmPm)), mouseX, y + BAR_Y);
            graphics.blit(CURSOR, mouseX - 1, y + BAR_Y, 0, 0, 3, 9, 3, 9);
        }
    }

    @Override
    public int getRequiredHeight() {
        return 24;
    }
}
