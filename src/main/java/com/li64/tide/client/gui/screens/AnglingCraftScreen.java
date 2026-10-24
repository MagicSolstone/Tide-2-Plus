package com.li64.tide.client.gui.screens;

import com.li64.tide.Tide;
import com.li64.tide.client.Gui26;
import com.li64.tide.client.gui.menus.AnglingCraftMenu;
import com.li64.tide.data.rods.RigSets;
import com.li64.tide.data.tackle.TackleLog;
import com.li64.tide.data.tackle.TackleRecipes;
import com.li64.tide.network.messages.BenchTabMsg;
import com.li64.tide.network.messages.TackleCraftMsg;
import com.li64.tide.network.messages.TackleRecipesMsg;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The Angling Table's Craft tab: every piece of fishing gear the table can make, filtered by kind or by rig, with
 * what it does, what it takes (what you have of each), and a Craft button. Items you can make now are lit up.
 */
public class AnglingCraftScreen extends AbstractContainerScreen<AnglingCraftMenu> {
    public static final int W = 256, H = 222;
    private static final int GRID_X = 8, GRID_Y = 38, COLS = 6, ROWS = 5, CELL = 18;
    private static final int INFO_X = 128, INFO_W = 120;
    private static final int PANEL = 0xFFC6C6C6, LIGHT = 0xFFFFFFFF, SHADE = 0xFF555555, EDGE = 0xFF000000;
    private static final int SLOT_DARK = 0xFF373737, SLOT_FILL = 0xFF8B8B8B, SLOT_LIGHT = 0xFFFFFFFF, INK = 0xFF404040;

    /** The filters along the top: All, Sets, then one per kind. */
    private static final String[] FILTER_ICONS = {"tide:angling_table", "tide:tackle_box", "tide:bamboo_fishing_rod", "tide:fishing_line",
            "tide:red_bobber", "tide:fishing_hook", "tide:spinner_lure", "tide:bait", "tide:fishing_journal"};
    private static final String[] FILTER_NAMES = {"Everything", "Rigs (sets)", "Rods", "Lines", "Bobbers", "Hooks", "Lures", "Bait", "Gear"};

    /** One thing in the grid: a recipe, or (in rig view) a rig piece that's found rather than made. */
    private record Cell(TackleRecipesMsg.Row row, Item item) {}

    /** Ingredients the same, grouped: which ingredient positions they fill and the items each accepts. */
    private record Need(List<Integer> positions, List<Item> options) {}

    private static int filter = 0, setIndex = 0;
    private static boolean onlyMakeable = false;
    private static String selectedId = null;
    private final Map<String, Map<Integer, Item>> prefer = new HashMap<>();
    private List<Cell> cells = List.of();
    private int scroll = 0;
    private Button craftButton;
    private Map<Item, Integer> stock = Map.of();
    private int stockTick = -1;

