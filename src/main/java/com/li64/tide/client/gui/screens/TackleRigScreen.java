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
    }

    @Override
    protected void extractLabels(@NotNull GuiGraphicsExtractor g, int xm, int ym) {
        g.text(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xFF404040, false);
        g.text(this.font, BOX, this.inventoryLabelX, this.inventoryLabelY, 0xFF1F5A28, false);
    }
}
