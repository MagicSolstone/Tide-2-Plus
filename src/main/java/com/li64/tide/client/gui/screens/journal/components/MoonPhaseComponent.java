package com.li64.tide.client.gui.screens.journal.components;

import com.li64.tide.Tide;
import com.li64.tide.client.gui.screens.journal.ProfileComponent;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MoonPhaseComponent extends ProfileComponent {
    private static final Identifier MOON_PHASES = Tide.resource("textures/gui/journal/moon_phases.png");
    private static final Component TITLE = Component.translatable("journal.info.moon_phase.title");

    public List<Integer> phases;

    public MoonPhaseComponent(List<Integer> phases) {
        this.phases = phases;
    }

    @Override
    public void render(@NotNull GuiGraphicsExtractor graphics, Font font, int x, int y, int mouseX, int mouseY, float partialTick) {
        int center = x + AREA_WIDTH / 2;
        graphics.drawString(font, TITLE, center - font.width(TITLE) / 2, y, TEXT_COLOR, false);

        int phaseCount = phases.size();
        int phasesY = y + 12;
        int padding = 4;

        for (int i = 0; i < phaseCount; i++) {
            int cellSize = 16 + padding;
            int phaseX = center - ((phaseCount - 1) * cellSize / 2) + (i * cellSize) - 4;

            graphics.pose().pushPose();
            graphics.pose().translate(phaseX + 4, phasesY, 0f);
            graphics.pose().scale(2f, 2f, 1f);
            graphics.pose().translate(-(phaseX + 4), -phasesY, 0f);
            graphics.blit(MOON_PHASES, phaseX, phasesY, phases.get(i) * 8, 0, 8, 8, 64, 8);
            graphics.pose().popPose();

            if (mouseX >= phaseX - 4 && mouseX <= phaseX + 12 && mouseY >= phasesY && mouseY <= phasesY + 16)
                graphics.renderTooltip(font, Component.translatable("journal.info.moon_phase." + phases.get(i)), mouseX, mouseY);
        }
    }

    @Override
    public int getRequiredHeight() {
        return 28;
    }
}
