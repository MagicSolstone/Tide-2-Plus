package com.li64.tide.client.gui.screens;

import com.li64.tide.registries.items.TackleBoxItem;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

/** The angling table, rigged from a tackle box: the box's tackle sits where the inventory rows usually are. */
public class TackleRigScreen extends AnglingTableScreen {
    private static final Component BOX = Component.literal("Tackle box");

    @SuppressWarnings({"unchecked", "rawtypes"})
    public TackleRigScreen(TackleBoxItem.RigMenu menu, Inventory inventory, Component title) {
        super((com.li64.tide.client.gui.menus.AnglingTableMenu) menu, inventory, title);
    }

    /** The table, then your own inventory rows under it. */
    private static final int TALL = TackleBoxItem.RigMenu.OWN_Y + 3 * 18 + 6;

    @Override
    protected int panelHeight() {
        return TALL;
    }

    @Override
    protected void subInit() {
        this.topPos = Math.max(18, (this.height - TALL) / 2);      // centred for the taller bench (the buttons follow)
        super.subInit();
    }

    @Override
    public void extractBackground(@NotNull GuiGraphicsExtractor g, int mouseX, int mouseY, float a) {
        super.extractBackground(g, mouseX, mouseY, a);
        // tint the box's rows green so it's clear they're the box, not your inventory
        int x = this.leftPos + 7, y = this.topPos + 83;
        g.fill(x, y, x + 162, y + 54, 0x553B7440);
        // below the table: more bench, with your own inventory rows
        int py = this.topPos + TABLE_H - 4;
        g.fill(this.leftPos + 3, py, this.leftPos + this.imageWidth - 3, this.topPos + TALL - 3, com.li64.tide.client.gui.BenchStyle.PANEL);
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                com.li64.tide.client.gui.BenchStyle.slot(g, this.leftPos + 8 + col * 18, this.topPos + TackleBoxItem.RigMenu.OWN_Y + row * 18);
        // the box's rod rack, hung off the right edge
        int rx = this.leftPos + com.li64.tide.registries.items.TackleBoxItem.Menu.RACK_X - 4, ry = this.topPos + 70;
        int rh = 18 + TackleBoxItem.MAX_RODS * 18;
        g.fill(rx, ry, rx + 26, ry + rh, 0xFF1F3A24);
        g.fill(rx + 1, ry + 1, rx + 25, ry + rh - 1, 0xFF5FA05E);
        for (int i = 0; i < TackleBoxItem.MAX_RODS; i++) {
            int sx = rx + 3, sy = this.topPos + 83 + i * 18;
            g.fill(sx, sy, sx + 18, sy + 18, 0xFF86BF7E);
            g.fill(sx, sy, sx + 17, sy + 17, 0xFF2C5131);
            g.fill(sx + 1, sy + 1, sx + 17, sy + 17, 0xFF467C46);
        }
    }

    @Override
    protected boolean showTabs() {
        return false;
    }

    @Override
    protected int rightReserve() {
        return 32;
    }

    @Override
    protected boolean hasClickedOutside(double mx, double my, int xo, int yo) {
        int rx = xo + com.li64.tide.registries.items.TackleBoxItem.Menu.RACK_X - 4;
        if (mx >= rx && mx < rx + 26 && my >= yo + 70 && my < yo + 70 + 18 + TackleBoxItem.MAX_RODS * 18) return false;      // the rod rack
        if (mx >= xo && mx < xo + this.imageWidth && my >= yo && my < yo + TALL) return false;      // your rows under the table
        return super.hasClickedOutside(mx, my, xo, yo);
    }

    @Override
    protected void extractLabels(@NotNull GuiGraphicsExtractor g, int xm, int ym) {
        g.text(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xFF404040, false);
        g.text(this.font, BOX, this.inventoryLabelX, this.inventoryLabelY, 0xFF1F5A28, false);
        g.text(this.font, Component.literal("Rods"), com.li64.tide.registries.items.TackleBoxItem.Menu.RACK_X - 4, 72, 0xFF14281A, false);
        g.text(this.font, this.playerInventoryTitle, this.inventoryLabelX, TackleBoxItem.RigMenu.OWN_Y - 11, 0xFF404040, false);
    }
}
