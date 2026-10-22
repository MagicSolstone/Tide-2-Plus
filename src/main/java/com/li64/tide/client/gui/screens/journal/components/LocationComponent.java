package com.li64.tide.client.gui.screens.journal.components;

import com.li64.tide.client.Gui26;
import com.li64.tide.client.gui.screens.journal.ProfileComponent;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class LocationComponent extends ProfileComponent {
    private final Component text;

    public LocationComponent(Component text) {
        this.text = Component.translatable("journal.info.location.title").append(": ").append(text);
    }

    @Override
    public void render(@NotNull GuiGraphicsExtractor graphics, Font font, int x, int y, int mouseX, int mouseY, float partialTick) {
        int center = x + AREA_WIDTH / 2;
        Gui26.text(graphics, font, text, center - font.width(text) / 2, y, TEXT_COLOR, false);
    }

    @Override
    public int getRequiredHeight() {
        return 9;
    }
}
