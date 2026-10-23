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
    private long ciLogged = 0;

    /** Room taken up just right of the table (the tackle box's rod rack). */
    protected int rightReserve() {
        return 4;
    }

    private void rigPanel(GuiGraphicsExtractor g) {
        if (this.menu.getSlot(0).getItem().isEmpty()) return;
        java.util.List<net.minecraft.world.item.ItemStack> pieces = new java.util.ArrayList<>();
        for (int i = 0; i <= 4; i++) pieces.add(this.menu.getSlot(i).getItem());
        net.minecraft.world.item.ItemStack rod = this.menu.getSlot(0).getItem();
        java.util.Set<net.minecraft.world.item.Item> baits = new java.util.HashSet<>();
        for (net.minecraft.world.item.ItemStack bait : com.li64.tide.util.BaitUtils.getBaitItems(rod))
            if (baits.add(bait.getItem())) pieces.add(bait);
        var rigs = com.li64.tide.data.rods.RigSets.progress(pieces);
        // to the left of the table if there's room, else to the right (past the tackle box's rod rack)
        int y = this.topPos, w = Math.min(124, this.leftPos - 6), x = this.leftPos - w - 4;
        if (w < 90) {
            int right = this.leftPos + this.imageWidth + rightReserve();
            w = Math.min(124, this.width - right - 4);
            x = right;
        }
        if (Tide.CI && System.currentTimeMillis() - ciLogged > 1000) {
            ciLogged = System.currentTimeMillis();
            Tide.LOG.info("TIDE-CI rig card: {} rig(s) for {} at x={}", rigs.size(), pieces.stream().map(p -> p.getItem().toString()).toList(), x);
        }
        if (rigs.isEmpty() || w < 90) return;
        float s = 0.75f;
        int wrap = (int) ((w - 8) / s);
        // each rig: name and count, what it gives now, what the next piece adds, what kinds of piece could still go on
        java.util.List<net.minecraft.util.FormattedCharSequence> text = new java.util.ArrayList<>();
        java.util.List<Integer> colours = new java.util.ArrayList<>();
        java.util.function.BiConsumer<String, Integer> add = (line, colour) -> {
            for (var part : this.font.split(Component.literal(line), wrap)) { text.add(part); colours.add(colour); }
        };
        for (var p : rigs.subList(0, Math.min(3, rigs.size()))) {
            var set = p.set();
            int n = p.have(), full = com.li64.tide.data.rods.RigSets.PIECES, half = com.li64.tide.data.rods.RigSets.HALF;
            add.accept(set.name() + "  " + Math.min(n, full) + "/" + full + (n >= full ? "  complete" : ""), n >= full ? 0xFF7FE07F : n >= half ? 0xFFFFD27F : 0xFFC8B48A);
            if (n >= full) add.accept(set.bonusText(), 0xFFE8E8E8);
            else {
                if (n >= half) add.accept("Now: " + set.twoText(), 0xFFE8E8E8);
                else add.accept("At 2: " + set.twoText(), 0xFFA8B4BC);
                add.accept("At 3: " + set.bonusText(), 0xFFA8B4BC);
                if (!p.canAdd().isEmpty()) add.accept("Add: " + String.join(", ", p.canAdd()), 0xFFB070E0);
            }
            text.add(null); colours.add(0);
        }
        int h = (int) (text.size() * 10 * s) + 2;
        g.fill(x, y, x + w, y + h, 0xC0101820);
        g.fill(x, y, x + 2, y + h, 0xFFB070E0);
        g.pose().pushMatrix();
        g.pose().translate(x + 5, y + 4);
        g.pose().scale(s, s);
        for (int i = 0, ly = 0; i < text.size(); i++, ly += 10)
            if (text.get(i) != null) com.li64.tide.client.Gui26.text(g, this.font, text.get(i), 0, ly, colours.get(i), true);
        g.pose().popMatrix();
    }

    @Override
    protected void extractErrorIcon(@NotNull GuiGraphicsExtractor graphics, int x, int y) {}
}
