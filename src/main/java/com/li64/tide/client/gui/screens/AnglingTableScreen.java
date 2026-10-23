package com.li64.tide.client.gui.screens;

import com.li64.tide.Tide;
import com.li64.tide.client.gui.menus.AnglingTableMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class AnglingTableScreen extends ItemCombinerScreen<AnglingTableMenu> {
    public static final Identifier GUI_LOCATION = Tide.resource("textures/gui/container/angling_table.png");

    public AnglingTableScreen(AnglingTableMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component, GUI_LOCATION);
        this.titleLabelX = 56;
        this.titleLabelY = 8;
    }

    @Override
    public void extractBackground(@NotNull GuiGraphicsExtractor g, int mouseX, int mouseY, float a) {
        super.extractBackground(g, mouseX, mouseY, a);
        rigPanel(g);
    }

    /**
     * Matched rigs for the rod on the table: a small card to the left for each rig it has a piece of, with how
     * many pieces it has, what kinds could still be added, and the bonus once it has three.
     */
    private void rigPanel(GuiGraphicsExtractor g) {
        if (this.menu.getSlot(0).getItem().isEmpty()) return;
        java.util.List<net.minecraft.world.item.ItemStack> pieces = new java.util.ArrayList<>();
        for (int i = 0; i <= 4; i++) pieces.add(this.menu.getSlot(i).getItem());
        net.minecraft.world.item.ItemStack rod = this.menu.getSlot(0).getItem();
        java.util.Set<net.minecraft.world.item.Item> baits = new java.util.HashSet<>();
        for (net.minecraft.world.item.ItemStack bait : com.li64.tide.util.BaitUtils.getBaitItems(rod))
            if (baits.add(bait.getItem())) pieces.add(bait);
        var rigs = com.li64.tide.data.rods.RigSets.progress(pieces);
        if (rigs.isEmpty()) return;
        int w = 124, x = this.leftPos - w - 6, y = this.topPos;
        if (x < 2) return;           // no room on a narrow screen
        float s = 0.75f;
        int need = com.li64.tide.data.rods.RigSets.PIECES;
        // a dark card, then each rig: name and count, the bonus (wrapped), what kinds of piece could still go on
        int rows = 0;
        for (var p : rigs.subList(0, Math.min(3, rigs.size())))
            rows += 2 + this.font.split(Component.literal("At 3: " + p.set().bonusText()), (int) ((w - 8) / s)).size() + (p.canAdd().isEmpty() ? 0 : 1);
        int h = (int) (rows * 10 * s) + 6;
        g.fill(x, y, x + w, y + h, 0xC0101820);
        g.fill(x, y, x + 2, y + h, 0xFFB070E0);
        g.pose().pushMatrix();
        g.pose().translate(x + 5, y + 4);
        g.pose().scale(s, s);
        int ly = 0;
        for (var p : rigs.subList(0, Math.min(3, rigs.size()))) {
            boolean done = p.have() >= need;
            com.li64.tide.client.Gui26.text(g, this.font, p.set().name() + "  " + Math.min(p.have(), 6) + "/" + need + (done ? "  complete" : ""), 0, ly, done ? 0xFF7FE07F : 0xFFFFD27F, true);
            ly += 10;
            for (var part : this.font.split(Component.literal((done ? "" : "At 3: ") + p.set().bonusText()), (int) ((w - 8) / s))) {
                com.li64.tide.client.Gui26.text(g, this.font, part, 0, ly, done ? 0xFFE8E8E8 : 0xFFA8B4BC, true);
                ly += 10;
            }
            if (!done && !p.canAdd().isEmpty()) {
                com.li64.tide.client.Gui26.text(g, this.font, "Add: " + String.join(", ", p.canAdd()), 0, ly, 0xFFB070E0, true);
                ly += 10;
            }
            ly += 10;
        }
        g.pose().popMatrix();
    }

    @Override
    protected void extractErrorIcon(@NotNull GuiGraphicsExtractor graphics, int x, int y) {}
}
