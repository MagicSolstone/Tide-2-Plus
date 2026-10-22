package com.li64.tide.client.gui.screens.journal.components;

import com.li64.tide.Tide;
import com.li64.tide.client.gui.screens.journal.ProfileComponent;
import com.li64.tide.data.fishing.conditions.FishingCondition;
import com.li64.tide.data.fishing.conditions.types.DimensionsCondition;
import com.li64.tide.data.fishing.conditions.types.FishingMediumCondition;
import com.li64.tide.data.fishing.mediums.FishingMedium;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DimensionsComponent extends ProfileComponent {
    private static final Identifier DIMENSIONS = Tide.resource("textures/gui/journal/dimensions.png");
    private static final Component TITLE = Component.translatable("journal.info.dimensions.title");

    public List<ResourceKey<Level>> dimensions;

    public static boolean shouldCreate(DimensionsCondition condition, List<FishingCondition> others) {
        boolean isWater = others.stream().filter(cond -> cond instanceof FishingMediumCondition)
                .findFirst().map(cond -> ((FishingMediumCondition)cond).getMediumId()
                        .equals(FishingMedium.WATER.id().getPath())).orElse(false);
        if (isWater && condition.isOverworldOnly()) return false;
        return condition.getDimensions().stream().anyMatch(DimensionsComponent::isKnown);
    }

    public DimensionsComponent(List<ResourceKey<Level>> dimensions) {
        this.dimensions = dimensions.stream().filter(DimensionsComponent::isKnown).toList();
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, Font font, int x, int y, int mouseX, int mouseY, float partialTick) {
        int center = x + AREA_WIDTH / 2;
        graphics.drawString(font, TITLE, center - font.width(TITLE) / 2, y, TEXT_COLOR, false);

        int count = dimensions.size();
        int spriteY = y + 12;
        int padding = 4;

        for (int i = 0; i < count; i++) {
            int cellSize = 10 + padding;
            int spriteX = center - ((count - 1) * cellSize / 2) + (i * cellSize) - 4;

            int offset = getOffset(dimensions.get(i));
            graphics.blit(DIMENSIONS, spriteX, spriteY, offset, 0, 10, 10, 30, 10);

            if (mouseX >= spriteX && mouseX <= spriteX + 10 && mouseY >= spriteY && mouseY <= spriteY + 10)
                graphics.renderTooltip(font, Component.translatable("journal.info.dimensions." + dimensions.get(i).location().getPath()), mouseX, mouseY);
        }
    }

    public static boolean isKnown(ResourceKey<Level> dimension) {
        return getOffset(dimension) >= 0;
    }

    public static int getOffset(ResourceKey<Level> dimension) {
        if (dimension == Level.OVERWORLD) return 0;
        if (dimension == Level.NETHER) return 10;
        if (dimension == Level.END) return 20;
        return -1;
    }

    @Override
    public int getRequiredHeight() {
        return 26;
    }
}
