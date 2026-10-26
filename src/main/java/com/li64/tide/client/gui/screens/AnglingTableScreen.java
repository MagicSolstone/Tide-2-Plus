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

    /** The table's tabs (not when rigging from a tackle box, which has no Craft tab). */
    protected boolean showTabs() {
        return true;
    }

    @Override
    protected void subInit() {
        super.subInit();
        com.li64.tide.client.MouseKeep.restore();
        if (showTabs())
            this.addRenderableWidget(net.minecraft.client.gui.components.Button.builder(Component.literal("Craft gear"), b -> {
                                com.li64.tide.client.MouseKeep.save();
                                Tide.NETWORK.sendToServer(new com.li64.tide.network.messages.BenchTabMsg(true));
                            })
                    .bounds(this.leftPos + this.imageWidth - 72, this.topPos - 17, 72, 16).build());
    }

    @Override
    public void extractBackground(@NotNull GuiGraphicsExtractor g, int mouseX, int mouseY, float a) {
        com.li64.tide.client.gui.BenchStyle.frame(g, this.leftPos, this.topPos, this.imageWidth, this.imageHeight);
        super.extractBackground(g, mouseX, mouseY, a);
        slotHints(g);
        rigPanel(g);
    }

    /** What goes in each slot: a faded picture while it is empty, and a small label. */
    private static final String[][] HINTS = {{"minecraft:fishing_rod", "Rod"}, {"tide:fishing_line", "Line"}, {"tide:red_bobber", "Bobber"},
            {"tide:fishing_hook", "Hook"}, {"tide:spinner_lure", "Lure"}};

    private void slotHints(GuiGraphicsExtractor g) {
        for (int i = 0; i < HINTS.length; i++) {
            var slot = this.menu.getSlot(i);
            var item = net.minecraft.core.registries.BuiltInRegistries.ITEM.getOptional(net.minecraft.resources.Identifier.parse(HINTS[i][0]));
            com.li64.tide.client.gui.BenchStyle.hint(g, this.font, this.leftPos + slot.x, this.topPos + slot.y,
                    item.map(net.minecraft.world.item.ItemStack::new).orElse(net.minecraft.world.item.ItemStack.EMPTY), HINTS[i][1], !slot.hasItem());
        }
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
        int wrap = (int) ((w - 10) / s);
        // one card per rig: its colour down the side, its name and tier, what it gives now and what the next piece adds
        int cy = y;
        int index = 0;
        for (var p : rigs.subList(0, Math.min(3, rigs.size()))) {
            boolean counts = index++ < com.li64.tide.data.rods.RigSets.MAX_RIGS;      // (a rod carries two rigs: the two with the most pieces)
            var set = p.set();
            int n = p.have(), full = com.li64.tide.data.rods.RigSets.PIECES, half = com.li64.tide.data.rods.RigSets.HALF;
            int colour = 0xFF000000 | com.li64.tide.data.rods.RigSets.color(set);
            java.util.List<net.minecraft.util.FormattedCharSequence> text = new java.util.ArrayList<>();
            java.util.List<Integer> colours = new java.util.ArrayList<>();
            java.util.function.BiConsumer<String, Integer> add = (line, c) -> {
                for (var part : this.font.split(Component.literal(line), wrap)) { text.add(part); colours.add(c); }
            };
            String tier = n >= half ? com.li64.tide.data.rods.RigSets.tier(n) : Math.min(n, full) + " of " + full;
            if (!counts && n >= half) add.accept("Not counted: a rod carries two rigs", 0xFFFF8A7A);
            if (n >= full) add.accept("Tier 2 (3+): " + set.bonusText(), 0xFFE8E8E8);
            else {
                if (n >= half) add.accept("Tier 1 (2+): " + set.twoText(), 0xFFE8E8E8);
                else add.accept("Tier 1 (2+): " + set.twoText(), 0xFFA8B4BC);
                add.accept("Tier 2 (3+): " + set.bonusText(), 0xFFA8B4BC);
                if (!p.canAdd().isEmpty()) add.accept("Add a " + String.join(" or ", p.canAdd()), colour);
            }
            int h = (int) ((12 + text.size() * 10) * s) + 5;
            g.fill(x, cy, x + w, cy + h, 0xD0101820);
            g.fill(x, cy, x + 2, cy + h, colour);
            // the pieces it has, as dots
            for (int d = 0; d < full; d++) {
                int dx = x + w - 6 - (full - 1 - d) * 5;
                g.fill(dx, cy + 3, dx + 3, cy + 6, d < n ? colour : 0xFF3A4450);
            }
            g.pose().pushMatrix();
            g.pose().translate(x + 5, cy + 3);
            g.pose().scale(s, s);
            com.li64.tide.client.Gui26.text(g, this.font, set.name(), 0, 0, colour, true);
            com.li64.tide.client.Gui26.text(g, this.font, tier, this.font.width(set.name()) + 6, 0, n >= half ? 0xFFFFE08A : 0xFF8C97A0, true);
            for (int i = 0, ly = 12; i < text.size(); i++, ly += 10)
                com.li64.tide.client.Gui26.text(g, this.font, text.get(i), 0, ly, colours.get(i), true);
            g.pose().popMatrix();
            cy += h + 3;
        }
    }

    @Override
    protected void extractErrorIcon(@NotNull GuiGraphicsExtractor graphics, int x, int y) {}
}
