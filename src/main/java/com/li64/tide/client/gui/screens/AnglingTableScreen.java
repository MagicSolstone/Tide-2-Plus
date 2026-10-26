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
        // the screen's own background first (it dims and blurs everything drawn before it, and draws the grey panel),
        // then the frame and the panel again in wood on top
        super.extractBackground(g, mouseX, mouseY, a);
        com.li64.tide.client.gui.BenchStyle.frame(g, this.leftPos, this.topPos, this.imageWidth, this.imageHeight);
        com.li64.tide.client.gui.BenchStyle.panel(g, GUI_LOCATION, this.leftPos, this.topPos, this.imageWidth, this.imageHeight);
        slotHints(g);
        rigPanel(g, mouseX, mouseY);
        statsPanel(g);
    }

    /**
     * To the right of the table: what the rod being put together gives, rod and tackle and rigs added up, with every
     * place, time and rig condition it cares about met (the most each piece can add). Updates as pieces go on.
     */
    private void statsPanel(GuiGraphicsExtractor g) {
        var result = this.menu.getSlot(5).getItem();
        var rod = result.isEmpty() ? this.menu.getSlot(0).getItem() : result;
        if (rod.isEmpty() || this.minecraft == null || this.minecraft.player == null) return;
        int x = this.leftPos + this.imageWidth + rightReserve() + 4, w = Math.min(118, this.width - x - 4);
        if (w < 90 || this.leftPos - 6 < 90) return;      // no room (the rig cards took the right side)
        var e = com.li64.tide.data.rods.RodEffects.everythingActive(rod);
        var enchants = this.minecraft.player.level().registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT);
        int lure = net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel(enchants.getOrThrow(net.minecraft.world.item.enchantment.Enchantments.LURE), rod);
        int sea = net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel(enchants.getOrThrow(net.minecraft.world.item.enchantment.Enchantments.LUCK_OF_THE_SEA), rod);
        int speed = lure + Math.min(com.li64.tide.util.BaitUtils.getCombinedSpeed(rod), com.li64.tide.registries.items.TideFishingRodItem.BAIT_SPEED_CAP) + e.speed();
        int luck = e.luck() + com.li64.tide.util.BaitUtils.getCombinedLuck(rod) + sea;
        java.util.List<String[]> rows = new java.util.ArrayList<>();
        java.util.function.BiConsumer<String, Double> row = (label, v) -> {
            long n = Math.round(v * 100);
            if (n != 0) rows.add(new String[]{label, String.format("%+d%%", n), n > 0 ? "g" : "b"});
        };
        row.accept("Cast range", e.cast());
        row.accept("Bite speed", e.biteSpeed(speed));
        row.accept("Hook window", e.window());
        row.accept("Rare finds", com.li64.tide.data.rods.RodEffects.rareFinds(luck));
        row.accept("Catch quality", e.quality());
        row.accept("Fishing XP", e.xp());
        row.accept("Land Good/Bad", e.land());
        row.accept("Fish fight easier", -e.fight());
        row.accept("Catch difficulty", -e.ease());
        row.accept("Second fish", Math.min(e.twin(), com.li64.tide.data.rods.RodEffects.TWIN_CAP));
        row.accept("Comes up cooked", e.cook());
        // where all of that applies
        java.util.LinkedHashSet<String> where = new java.util.LinkedHashSet<>();
        for (var when : com.li64.tide.data.rods.RodEffects.situationalTotals(rod).keySet()) where.add(when.text);
        for (var set : com.li64.tide.data.rods.RigSets.carried(rod)) if (!set.when().text.isEmpty()) where.add(set.when().text);
        float s = 0.75f;
        int inner = (int) ((w - 10) / s);
        java.util.List<net.minecraft.util.FormattedCharSequence> note = new java.util.ArrayList<>(this.font.split(Component.literal(where.isEmpty()
                ? "Works anywhere" : "With everything active: " + String.join(", ", where)), inner));
        int h = (int) ((14 + Math.max(1, rows.size()) * 10 + 3 + note.size() * 9) * s) + 6;
        int y = this.topPos;
        g.fill(x, y, x + w, y + h, 0xD0101820);
        g.fill(x, y, x + 2, y + h, 0xFFC9A44A);
        g.pose().pushMatrix();
        g.pose().translate(x + 5, y + 3);
        g.pose().scale(s, s);
        com.li64.tide.client.Gui26.text(g, this.font, "This rod, in all", 0, 0, 0xFFFFD27F, true);
        int ly = 14;
        if (rows.isEmpty()) { com.li64.tide.client.Gui26.text(g, this.font, "No bonuses yet", 0, ly, 0xFFA8B4BC, true); ly += 10; }
        for (String[] r : rows) {
            com.li64.tide.client.Gui26.text(g, this.font, r[0], 0, ly, 0xFFE8E8E8, true);
            com.li64.tide.client.Gui26.text(g, this.font, r[1], inner - this.font.width(r[1]), ly, r[2].equals("g") ? 0xFF7FE07F : 0xFFFF8A7A, true);
            ly += 10;
        }
        ly += 3;
        for (var line : note) { com.li64.tide.client.Gui26.text(g, this.font, line, 0, ly, 0xFFA8B4BC, true); ly += 9; }
        g.pose().popMatrix();
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
     * The rigs the rod on the table has pieces of, beside the table: a full card (what it gives now and what the next
     * piece adds) for each rig that is working, and just a header for the rest (one piece so far, or past the two a
     * rod carries). Each header has two boxes, Tier 1 and Tier 2, filled in when reached; hover a header for the bonuses.
     */
    private long ciLogged = 0;

    /** Room taken up just right of the table (the tackle box's rod rack). */
    protected int rightReserve() {
        return 4;
    }

    private static final int MAX_SHOWN = 6;

    private void rigPanel(GuiGraphicsExtractor g, int mouseX, int mouseY) {
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
        int half = com.li64.tide.data.rods.RigSets.HALF, full = com.li64.tide.data.rods.RigSets.PIECES;
        int cy = y, index = 0, working = 0;
        java.util.List<Component> hover = null;
        for (var p : rigs.subList(0, Math.min(MAX_SHOWN, rigs.size()))) {
            var set = p.set();
            int n = p.have();
            // the rod carries the two rigs with the most pieces (of those with two or more); the rest give nothing
            boolean counts = n >= half && working < com.li64.tide.data.rods.RigSets.MAX_RIGS;
            if (counts) working++;
            index++;
            int colour = 0xFF000000 | com.li64.tide.data.rods.RigSets.color(set);
            String status = counts ? com.li64.tide.data.rods.RigSets.tier(n) : n >= half ? "not counted" : "1 piece";
            java.util.List<net.minecraft.util.FormattedCharSequence> text = new java.util.ArrayList<>();
            java.util.List<Integer> colours = new java.util.ArrayList<>();
            java.util.function.BiConsumer<String, Integer> add = (line, c) -> {
                for (var part : this.font.split(Component.literal(line), wrap)) { text.add(part); colours.add(c); }
            };
            if (counts) {
                if (n >= full) add.accept("Tier 2 (3+): " + set.bonusText(), 0xFFE8E8E8);
                else {
                    add.accept("Tier 1 (2+): " + set.twoText(), 0xFFE8E8E8);
                    add.accept("Next, Tier 2 (3+): " + set.bonusText(), 0xFFA8B4BC);
                    if (!p.canAdd().isEmpty()) add.accept("Add a " + String.join(" or ", p.canAdd()), colour);
                }
            }
            int h = (int) ((12 + text.size() * 10) * s) + (text.isEmpty() ? 3 : 5);
            g.fill(x, cy, x + w, cy + h, counts ? 0xD0101820 : 0xA8101820);
            g.fill(x, cy, x + 2, cy + h, counts ? colour : dim(colour));
            // two boxes at the right of the header: Tier 1 and Tier 2, filled in when reached (and working)
            for (int t = 0; t < 2; t++) {
                int bx = x + w - 15 + t * 7, by = cy + 2, need = t == 0 ? half : full;
                g.fill(bx, by, bx + 5, by + 5, 0xFF56616B);
                g.fill(bx + 1, by + 1, bx + 4, by + 4, n >= need ? (counts ? colour : 0xFF7A848C) : 0xFF1C252C);
            }
            g.pose().pushMatrix();
            g.pose().translate(x + 5, cy + 3);
            g.pose().scale(s, s);
            com.li64.tide.client.Gui26.text(g, this.font, set.name(), 0, 0, counts ? colour : dim(colour), true);
            com.li64.tide.client.Gui26.text(g, this.font, status, this.font.width(set.name()) + 6, 0,
                    counts ? 0xFFFFE08A : n >= half ? 0xFFFF8A7A : 0xFF8C97A0, true);
            for (int i = 0, ly = 12; i < text.size(); i++, ly += 10)
                com.li64.tide.client.Gui26.text(g, this.font, text.get(i), 0, ly, colours.get(i), true);
            g.pose().popMatrix();
            if (mouseX >= x && mouseX < x + w && mouseY >= cy && mouseY < cy + h) hover = rigTooltip(p, counts);
            cy += h + 2;
        }
        if (rigs.size() > MAX_SHOWN) {
            g.pose().pushMatrix();
            g.pose().translate(x + 5, cy + 1);
            g.pose().scale(s, s);
            com.li64.tide.client.Gui26.text(g, this.font, "+" + (rigs.size() - MAX_SHOWN) + " more", 0, 0, 0xFF8C97A0, true);
            g.pose().popMatrix();
        }
        if (hover != null) g.setTooltipForNextFrame(this.font, hover, java.util.Optional.empty(), mouseX, mouseY);
    }

    /** Hovering a rig's card or header: both bonuses, what could still go on, and whether it counts. */
    private static java.util.List<Component> rigTooltip(com.li64.tide.data.rods.RigSets.Progress p, boolean counts) {
        var set = p.set();
        int n = p.have(), half = com.li64.tide.data.rods.RigSets.HALF, full = com.li64.tide.data.rods.RigSets.PIECES;
        var colour = net.minecraft.network.chat.TextColor.fromRgb(com.li64.tide.data.rods.RigSets.color(set));
        java.util.List<Component> tip = new java.util.ArrayList<>();
        tip.add(Component.literal(set.name()).withStyle(st -> st.withColor(colour))
                .append(Component.literal("  " + Math.min(n, full) + " of " + full + " pieces").withStyle(net.minecraft.ChatFormatting.GRAY)));
        tip.add(Component.literal("Tier 1 (2+): " + set.twoText()).withStyle(counts && n >= half ? net.minecraft.ChatFormatting.WHITE : net.minecraft.ChatFormatting.GRAY));
        tip.add(Component.literal("Tier 2 (3+): " + set.bonusText()).withStyle(counts && n >= full ? net.minecraft.ChatFormatting.WHITE : net.minecraft.ChatFormatting.GRAY));
        if (n < full && !p.canAdd().isEmpty()) tip.add(Component.literal("Add a " + String.join(" or ", p.canAdd())).withStyle(st -> st.withColor(colour)));
        if (!counts && n >= half) tip.add(Component.literal("Not counted: a rod carries two rigs, the two with the most pieces").withStyle(net.minecraft.ChatFormatting.RED));
        return tip;
    }

    private static int dim(int argb) {
        return 0xFF000000 | ((argb >> 16 & 0xFF) * 3 / 5) << 16 | ((argb >> 8 & 0xFF) * 3 / 5) << 8 | (argb & 0xFF) * 3 / 5;
    }

    @Override
    protected void extractErrorIcon(@NotNull GuiGraphicsExtractor graphics, int x, int y) {}
}
