package com.li64.tide.client.gui.screens;

import com.li64.tide.registries.items.FishSatchelItem;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;

/** Inside the fish satchel: a leather pouch with a strap between your catch and your inventory. */
public class SatchelScreen extends AbstractContainerScreen<FishSatchelItem.Menu> {
    private static final int OUTLINE = 0xFF2E1C0E, BODY = 0xFF8A5A32, LIGHT = 0xFFB07A48, SHADE = 0xFF5E3A1C, BAND = 0xFF4A2E16, BAND_TEXT = 0xFFF2E2C8;
    private static final int SLOT_DARK = 0xFF3E2610, SLOT_FILL = 0xFF6E4624, SLOT_LIGHT = 0xFFA87A4E, STITCH = 0xFFD8B888;
    private static final int GRID_BOTTOM = FishSatchelItem.Menu.TOP + FishSatchelItem.Menu.ROWS * 18;

    public SatchelScreen(FishSatchelItem.Menu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, FishSatchelItem.Menu.WIDTH, GRID_BOTTOM + 14 + 76 + 6);
        this.inventoryLabelY = GRID_BOTTOM + 3;
        this.inventoryLabelX = (FishSatchelItem.Menu.WIDTH - 162) / 2;
    }

    @Override
    public void extractBackground(@NotNull GuiGraphicsExtractor g, int mouseX, int mouseY, float a) {
        super.extractBackground(g, mouseX, mouseY, a);
        int x = this.leftPos, y = this.topPos, w = this.imageWidth, h = this.imageHeight;
        g.fill(x, y + 1, x + w, y + h - 1, OUTLINE);
        g.fill(x + 1, y, x + w - 1, y + h, OUTLINE);
        g.fill(x + 1, y + 1, x + w - 1, y + h - 1, BODY);
        g.fill(x + 1, y + 1, x + w - 1, y + 3, LIGHT);
        g.fill(x + 1, y + 1, x + 3, y + h - 1, LIGHT);
        g.fill(x + 1, y + h - 3, x + w - 1, y + h - 1, SHADE);
        g.fill(x + w - 3, y + 1, x + w - 1, y + h - 1, SHADE);
        for (int sx = x + 6; sx < x + w - 6; sx += 4) g.fill(sx, y + 4, sx + 2, y + 5, STITCH);       // stitching along the top
        int band = y + GRID_BOTTOM + 1;                                                                 // the strap, carrying "Inventory"
        g.fill(x + 3, band, x + w - 3, band + 12, OUTLINE);
        g.fill(x + 3, band + 1, x + w - 3, band + 11, BAND);
        g.fill(x + w - 22, band - 1, x + w - 12, band + 13, 0xFF9A9A9A);                               // buckle
        g.fill(x + w - 20, band + 1, x + w - 14, band + 11, BAND);
        for (Slot s : this.menu.slots) {
            int sx = x + s.x - 1, sy = y + s.y - 1;
            g.fill(sx, sy, sx + 18, sy + 18, SLOT_LIGHT);
            g.fill(sx, sy, sx + 17, sy + 17, SLOT_DARK);
            g.fill(sx + 1, sy + 1, sx + 17, sy + 17, SLOT_FILL);
        }
    }

    @Override
    protected void extractLabels(@NotNull GuiGraphicsExtractor g, int xm, int ym) {
        g.text(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xFF2E1C0E, false);
        g.text(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, BAND_TEXT, false);
    }
}
