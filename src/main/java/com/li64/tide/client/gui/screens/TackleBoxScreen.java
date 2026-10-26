package com.li64.tide.client.gui.screens;

import com.li64.tide.Tide;
import com.li64.tide.network.messages.OpenTackleMsg;
import com.li64.tide.registries.items.TackleBoxItem;
import net.minecraft.client.gui.components.Button;
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
    private static final int METAL = 0xFFC9C9C9, METAL_DARK = 0xFF7E7E7E, BAND = 0xFF2A5530, BAND_TEXT = 0xFFDDEFD8;

    public TackleBoxScreen(TackleBoxItem.Menu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 204, 114 + 3 * 18);
        this.inventoryLabelY = 17 + 3 * 18 + 3;     // inside the band
    }

    @Override
    protected void init() {
        super.init();
        com.li64.tide.client.MouseKeep.restore();
        this.addRenderableWidget(Button.builder(Component.literal("Assemble rod"), b -> {
                    com.li64.tide.client.MouseKeep.save();
                    Tide.NETWORK.sendToServer(new OpenTackleMsg(true));
                }).bounds(this.leftPos + this.imageWidth - 80, this.topPos - 17, 80, 16).build());
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
        // a dark band between the tray and your inventory, carrying the "Inventory" label, with a latch at each end
        int band = y + 17 + 3 * 18 + 1;
        g.fill(x + 3, band, x + w - 3, band + 12, OUTLINE);
        g.fill(x + 3, band + 1, x + w - 3, band + 11, BAND);
        for (int lx : new int[]{x + w - 26, x + w - 14}) {
            g.fill(lx, band - 2, lx + 8, band + 13, METAL_DARK);
            g.fill(lx + 1, band - 1, lx + 7, band + 11, METAL);
        }
        // the rod rack: a darker strip down the right of the tray
        int rx = x + TackleBoxItem.Menu.RACK_X - 4;
        g.fill(rx, y + 14, rx + 26, y + 18 + 3 * 18 + 2, OUTLINE);
        g.fill(rx + 1, y + 15, rx + 25, y + 18 + 3 * 18 + 1, SHADE);
        // a carrying handle on the lid
        int hx = x + w / 2 - 40;      // (left of centre: the Assemble rod button sits at the top right)
        g.fill(hx, y - 7, hx + 48, y + 1, OUTLINE);
        g.fill(hx + 1, y - 6, hx + 47, y, SHADE);
        g.fill(hx + 6, y - 4, hx + 42, y, OUTLINE);
        for (int lx : new int[]{hx - 3, hx + 46}) {
            g.fill(lx, y - 3, lx + 5, y + 3, METAL_DARK);
            g.fill(lx + 1, y - 2, lx + 4, y + 2, METAL);
        }
        // a slot under every slot, sunk into the box
        for (Slot s : this.menu.slots) {
            int sx = x + s.x - 1, sy = y + s.y - 1;
            g.fill(sx, sy, sx + 18, sy + 18, SLOT_LIGHT);
            g.fill(sx, sy, sx + 17, sy + 17, SLOT_DARK);
            g.fill(sx + 1, sy + 1, sx + 17, sy + 17, SLOT_FILL);
        }
        // the tray's dividers, drawn over the slot edges: a trough every third column
        for (int col = 3; col < 9; col += 3) {
            int dx = x + 7 + col * 18;
            g.fill(dx - 1, y + 16, dx + 1, y + 17 + 3 * 18, OUTLINE);
        }
    }

    @Override
    protected void extractLabels(@NotNull GuiGraphicsExtractor g, int xm, int ym) {
        g.text(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xFF14281A, false);
        g.text(this.font, Component.literal("Rods"), TackleBoxItem.Menu.RACK_X - 5, this.titleLabelY, 0xFF14281A, false);
        g.text(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, BAND_TEXT, false);
    }
}
