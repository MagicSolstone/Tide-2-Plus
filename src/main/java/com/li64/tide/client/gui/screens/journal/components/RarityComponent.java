package com.li64.tide.client.gui.screens.journal.components;

import com.li64.tide.Tide;
import com.li64.tide.client.gui.screens.journal.ProfileComponent;
import com.li64.tide.data.journal.FishRarity;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class RarityComponent extends ProfileComponent {
    private static final Identifier STAR = Tide.resource("textures/gui/journal/star_note.png");

    public int numStars;

    public RarityComponent(FishRarity rarity) {
        this.numStars = rarity.getNumStars();
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, Font font, int x, int y, int mouseX, int mouseY, float partialTick) {
        int center = x + AREA_WIDTH / 2;
        int padding = 2;
        for (int i = 0; i < numStars; i++) {
            int cellSize = 7 + padding;
            graphics.blit(STAR, center - ((numStars - 1) * cellSize / 2) + (i * cellSize) - 4, y,
                    0, 0, 7, 6, 7, 6);
        }
    }

    @Override
    public int getRequiredHeight() {
        return 7;
    }
}
