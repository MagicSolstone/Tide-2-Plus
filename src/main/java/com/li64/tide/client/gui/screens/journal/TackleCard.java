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

    int pageCount() {
        return pages.size();
    }

    private static boolean found(Item item) {
        return ClientTackle.found.contains(BuiltInRegistries.ITEM.getKey(item).getPath());
    }

    /** Draws the grid or the selected item's page. Returns true when a click changed what's shown. */
    boolean draw(GuiGraphicsExtractor g, Font font, int tlX, int tlY, int mouseX, int mouseY, boolean clicked) {
        if (selected != null) {
            drawItemPage(g, font, tlX, tlY);
            return false;
        }
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

    private void drawItemPage(GuiGraphicsExtractor g, Font font, int tlX, int tlY) {
        ItemStack stack = selected;
        TackleLog.Category kind = TackleLog.category(stack.getItem());

        // left page: the item, big, with its name
        int x = tlX + 40, y = tlY + 30;
        Gui26.text(g, font, stack.getHoverName().getString(), x, y, DARK, false);
        y += 11;
        Gui26.text(g, font, kind == null ? "" : kind.label.replaceAll("s$", ""), x, y, INK, false);
        g.pose().pushMatrix();
        g.pose().translate(x + 35, y + 25);
        g.pose().scale(5f, 5f);
        g.item(stack, 0, 0);
        g.pose().popMatrix();
        y += 120;
        for (FormattedCharSequence line : font.split(Component.literal(howToUse(kind)), 150)) {
            Gui26.text(g, font, line, x, y, INK, false);
            y += 10;
        }

        // right page: what it does, in the same words as its tooltip
        x = tlX + 215;
        y = tlY + 30;
        Gui26.text(g, font, "What it does", x, y, DARK, false);
        y += 14;
        List<Component> lines = new ArrayList<>(kind == TackleLog.Category.RODS ? RodEffects.describeRod(stack) : RodEffects.describeItem(stack));
        if (kind == TackleLog.Category.BAIT)
            BaitUtils.getDescriptionLines(stack).stream().filter(c -> !c.getString().isBlank() && !c.getString().endsWith(":")).forEach(lines::add);
        if (lines.isEmpty()) lines.add(Component.literal("No special perks: good for looks and the basics."));
        for (Component c : lines) {
            for (FormattedCharSequence line : font.split(Component.literal("- " + c.getString()), 150)) {
                if (y > tlY + 220) break;
                Gui26.text(g, font, line, x, y, INK, false);
                y += 10;
            }
            y += 2;
        }
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
