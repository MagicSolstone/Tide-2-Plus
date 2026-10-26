package com.li64.tide.client.gui.screens.journal;

import com.li64.tide.client.ClientTackle;
import com.li64.tide.client.Gui26;
import com.li64.tide.data.rods.RodEffects;
import com.li64.tide.data.tackle.TackleLog;
import com.li64.tide.util.BaitUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * The journal's tackle section: every rod, line, hook, bobber, lure, bait and piece of gear, grouped by kind.
 * Ones the player hasn't held yet are shadows, like unknown fish; clicking a found one opens its page (what it
 * does). The list comes from the items themselves, so new tackle shows up without touching this.
 */
final class TackleCard {
    private static final int INK = 0xFF000000 | FishingJournal.TEXT_COLOR;
    private static final int DARK = 0xFF6B4A33;
    private static final int CELL = 22;
    private static final int PER_ROW = 14;
    private static final int AREA = 186;
    private static final int HEADER = 12;

    /** A line of the grid: a category heading, or a row of items. */
    private record Row(TackleLog.Category heading, List<Item> items) {}

    private final List<List<Row>> pages = new ArrayList<>();
    private final int total;
    int page = 0;
    ItemStack selected = null;

    TackleCard() {
        Map<TackleLog.Category, List<Item>> byKind = new EnumMap<>(TackleLog.Category.class);
        for (Item item : BuiltInRegistries.ITEM) {
            TackleLog.Category c = TackleLog.category(item);
            if (c != null) byKind.computeIfAbsent(c, k -> new ArrayList<>()).add(item);
        }
        this.total = byKind.values().stream().mapToInt(List::size).sum();

        List<Row> current = new ArrayList<>();
        int used = 0;
        for (var e : byKind.entrySet()) {
            List<Item> items = e.getValue();
            int rows = (items.size() + PER_ROW - 1) / PER_ROW;
            if (used + HEADER + CELL > AREA) { pages.add(current); current = new ArrayList<>(); used = 0; }   // keep a heading with its first row
            current.add(new Row(e.getKey(), List.of()));
            used += HEADER;
            for (int r = 0; r < rows; r++) {
                if (used + CELL > AREA) { pages.add(current); current = new ArrayList<>(); used = 0; }
                current.add(new Row(null, items.subList(r * PER_ROW, Math.min(items.size(), (r + 1) * PER_ROW))));
                used += CELL;
            }
            used += 4;
        }
        if (!current.isEmpty()) pages.add(current);
    }

    /** Rigs come after the tackle grid, four to a spread (two on each page). */
    private static final int RIGS_PER_SPREAD = 4;

    int pageCount() {
        return pages.size() + rigPages();
    }

    private static int rigPages() {
        return (com.li64.tide.data.rods.RigSets.SETS.size() + RIGS_PER_SPREAD - 1) / RIGS_PER_SPREAD;
    }

    private static boolean found(Item item) {
        return ClientTackle.found.contains(BuiltInRegistries.ITEM.getKey(item).getPath());
    }

