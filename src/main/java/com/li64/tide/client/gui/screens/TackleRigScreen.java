package com.li64.tide.client.gui.screens;

import com.li64.tide.Tide;
import com.li64.tide.network.messages.OpenTackleMsg;
import com.li64.tide.registries.items.TackleBoxItem;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
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

    @Override
    protected void subInit() {
        super.subInit();
        this.addRenderableWidget(Button.builder(Component.literal("< Box"), b -> Tide.NETWORK.sendToServer(new OpenTackleMsg(false)))
                .bounds(this.leftPos + this.imageWidth - 44, this.topPos - 16, 44, 14).build());
    }

    @Override
    public void extractBackground(@NotNull GuiGraphicsExtractor g, int mouseX, int mouseY, float a) {
        super.extractBackground(g, mouseX, mouseY, a);
        // tint the box's rows green so it's clear they're the box, not your inventory
        int x = this.leftPos + 7, y = this.topPos + 83;
        g.fill(x, y, x + 162, y + 54, 0x553B7440);
        // the box's rod rack, hung off the right edge
        int rx = this.leftPos + com.li64.tide.registries.items.TackleBoxItem.Menu.RACK_X - 4, ry = this.topPos + 70;
        g.fill(rx, ry, rx + 26, ry + 72, 0xFF1F3A24);
        g.fill(rx + 1, ry + 1, rx + 25, ry + 71, 0xFF5FA05E);
        for (int i = 0; i < 3; i++) {
            int sx = rx + 3, sy = this.topPos + 83 + i * 18;
            g.fill(sx, sy, sx + 18, sy + 18, 0xFF86BF7E);
            g.fill(sx, sy, sx + 17, sy + 17, 0xFF2C5131);
            g.fill(sx + 1, sy + 1, sx + 17, sy + 17, 0xFF467C46);
        }
    }

    @Override
    protected boolean hasClickedOutside(double mx, double my, int xo, int yo) {
        int rx = xo + com.li64.tide.registries.items.TackleBoxItem.Menu.RACK_X - 4;
        if (mx >= rx && mx < rx + 26 && my >= yo + 70 && my < yo + 142) return false;      // the rod rack
        return super.hasClickedOutside(mx, my, xo, yo);
    }

    @Override
    protected void extractLabels(@NotNull GuiGraphicsExtractor g, int xm, int ym) {
        g.text(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xFF404040, false);
        g.text(this.font, BOX, this.inventoryLabelX, this.inventoryLabelY, 0xFF1F5A28, false);
        g.text(this.font, Component.literal("Rods"), com.li64.tide.registries.items.TackleBoxItem.Menu.RACK_X - 4, 72, 0xFF14281A, false);
    }
}
