package com.li64.tide.client.gui.screens;

import com.li64.tide.registries.items.TackleBoxItem;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;

/** The tackle box: drawn as a green box with its latch strip, so it's clearly not a chest. */
public class TackleBoxScreen extends AbstractContainerScreen<TackleBoxItem.Menu> {
    private static final int OUTLINE = 0xFF1F3A24, BODY = 0xFF5FA05E, LIGHT = 0xFF93D08A, SHADE = 0xFF3B7440;
    private static final int SLOT_DARK = 0xFF2C5131, SLOT_FILL = 0xFF467C46, SLOT_LIGHT = 0xFF86BF7E;
    private static final int METAL = 0xFFC9C9C9, METAL_DARK = 0xFF7E7E7E;

    public TackleBoxScreen(TackleBoxItem.Menu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 114 + 3 * 18);
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void extractBackground(@NotNull GuiGraphicsExtractor g, int mouseX, int mouseY, float a) {
        super.extractBackground(g, mouseX, mouseY, a);
        int x = this.leftPos, y = this.topPos, w = this.imageWidth, h = this.imageHeight;
        // the box: outline, body, a light top-left edge and a shaded bottom-right edge
        g.fill(x, y + 1, x + w, y + h - 1, OUTLINE);
        g.fill(x + 1, y, x + w - 1, y + h, OUTLINE);
        g.fill(x + 1, y + 1, x + w - 1, y + h - 1, BODY);
        g.fill(x + 1, y + 1, x + w - 1, y + 3, LIGHT);
        g.fill(x + 1, y + 1, x + 3, y + h - 1, LIGHT);
        g.fill(x + 1, y + h - 3, x + w - 1, y + h - 1, SHADE);
        g.fill(x + w - 3, y + 1, x + w - 1, y + h - 1, SHADE);
        // the seam between the tray and your inventory, with two latches
        int seam = y + 17 + 3 * 18 + 1;        // just under the tray, above the "Inventory" label
        g.fill(x + 3, seam, x + w - 3, seam + 4, SHADE);
        g.fill(x + 3, seam + 1, x + w - 3, seam + 2, OUTLINE);
        for (int lx : new int[]{x + 80, x + w - 30}) {        // clear of the label
            g.fill(lx, seam - 2, lx + 8, seam + 6, METAL_DARK);
            g.fill(lx + 1, seam - 1, lx + 7, seam + 4, METAL);
        }
        // a slot under every slot, sunk into the box
        for (Slot s : this.menu.slots) {
            int sx = x + s.x - 1, sy = y + s.y - 1;
            g.fill(sx, sy, sx + 18, sy + 18, SLOT_LIGHT);
            g.fill(sx, sy, sx + 17, sy + 17, SLOT_DARK);
            g.fill(sx + 1, sy + 1, sx + 17, sy + 17, SLOT_FILL);
        }
    }

    @Override
    protected void extractLabels(@NotNull GuiGraphicsExtractor g, int xm, int ym) {
        g.text(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xFF14281A, false);
        g.text(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0xFF14281A, false);
    }
}