    /** Draws the grid or the selected item's page. Returns true when a click changed what's shown. */
    boolean draw(GuiGraphicsExtractor g, Font font, int tlX, int tlY, int mouseX, int mouseY, boolean clicked) {
        if (selected != null) {
            drawItemPage(g, font, tlX, tlY, mouseX, mouseY);
            return false;
        }
        if (page >= pages.size()) return drawRigs(g, font, tlX, tlY, mouseX, mouseY, clicked);
        long have = BuiltInRegistries.ITEM.stream().filter(i -> TackleLog.category(i) != null && found(i)).count();
        Gui26.text(g, font, "Tackle found: " + have + " / " + total, tlX + 44, tlY + 228, DARK, false);
        if (pages.size() > 1) {
            String p = "Page " + (page + 1) + " of " + pages.size();
            Gui26.text(g, font, p, tlX + 356 - font.width(p), tlY + 228, INK, false);
        }

        boolean changed = false;
        int y = tlY + 36;
        for (Row row : pages.get(Math.min(page, pages.size() - 1))) {
            if (row.heading() != null) {
                Gui26.text(g, font, row.heading().label, tlX + 44, y + 2, DARK, false);
                g.blitSprite(RenderPipelines.GUI_TEXTURED, FishingJournal.LINE_BOTTOM, tlX + 44 + font.width(row.heading().label) + 4, y + 6,
                        Math.max(0, 300 - font.width(row.heading().label)), 2);
                y += HEADER;
                continue;
            }
            int x = tlX + 44;
            for (Item item : row.items()) {
                ItemStack stack = new ItemStack(item);
                boolean has = found(item);
                boolean hover = mouseX > x - 3 && mouseY > y - 3 && mouseX < x + 19 && mouseY < y + 19;
                FishingJournal.renderItemSilhouette(g, stack, x + 1, y + 1, FishingJournal.SHADOW_COLOR);
                if (hover) {
                    for (int[] o : new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}})
                        FishingJournal.renderItemSilhouette(g, stack, x + o[0], y + o[1], 0xFFFFFFFF);
                    g.setTooltipForNextFrame(font, has ? stack.getHoverName() : Component.literal("Not found yet"), mouseX, mouseY);
                    if (has && clicked) { selected = stack; changed = true; }
                }
                if (has) g.item(stack, x, y);
                else FishingJournal.renderItemSilhouette(g, stack, x, y, 0xFF000000);
                x += CELL;
            }
            y += CELL;
        }
        return changed;
    }

    /**
     * The rigs: each one's name and where it works, its pieces (blacked out until found; click a found one to
     * open its page), and what two and three pieces give.
     */
    private boolean drawRigs(GuiGraphicsExtractor g, Font font, int tlX, int tlY, int mouseX, int mouseY, boolean clicked) {
        var sets = com.li64.tide.data.rods.RigSets.SETS;
        int spread = page - pages.size();
        Gui26.text(g, font, "Rigs: 2 pieces on one rod give a small bonus, 3 or more the full one", tlX + 44, tlY + 228, DARK, false);
        String p = "Page " + (page + 1) + " of " + pageCount();
        Gui26.text(g, font, p, tlX + 356 - font.width(p), tlY + 228, INK, false);
        boolean changed = false;
        float s = 0.75f;
        for (int k = 0; k < RIGS_PER_SPREAD; k++) {
            int idx = spread * RIGS_PER_SPREAD + k;
            if (idx >= sets.size()) break;
            var set = sets.get(idx);
            int x = k < 2 ? tlX + 40 : tlX + 215, y = tlY + 30 + (k % 2) * 98;
            Gui26.text(g, font, set.name(), x, y, DARK, false);
            g.pose().pushMatrix();
            g.pose().translate(x + font.width(set.name()) + 4, y + 2);
            g.pose().scale(s, s);
            Gui26.text(g, font, set.when().text, 0, 0, INK, false);
            g.pose().popMatrix();
            // its pieces, small, in a row or two
            int ix = x, iy = y + 11, n = 0;
            for (String id : set.members().stream().sorted(java.util.Comparator.comparing(m -> {
                    var c = TackleLog.category(BuiltInRegistries.ITEM.getValue(com.li64.tide.Tide.resource(m)));
                    return (c == null ? 9 : c.ordinal()) + m; })).toList()) {
                Item item = BuiltInRegistries.ITEM.getValue(com.li64.tide.Tide.resource(id));
                ItemStack stack = new ItemStack(item);
                int cx = ix + (n % 11) * 13, cy = iy + (n / 11) * 13;
                g.pose().pushMatrix();
                g.pose().translate(cx, cy);
                g.pose().scale(s, s);
                if (found(item)) g.item(stack, 0, 0);
                else FishingJournal.renderItemSilhouette(g, stack, 0, 0, 0xFF000000);
                g.pose().popMatrix();
                if (mouseX >= cx && mouseX < cx + 12 && mouseY >= cy && mouseY < cy + 12) {
                    g.setTooltipForNextFrame(font, found(item) ? stack.getHoverName() : Component.literal("Not found yet"), mouseX, mouseY);
                    if (found(item) && clicked) { selected = stack; changed = true; }
                }
                n++;
            }
            int ty = iy + ((n + 10) / 11) * 13 + 2;
            // what two and three pieces give, wrapped small
            g.pose().pushMatrix();
            g.pose().translate(x, ty);
            g.pose().scale(s, s);
            int ly = 0;
            for (String[] line : new String[][]{{"Tier 1 (2 pieces): ", set.twoText()}, {"Tier 2 (3 or more): ", set.bonusText()}})
                for (FormattedCharSequence part : font.split(Component.literal(line[0] + line[1]), (int) (150 / s))) {
                    Gui26.text(g, font, part, 0, ly, line[0].startsWith("Tier 1") ? INK : 0xFF8A3FA8, false);
                    ly += 10;
                }
            g.pose().popMatrix();
        }
        return changed;
    }

    private final java.util.Set<String> asked = new java.util.HashSet<>();

    private void drawItemPage(GuiGraphicsExtractor g, Font font, int tlX, int tlY, int mouseX, int mouseY) {
        ItemStack stack = selected;
        TackleLog.Category kind = TackleLog.category(stack.getItem());
        String id = BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();

        // left page: name, the item, and how it's made
        int x = tlX + 40, y = tlY + 30;
        Gui26.text(g, font, stack.getHoverName().getString(), x, y, DARK, false);
        y += 11;
        Gui26.text(g, font, kind == null ? "" : kind.label.replaceAll("s$", ""), x, y, INK, false);
        y += 14;
        g.pose().pushMatrix();
        g.pose().translate(x, y);
        g.pose().scale(3f, 3f);
        g.item(stack, 0, 0);
        g.pose().popMatrix();

        com.li64.tide.network.messages.RecipeViewMsg recipe = ClientTackle.recipes.get(id);
        if (recipe == null && asked.add(id)) com.li64.tide.Tide.NETWORK.sendToServer(new com.li64.tide.network.messages.RecipeRequestMsg(id));
        int gx = x + 62, gy = y - 4;
        if (recipe == null) Gui26.text(g, font, "...", gx, gy + 20, INK, false);
        else if (recipe.kind().equals("found")) {
            int ty = gy + 4;
            for (FormattedCharSequence line : font.split(Component.literal(recipe.note()), 88)) { Gui26.text(g, font, line, gx, ty, INK, false); ty += 10; }
        } else {
            String label = !recipe.note().isEmpty() ? "Angling Table"
                    : switch (recipe.kind()) { case "smithing" -> "Smithing table"; case "shapeless" -> "Any arrangement"; default -> "Crafting table"; };
            Gui26.text(g, font, label, gx, gy - 6, DARK, false);
            if (recipe.kind().equals("smithing")) {      // template + base + addition, in a row
                for (int i = 0; i < recipe.slots().size(); i++) slot(g, font, recipe.slots().get(i), gx + i * 20, gy + 6, mouseX, mouseY);
                Gui26.text(g, font, "template, rod, upgrade", gx, gy + 30, INK, false);
            } else {
                for (int r = 0; r < recipe.height(); r++)
                    for (int c = 0; c < recipe.width(); c++)
                        slot(g, font, recipe.slots().get(r * recipe.width() + c), gx + c * 20, gy + 6 + r * 20, mouseX, mouseY);
            }
        }
        y += 66;
        for (FormattedCharSequence line : font.split(Component.literal(howToUse(kind)), 150)) {
            Gui26.text(g, font, line, x, y, INK, false);
            y += 10;
        }

        // right page: what it does, in the same words as its tooltip; smaller text if it's a long list
        x = tlX + 215;
        y = tlY + 30;
        Gui26.text(g, font, "What it does", x, y, DARK, false);
        y += 14;
        List<Component> lines = new ArrayList<>(kind == TackleLog.Category.RODS ? RodEffects.describeRod(stack) : RodEffects.describeItem(stack));
        lines.removeIf(c -> c.getString().startsWith("Part of: "));        // the full rig lines below say it
        lines.addAll(com.li64.tide.data.rods.RigSets.describe(stack));
        if (kind == TackleLog.Category.BAIT)
            BaitUtils.getDescriptionLines(stack).stream().filter(c -> !c.getString().isBlank() && !c.getString().endsWith(":")).forEach(lines::add);
        if (lines.isEmpty()) lines.add(Component.literal("No special perks: good for looks and the basics."));
        int room = tlY + 226 - y;
        float scale = 1f;
        List<FormattedCharSequence> wrapped = new ArrayList<>();
        for (; scale >= 0.6f; scale -= 0.1f) {
            wrapped.clear();
            for (Component c : lines) wrapped.addAll(font.split(Component.literal("- " + c.getString()), (int) (150 / scale)));
            if (wrapped.size() * 10 * scale + lines.size() * 2 * scale <= room) break;
        }
        g.pose().pushMatrix();
        g.pose().translate(x, y);
        g.pose().scale(scale, scale);
        int ly = 0;
        for (Component c : lines) {
            // place-, time- and weather-bound perks in purple, like the tooltip
            var tc = c.getStyle().getColor();
            int color = tc != null && tc.getValue() == 0xFF55FF ? 0xFF8A3FA8 : INK;
            for (FormattedCharSequence line : font.split(Component.literal("- " + c.getString()), (int) (150 / scale))) {
                Gui26.text(g, font, line, 0, ly, color, false);
                ly += 10;
            }
            ly += 2;
        }
        g.pose().popMatrix();
    }

    /** One recipe slot: a tan square with the item (cycling through a tag's items), its name on hover. */
    private static void slot(GuiGraphicsExtractor g, Font font, List<String> options, int x, int y, int mouseX, int mouseY) {
        g.fill(x, y, x + 18, y + 18, 0xFFD7B594);
        g.fill(x + 1, y + 1, x + 17, y + 17, 0xFFE8D2B4);
        if (options.isEmpty()) return;
        String id = options.get((int) ((System.currentTimeMillis() / 1000) % options.size()));
        ItemStack stack = new ItemStack(BuiltInRegistries.ITEM.getValue(net.minecraft.resources.Identifier.parse(id)));
        g.item(stack, x + 1, y + 1);
        if (mouseX >= x && mouseX < x + 18 && mouseY >= y && mouseY < y + 18)
            g.setTooltipForNextFrame(font, options.size() > 1 ? Component.literal(stack.getHoverName().getString() + " (or similar)") : stack.getHoverName(), mouseX, mouseY);
    }

    private static String howToUse(TackleLog.Category kind) {
        if (kind == null) return "";
        return switch (kind) {
            case RODS -> "Attach a line, hook, bobber and lure at an angling table, or a tackle box (sneak + right-click).";
            case LINES, HOOKS, BOBBERS, LURES -> "Put it on a rod at an angling table, or a tackle box (sneak + right-click).";
            case BAIT -> "Right-click a rod with it to load its bait slots. One is used up per catch.";
            case GEAR -> "Wear it or carry it while you fish.";
        };
    }
}