    public AnglingCraftScreen(AnglingCraftMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, W, H);
        this.inventoryLabelX = AnglingCraftMenu.INV_X;
        this.inventoryLabelY = AnglingCraftMenu.INV_Y - 11;
        this.titleLabelX = 8;
        this.titleLabelY = 6;
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(Button.builder(Component.literal("Assemble rod"), b -> Tide.NETWORK.sendToServer(new BenchTabMsg(false)))
                .bounds(this.leftPos + W - 80, this.topPos - 17, 80, 16).build());
        this.craftButton = this.addRenderableWidget(Button.builder(Component.literal("Craft"), b -> craft(false))
                .bounds(this.leftPos + INFO_X + INFO_W - 46, this.topPos + 115, 46, 16).build());
        rebuild();
        if (Tide.CI) ciLog();
    }

    /** CI: where things are on screen (in window pixels), so the smoke test can click them. */
    private void ciLog() {
        double k = this.minecraft.getWindow().getGuiScale();
        Tide.LOG.info("TIDE-CI bench {} recipes, filter {}, cells {}", TackleRecipesMsg.CLIENT.size(), filter, cells.size());
        Tide.LOG.info("TIDE-CI bench px hooks {} {} craft {} {} assemble {} {}",
                (int) ((this.leftPos + 8 + 5 * 20 + 9) * k), (int) ((this.topPos + 25) * k),
                (int) ((this.leftPos + INFO_X + INFO_W - 23) * k), (int) ((this.topPos + 123) * k),
                (int) ((this.leftPos + W - 40) * k), (int) ((this.topPos - 9) * k));
        for (int i = 0; i < cells.size() && i < COLS * ROWS; i++)
            if (BuiltInRegistries.ITEM.getKey(cells.get(i).item).getPath().equals("diamond_hook"))
                Tide.LOG.info("TIDE-CI bench px diamond_hook {} {}", (int) ((this.leftPos + GRID_X + (i % COLS) * CELL + 9) * k),
                        (int) ((this.topPos + gridTop() + (i / COLS) * CELL + 9) * k));
    }

    // ------------------------------------------------------------------ data

    private static Item item(String id) {
        Identifier key = Identifier.tryParse(id);
        return key == null ? Items.AIR : BuiltInRegistries.ITEM.getOptional(key).orElse(Items.AIR);
    }

    /** What the player has, by item (main inventory and offhand), refreshed each tick. */
    private Map<Item, Integer> stock() {
        if (stockTick == this.minecraft.player.tickCount) return stock;
        stockTick = this.minecraft.player.tickCount;
        Map<Item, Integer> out = new HashMap<>();
        Inventory inv = this.minecraft.player.getInventory();
        for (int i = 0; i < 36; i++) add(out, inv.getItem(i));
        add(out, inv.getItem(40));
        return stock = out;
    }

    private static void add(Map<Item, Integer> out, ItemStack s) {
        if (!s.isEmpty()) out.merge(s.getItem(), s.getCount(), Integer::sum);
    }

    /** Whether the player has enough for one, taking each ingredient from what's left (as the server does). */
    private boolean canMake(TackleRecipesMsg.Row row) {
        Map<Item, Integer> left = new HashMap<>(stock());
        Map<Integer, Item> pref = prefer.getOrDefault(row.id(), Map.of());
        for (int i = 0; i < row.ingredients().size(); i++) {
            Item want = pref.get(i), pick = null;
            if (want != null && left.getOrDefault(want, 0) > 0) pick = want;
            else for (String o : row.ingredients().get(i)) {
                Item it = item(o);
                if (left.getOrDefault(it, 0) > 0) { pick = it; break; }
            }
            if (pick == null) return false;
            left.merge(pick, -1, Integer::sum);
        }
        return true;
    }

    private List<Need> needs(TackleRecipesMsg.Row row) {
        Map<List<String>, List<Integer>> grouped = new LinkedHashMap<>();
        for (int i = 0; i < row.ingredients().size(); i++) grouped.computeIfAbsent(row.ingredients().get(i), k -> new ArrayList<>()).add(i);
        List<Need> out = new ArrayList<>();
        grouped.forEach((opts, pos) -> out.add(new Need(pos, opts.stream().map(AnglingCraftScreen::item).filter(i -> i != Items.AIR).toList())));
        return out;
    }

    private void rebuild() {
        List<Cell> out = new ArrayList<>();
        List<TackleRecipesMsg.Row> rows = TackleRecipesMsg.CLIENT;
        if (filter == 1) {
            RigSets.RigSet set = RigSets.SETS.get(Math.floorMod(setIndex, RigSets.SETS.size()));
            for (String m : set.members()) {
                Item it = item(Tide.MOD_ID + ":" + m);
                if (it == Items.AIR) continue;
                TackleRecipesMsg.Row row = rows.stream().filter(r -> item(r.result()) == it).findFirst().orElse(null);
                out.add(new Cell(row, it));
            }
            out.sort(Comparator.comparing((Cell c) -> c.row == null).thenComparingInt(c -> c.row == null ? 0 : c.row.kind()));
        } else {
            for (TackleRecipesMsg.Row r : rows)
                if (filter == 0 || r.kind() == filter - 2) out.add(new Cell(r, item(r.result())));
        }
        if (onlyMakeable) out.removeIf(c -> c.row == null || !canMake(c.row));
        cells = out;
        int maxScroll = Math.max(0, (cells.size() + COLS - 1) / COLS - rows());
        scroll = Math.min(scroll, maxScroll);
        if (selected() == null && !cells.isEmpty()) selectedId = key(cells.get(0));
    }

    private int rows() {
        return filter == 1 ? ROWS - 1 : ROWS;
    }

    private int gridTop() {
        return filter == 1 ? GRID_Y + CELL : GRID_Y;
    }

    private static String key(Cell c) {
        return c.row != null ? c.row.id() : "found:" + BuiltInRegistries.ITEM.getKey(c.item);
    }

    private Cell selected() {
        for (Cell c : cells) if (key(c).equals(selectedId)) return c;
        return null;
    }

    private void craft(boolean all) {
        Cell c = selected();
        if (c == null || c.row == null) return;
        Map<Integer, Item> pref = prefer.getOrDefault(c.row.id(), Map.of());
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < c.row.ingredients().size(); i++) {
            Item it = pref.get(i);
            ids.add(it == null ? "" : BuiltInRegistries.ITEM.getKey(it).toString());
        }
        Tide.NETWORK.sendToServer(new TackleCraftMsg(c.row.id(), ids, all || this.minecraft.hasShiftDown()));
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (onlyMakeable || cells.isEmpty()) rebuild();
        Cell c = selected();
        this.craftButton.active = c != null && c.row != null && canMake(c.row);
        this.craftButton.visible = c != null && c.row != null;
    }

    // ------------------------------------------------------------------ drawing

    private void panel(GuiGraphicsExtractor g, int x, int y, int w, int h) {
        g.fill(x, y + 1, x + w, y + h - 1, EDGE);
        g.fill(x + 1, y, x + w - 1, y + h, EDGE);
        g.fill(x + 1, y + 1, x + w - 1, y + h - 1, PANEL);
        g.fill(x + 1, y + 1, x + w - 2, y + 3, LIGHT);
        g.fill(x + 1, y + 1, x + 3, y + h - 2, LIGHT);
        g.fill(x + 3, y + h - 3, x + w - 1, y + h - 1, SHADE);
        g.fill(x + w - 3, y + 3, x + w - 1, y + h - 1, SHADE);
    }

    private static void slotBox(GuiGraphicsExtractor g, int x, int y, int fill) {
        g.fill(x, y, x + 18, y + 18, SLOT_LIGHT);
        g.fill(x, y, x + 17, y + 17, SLOT_DARK);
        g.fill(x + 1, y + 1, x + 17, y + 17, fill);
    }

    @Override
    public void extractBackground(@NotNull GuiGraphicsExtractor g, int mouseX, int mouseY, float a) {
        super.extractBackground(g, mouseX, mouseY, a);
        int x = this.leftPos, y = this.topPos;
        panel(g, x, y, W, H);
        // the Craft tab, joined to the panel
        g.fill(x + 4, y - 15, x + 60, y + 2, EDGE);
        g.fill(x + 5, y - 14, x + 59, y + 3, PANEL);
        g.fill(x + 5, y - 14, x + 58, y - 12, LIGHT);
        Gui26.text(g, this.font, "Craft", x + 24, y - 10, INK, false);
        g.item(new ItemStack(item("tide:angling_table")), x + 4 + 2, y - 14 + 0);

        // filters
        for (int i = 0; i < FILTER_ICONS.length; i++) {
            int fx = x + 8 + i * 20, fy = y + 16;
            slotBox(g, fx, fy, i == filter ? 0xFF6E9FC8 : SLOT_FILL);
            g.item(new ItemStack(item(FILTER_ICONS[i])), fx + 1, fy + 1);
        }
        // "only what I can make"
        int tx = x + 8 + FILTER_ICONS.length * 20 + 4, ty = y + 20;
        g.fill(tx, ty, tx + 10, ty + 10, SLOT_DARK);
        g.fill(tx + 1, ty + 1, tx + 9, ty + 9, onlyMakeable ? 0xFF5DB85D : 0xFFE0E0E0);
        if (onlyMakeable) Gui26.text(g, this.font, "✔", tx + 2, ty + 1, 0xFF14401A, false);
        Gui26.text(g, this.font, "Can make", tx + 13, ty + 1, INK, false);

        // rig picker
        if (filter == 1) {
            RigSets.RigSet set = RigSets.SETS.get(Math.floorMod(setIndex, RigSets.SETS.size()));
            g.fill(x + GRID_X, y + GRID_Y, x + GRID_X + COLS * CELL, y + GRID_Y + 15, 0xFF2A3440);
            Gui26.text(g, this.font, "<", x + GRID_X + 4, y + GRID_Y + 4, 0xFFFFFFFF, false);
            Gui26.text(g, this.font, ">", x + GRID_X + COLS * CELL - 9, y + GRID_Y + 4, 0xFFFFFFFF, false);
            String name = set.name();
            Gui26.text(g, this.font, name, x + GRID_X + (COLS * CELL - this.font.width(name)) / 2, y + GRID_Y + 4, 0xFFFFD27F, false);
        }

        // the grid
        int top = gridTop();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < COLS; c++) {
                int i = (scroll + r) * COLS + c, cx = x + GRID_X + c * CELL, cy = y + top + r * CELL;
                if (i >= cells.size()) { slotBox(g, cx, cy, SLOT_FILL); continue; }
                Cell cell = cells.get(i);
                boolean make = cell.row != null && canMake(cell.row);
                boolean sel = key(cell).equals(selectedId);
                slotBox(g, cx, cy, sel ? 0xFFE8C46A : make ? 0xFF8FC48A : cell.row == null ? 0xFF6E6E6E : SLOT_FILL);
                g.item(new ItemStack(cell.item, cell.row == null ? 1 : cell.row.count()), cx + 1, cy + 1);
                if (cell.row != null && cell.row.count() > 1) g.itemDecorations(this.font, new ItemStack(cell.item, cell.row.count()), cx + 1, cy + 1);
            }
        // scrollbar
        int total = (cells.size() + COLS - 1) / COLS, sbX = x + GRID_X + COLS * CELL + 2, sbH = rows() * CELL;
        g.fill(sbX, y + top, sbX + 6, y + top + sbH, SLOT_DARK);
        if (total > rows()) {
            int knob = Math.max(10, sbH * rows() / total), ky = y + top + (sbH - knob) * scroll / Math.max(1, total - rows());
            g.fill(sbX + 1, ky, sbX + 5, ky + knob, 0xFFC6C6C6);
        } else g.fill(sbX + 1, y + top, sbX + 5, y + top + sbH, 0xFF8B8B8B);

        // details
        int ix = x + INFO_X, iy = y + GRID_Y;
        g.fill(ix - 2, iy - 2, ix + INFO_W, y + 132, 0xFFB4B4B4);
        Cell cell = selected();
        if (cell != null) drawInfo(g, cell, ix, iy, mouseX, mouseY);
        else Gui26.text(g, this.font, cells.isEmpty() ? "Nothing to show" : "", ix + 2, iy + 2, INK, false);

        // inventory slots
        for (Slot s : this.menu.slots) slotBox(g, x + s.x - 1, y + s.y - 1, SLOT_FILL);
    }

    private void drawInfo(GuiGraphicsExtractor g, Cell cell, int ix, int iy, int mouseX, int mouseY) {
        ItemStack stack = new ItemStack(cell.item);
        g.item(stack, ix, iy);
        List<FormattedCharSequence> name = this.font.split(stack.getHoverName(), INFO_W - 22);
        Gui26.text(g, this.font, name.get(0), ix + 19, iy + 4, 0xFF202020, false);
        // what it does: its tooltip without the name, small
        List<Component> lines = new ArrayList<>(Screen.getTooltipFromItem(this.minecraft, stack));
        if (!lines.isEmpty()) lines.remove(0);
        lines.removeIf(l -> l.getString().isBlank());
        List<FormattedCharSequence> small = new ArrayList<>();
        float s = 0.75f;
        for (Component l : lines) small.addAll(this.font.split(l, (int) ((INFO_W - 4) / s)));
        int textTop = iy + 19, textBottom = iy + 46;
        int maxLines = (int) ((textBottom - textTop) / (9 * s));
        g.pose().pushMatrix();
        g.pose().translate(ix + 1, textTop);
        g.pose().scale(s, s);
        for (int i = 0; i < Math.min(maxLines, small.size()); i++)
            Gui26.text(g, this.font, small.get(i), 0, i * 9, i == maxLines - 1 && small.size() > maxLines ? 0xFF606060 : 0xFF303030, false);
        g.pose().popMatrix();
        if (small.size() > maxLines) {
            Gui26.text(g, this.font, "…", ix + INFO_W - 10, textBottom - 6, INK, false);
            if (mouseX >= ix && mouseX < ix + INFO_W && mouseY >= textTop && mouseY < textBottom) g.setTooltipForNextFrame(this.font, stack, mouseX, mouseY);
        }

        int ny = iy + 48;
        if (cell.row == null) {
            String where = TackleLog.FOUND.getOrDefault(BuiltInRegistries.ITEM.getKey(cell.item).getPath(), "Found while fishing or exploring.");
            int ly = ny;
            for (FormattedCharSequence l : this.font.split(Component.literal("Not crafted. " + where), INFO_W - 4)) {
                Gui26.text(g, this.font, l, ix + 1, ly, 0xFF5A3A10, false);
                ly += 9;
            }
            return;
        }
        Gui26.text(g, this.font, cell.row.smithing() ? "Upgrade:" : "Needs:", ix + 1, ny, INK, false);
        List<Need> needs = needs(cell.row);
        Map<Integer, Item> pref = prefer.getOrDefault(cell.row.id(), Map.of());
        Map<Item, Integer> have = stock();
        int nx = ix, ry = ny + 9;
        for (Need n : needs) {
            if (nx + 20 > ix + INFO_W) { nx = ix; ry += 20; }
            Item shown = shownOption(n, pref, have);
            int owned = n.options.stream().mapToInt(o -> have.getOrDefault(o, 0)).sum();
            int need = n.positions.size();
            slotBox(g, nx, ry, owned >= need ? 0xFF8FC48A : 0xFFC88A8A);
            if (shown != null) g.item(new ItemStack(shown), nx + 1, ry + 1);
            String count = Math.min(owned, 99) + "/" + need;
            g.pose().pushMatrix();
            g.pose().translate(nx + 18 - this.font.width(count) * 0.6f, ry + 13);
            g.pose().scale(0.6f, 0.6f);
            Gui26.text(g, this.font, count, 0, 0, owned >= need ? 0xFFFFFFFF : 0xFFFFB0B0, true);
            g.pose().popMatrix();
            if (mouseX >= nx && mouseX < nx + 18 && mouseY >= ry && mouseY < ry + 18 && shown != null) {
                List<Component> tip = new ArrayList<>();
                tip.add(new ItemStack(shown).getHoverName());
                if (n.options.size() > 1) {
                    long kinds = n.options.stream().filter(o -> have.getOrDefault(o, 0) > 0).count();
                    tip.add(Component.literal("Any of " + n.options.size() + " kinds").withStyle(net.minecraft.ChatFormatting.GRAY));
                    if (kinds > 1) tip.add(Component.literal("Click to choose which one to use").withStyle(net.minecraft.ChatFormatting.YELLOW));
                }
                g.setTooltipForNextFrame(this.font, tip, Optional.empty(), mouseX, mouseY);
            }
            nx += 20;
        }
    }

    /** The item an ingredient shows: the chosen one, else one the player has, else each option in turn. */
    private Item shownOption(Need n, Map<Integer, Item> pref, Map<Item, Integer> have) {
        if (n.options.isEmpty()) return null;
        Item chosen = pref.get(n.positions.get(0));
        if (chosen != null && n.options.contains(chosen)) return chosen;
        for (Item o : n.options) if (have.getOrDefault(o, 0) > 0) return o;
        return n.options.get((int) ((System.currentTimeMillis() / 1000) % n.options.size()));
    }

    @Override
    protected void extractLabels(@NotNull GuiGraphicsExtractor g, int mouseX, int mouseY) {
        g.text(this.font, this.title, this.titleLabelX, this.titleLabelY, INK, false);
        g.text(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, INK, false);
        if (this.craftButton != null && this.craftButton.visible) {
            g.pose().pushMatrix();
            g.pose().translate(INFO_X + 1, 118);
            g.pose().scale(0.6f, 0.6f);
            Gui26.text(g, this.font, "Shift-click Craft", 0, 0, 0xFF606060, false);
            Gui26.text(g, this.font, "to make a stack", 0, 10, 0xFF606060, false);
            g.pose().popMatrix();
        }
    }

    @Override
    public void extractRenderState(@NotNull GuiGraphicsExtractor g, int mouseX, int mouseY, float a) {
        super.extractRenderState(g, mouseX, mouseY, a);
        int x = this.leftPos, y = this.topPos;
        for (int i = 0; i < FILTER_ICONS.length; i++) {
            int fx = x + 8 + i * 20, fy = y + 16;
            if (mouseX >= fx && mouseX < fx + 18 && mouseY >= fy && mouseY < fy + 18)
                g.setTooltipForNextFrame(this.font, Component.literal(FILTER_NAMES[i]), mouseX, mouseY);
        }
        Cell hover = cellAt(mouseX, mouseY);
        if (hover != null) g.setTooltipForNextFrame(this.font, new ItemStack(hover.item), mouseX, mouseY);
        if (filter == 1 && mouseX >= x + GRID_X && mouseX < x + GRID_X + COLS * CELL && mouseY >= y + GRID_Y && mouseY < y + GRID_Y + 15) {
            RigSets.RigSet set = RigSets.SETS.get(Math.floorMod(setIndex, RigSets.SETS.size()));
            g.setTooltipForNextFrame(this.font, List.of(Component.literal(set.name()).withStyle(net.minecraft.ChatFormatting.LIGHT_PURPLE),
                    Component.literal("2 pieces: " + set.twoText()), Component.literal("3 pieces: " + set.bonusText())), Optional.empty(), mouseX, mouseY);
        }
    }

    private Cell cellAt(double mx, double my) {
        int gx = (int) Math.floor(mx) - this.leftPos - GRID_X, gy = (int) Math.floor(my) - this.topPos - gridTop();
        if (gx < 0 || gy < 0 || gx >= COLS * CELL || gy >= rows() * CELL) return null;
        int i = (scroll + gy / CELL) * COLS + gx / CELL;
        return i < cells.size() ? cells.get(i) : null;
    }

    // ------------------------------------------------------------------ input

    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent e, boolean doubleClick) {
        double mx = e.x(), my = e.y();
        int x = this.leftPos, y = this.topPos;
        for (int i = 0; i < FILTER_ICONS.length; i++) {
            int fx = x + 8 + i * 20, fy = y + 16;
            if (mx >= fx && mx < fx + 18 && my >= fy && my < fy + 18) {
                filter = i; scroll = 0; selectedId = null; rebuild(); click();
                if (Tide.CI) ciLog();
                return true;
            }
        }
        int tx = x + 8 + FILTER_ICONS.length * 20 + 4;
        if (mx >= tx && mx < tx + 60 && my >= y + 18 && my < y + 32) {
            onlyMakeable = !onlyMakeable; selectedId = null; rebuild(); click(); return true;
        }
        if (filter == 1 && my >= y + GRID_Y && my < y + GRID_Y + 15 && mx >= x + GRID_X && mx < x + GRID_X + COLS * CELL) {
            setIndex += mx < x + GRID_X + COLS * CELL / 2.0 ? -1 : 1;
            selectedId = null; scroll = 0; rebuild(); click(); return true;
        }
        Cell c = cellAt(mx, my);
        if (c != null) {
            if (key(c).equals(selectedId) && doubleClick && c.row != null) craft(false);
            selectedId = key(c); click(); return true;
        }
        if (clickIngredient(mx, my)) return true;
        return super.mouseClicked(e, doubleClick);
    }

    /** Clicking an ingredient that can be one of several things you have picks the next one to use. */
    private boolean clickIngredient(double mx, double my) {
        Cell cell = selected();
        if (cell == null || cell.row == null) return false;
        int ix = this.leftPos + INFO_X, nx = ix, ry = this.topPos + GRID_Y + 57;
        Map<Item, Integer> have = stock();
        Map<Integer, Item> pref = prefer.computeIfAbsent(cell.row.id(), k -> new HashMap<>());
        for (Need n : needs(cell.row)) {
            if (nx + 20 > ix + INFO_W) { nx = ix; ry += 20; }
            if (mx >= nx && mx < nx + 18 && my >= ry && my < ry + 18) {
                List<Item> owned = n.options.stream().filter(o -> have.getOrDefault(o, 0) > 0).toList();
                if (owned.size() < 2) return true;
                Item cur = shownOption(n, pref, have);
                Item next = owned.get((owned.indexOf(cur) + 1) % owned.size());
                for (int p : n.positions) pref.put(p, next);
                click();
                return true;
            }
            nx += 20;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double dx, double dy) {
        int total = (cells.size() + COLS - 1) / COLS;
        if (mx >= this.leftPos && mx < this.leftPos + INFO_X && my >= this.topPos + GRID_Y && my < this.topPos + GRID_Y + ROWS * CELL) {
            scroll = Math.max(0, Math.min(Math.max(0, total - rows()), scroll - (int) Math.signum(dy)));
            return true;
        }
        return super.mouseScrolled(mx, my, dx, dy);
    }

    private void click() {
        this.minecraft.getSoundManager().play(net.minecraft.client.resources.sounds.SimpleSoundInstance.forUI(net.minecraft.sounds.SoundEvents.UI_BUTTON_CLICK, 1.0f));
    }

    /** The kinds as the server numbers them (for tests and the CI log). */
    static TackleRecipes.Kind kind(int ordinal) {
        return TackleRecipes.Kind.values()[ordinal];
    }
}
