package com.li64.tide.client.gui.screens.journal.components;

import com.li64.tide.Tide;
import com.li64.tide.client.gui.screens.journal.ProfileComponent;
import com.li64.tide.data.fishing.conditions.types.WeatherType;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WeatherComponent extends ProfileComponent {
    private static final Identifier ICONS = Tide.resource("textures/gui/journal/weather.png");
    private static final Component TITLE = Component.translatable("journal.info.weather.title");

    public List<WeatherType> weatherTypes;

    public WeatherComponent(List<WeatherType> weatherTypes) {
        this.weatherTypes = weatherTypes;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, Font font, int x, int y, int mouseX, int mouseY, float partialTick) {
        int center = x + AREA_WIDTH / 2;
        graphics.drawString(font, TITLE, center - font.width(TITLE) / 2, y, TEXT_COLOR, false);

        int count = weatherTypes.size();
        int spriteY = y + 12;
        int padding = 4;

        for (int i = 0; i < count; i++) {
            int cellSize = 10 + padding;
            int spriteX = center - ((count - 1) * cellSize / 2) + (i * cellSize) - 4;

            int offset = getOffset(weatherTypes.get(i)) * 10;
            graphics.blit(ICONS, spriteX, spriteY, offset, 0, 10, 10, 30, 10);

            if (mouseX >= spriteX && mouseX <= spriteX + 10 && mouseY >= spriteY && mouseY <= spriteY + 10)
                graphics.renderTooltip(font, Component.translatable("journal.info.weather." + weatherTypes.get(i).getSerializedName()), mouseX, mouseY);
        }
    }

    public static int getOffset(WeatherType weatherType) {
        return switch (weatherType) {
            case CLEAR -> 0;
            case RAIN -> 1;
            case STORM -> 2;
        };
    }

    @Override
    public int getRequiredHeight() {
        return 26;
    }
}
