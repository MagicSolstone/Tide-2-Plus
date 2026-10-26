package com.li64.tide.client.gui.screens.journal.components;

import com.li64.tide.client.Gui26;
import com.li64.tide.client.gui.screens.journal.ProfileComponent;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class LuckComponent extends ProfileComponent {
    private final Component text;

    public LuckComponent(int min, int max) {
        boolean hasMin = min > 0;
        boolean hasMax = max < Integer.MAX_VALUE;
        // luck points are shown as rare-find percentages everywhere else
        int per = (int) Math.round(com.li64.tide.data.rods.RodEffects.RARE_PER_LUCK * 100);
        String range = "+" + min * per + "% or more";
        if (!hasMin && !hasMax) range = "any";
        else if (hasMin && hasMax) range = min * per + "% to " + max * per + "%";
        else if (hasMax) range = "under " + (max + 1) * per + "%";
        this.text = Component.translatable("journal.info.luck.title").append(": ").append(range);
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
