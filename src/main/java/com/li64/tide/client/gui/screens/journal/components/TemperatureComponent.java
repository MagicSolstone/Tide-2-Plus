package com.li64.tide.client.gui.screens.journal.components;

import com.li64.tide.Tide;
import com.li64.tide.client.gui.screens.journal.ProfileComponent;
import com.li64.tide.util.TideUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class TemperatureComponent extends BarComponent {
    private static final Identifier TEMP_BAR = Tide.resource("textures/gui/journal/temp_bar.png");
    private static final Component TITLE = Component.translatable("journal.info.climate.title");

    private final float minTemp;
    private final float maxTemp;

    public TemperatureComponent(float minTemp, float maxTemp) {
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, Font font, int x, int y, int mouseX, int mouseY, float partialTick) {
        int center = x + ProfileComponent.AREA_WIDTH / 2;

        // render temperature bar
        graphics.drawString(font, TITLE, center - font.width(TITLE) / 2, y, ProfileComponent.TEXT_COLOR, false);
        graphics.blit(EMPTY_BAR, x + BAR_X, y + BAR_Y, 0, 0, BAR_WIDTH, BAR_HEIGHT, BAR_WIDTH, BAR_HEIGHT);
        renderFilledBar(TEMP_BAR, graphics, x + BAR_X, y + BAR_Y, minTemp / 2f + 0.5f, maxTemp / 2f + 0.5f);

        // render temperature mouse cursor
        if (mouseX >= x + BAR_X && mouseX <= x + BAR_X + BAR_WIDTH
                && mouseY >= y + BAR_Y && mouseY <= y + BAR_Y + BAR_HEIGHT) {
            float temp = Mth.clamp((float) (mouseX - (x + BAR_X)) / BAR_WIDTH, 0f, 1f) * 2f - 1f;
            graphics.renderTooltip(font, Component.literal(
                    Math.round(TideUtils.mcTempToRealTemp(temp))
                            + (Tide.CLIENT_CONFIG.journal.useFahrenheit ? "°F" : "°C")),
                    mouseX, y + BAR_Y
            );
            graphics.blit(CURSOR, mouseX - 1, y + BAR_Y, 0, 0, 3, 9, 3, 9);
        }
    }

    @Override
    public int getRequiredHeight() {
        return 24;
    }
}
