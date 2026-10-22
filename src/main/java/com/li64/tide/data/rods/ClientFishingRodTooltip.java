package com.li64.tide.data.rods;

import com.li64.tide.util.TideUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record ClientFishingRodTooltip(int slots, BaitContents contents) implements ClientTooltipComponent {
    private static final Identifier SLOT_BACKGROUND_SPRITE = TideUtils.sprite("bait/slot_background");
    private static final Component MESSAGE = Component.translatable("text.tide.rod_tooltip.bait_desc");
    private static final int OFFSET_Y = 10;
    private static final int MARGIN_Y = 4;
    private static final int BG_BORDER = 1;
    private static final int SLOT_SIZE_X = 20;
    private static final int SLOT_SIZE_Y = 20;

    @Override
    public int getHeight() {
        return this.backgroundHeight() + MARGIN_Y + OFFSET_Y;
    }

    public int getWidth(@NotNull Font font) {
        return Math.max(this.backgroundWidth(), font.width(MESSAGE));
    }

    private int backgroundWidth() {
        return this.gridWidth() * SLOT_SIZE_X + BG_BORDER * 2;
    }

    private int backgroundHeight() {
        return SLOT_SIZE_Y + BG_BORDER * 2;
    }

    @Override
    public void renderImage(@NotNull Font font, int x, int y, @NotNull GuiGraphicsExtractor graphics) {
        int gridWidth = this.gridWidth();

        graphics.drawString(font, MESSAGE, x, y, DyeColor.LIGHT_GRAY.getTextColor());

        for (int i = 0; i < gridWidth; i++) {
            int dspX = x + i * SLOT_SIZE_X + BG_BORDER;
            int dspY = y + BG_BORDER + OFFSET_Y;

            this.renderSlot(dspX, dspY, i, graphics, font);
        }
    }

    private void renderSlot(int x, int y, int index, GuiGraphicsExtractor graphics, Font font) {
        graphics.blitSprite(SLOT_BACKGROUND_SPRITE, x - 4 + BG_BORDER, y - 4 + BG_BORDER, 24, 24);

        if (index >= this.contents.size()) return;
        ItemStack stack = this.contents.items().get(index);

        graphics.renderItem(stack, x + BG_BORDER, y + BG_BORDER, index);
        graphics.renderItemDecorations(font, stack, x + BG_BORDER, y + BG_BORDER);
    }

    private int gridWidth() {
        return Math.max(this.slots, this.contents.size());
    }
}