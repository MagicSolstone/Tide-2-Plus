package com.li64.tide.client.gui.screens;

import com.li64.tide.Tide;
import com.li64.tide.client.Gui26;
import com.li64.tide.client.gui.menus.AnglingCraftMenu;
import com.li64.tide.data.rods.RigSets;
import com.li64.tide.data.tackle.TackleLog;
import com.li64.tide.network.messages.BenchTabMsg;
import com.li64.tide.network.messages.TackleFillMsg;
import com.li64.tide.network.messages.TackleRecipesMsg;
import com.li64.tide.network.messages.TackleUnlocksMsg;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The Angling Table's Craft tab: a crafting grid for fishing gear, with its own recipe book beside it. The book shows
 * the recipes you've discovered, by kind or by rig; click one to move its items from your inventory into the grid
 * (shift-click for as many as you can make), or, if you're short, to see it laid out in the grid as a ghost.
 */
public class AnglingCraftScreen extends AbstractContainerScreen<AnglingCraftMenu> {
    private static final Identifier BG = Identifier.withDefaultNamespace("textures/gui/container/crafting_table.png");
    private static final WidgetSprites BOOK_BUTTON = new WidgetSprites(Identifier.withDefaultNamespace("recipe_book/button"),
            Identifier.withDefaultNamespace("recipe_book/button_highlighted"));
    private static final Identifier SLOT_OK = Identifier.withDefaultNamespace("recipe_book/slot_craftable"),
            SLOT_NO = Identifier.withDefaultNamespace("recipe_book/slot_uncraftable"),
            FILTER_ON = Identifier.withDefaultNamespace("recipe_book/filter_enabled"), FILTER_OFF = Identifier.withDefaultNamespace("recipe_book/filter_disabled"),
            FILTER_ON_HI = Identifier.withDefaultNamespace("recipe_book/filter_enabled_highlighted"), FILTER_OFF_HI = Identifier.withDefaultNamespace("recipe_book/filter_disabled_highlighted"),
            PAGE_FWD = Identifier.withDefaultNamespace("recipe_book/page_forward"), PAGE_BACK = Identifier.withDefaultNamespace("recipe_book/page_backward");
    public static final int BOOK_W = 147, BOOK_H = 166;
    private static final int COLS = 5, ROWS = 4, CELL = 25, CELLS_X = 11, CELLS_Y = 43;

    /** The filters along the top of the book: everything, by rig, then one per kind (the server's order). */
    private static final String[] FILTER_ICONS = {"tide:angling_table", "minecraft:nether_star", "tide:bamboo_fishing_rod", "tide:fishing_line",
            "tide:red_bobber", "tide:fishing_hook", "tide:spinner_lure", "tide:bait", "tide:tackle_box"};
    private static final String[] FILTER_NAMES = {"Everything", "Rigs", "Rods", "Lines", "Bobbers", "Hooks", "Lures", "Bait", "Gear"};

    /** A book entry: a discovered recipe, or (rig view) a piece not discovered yet or found rather than made. */
    private record Cell(TackleRecipesMsg.Row row, Item item, boolean locked) {}

    private static boolean bookOpen = true, onlyMakeable = false;
    private static int filter = 0, setIndex = 0;
    private int page = 0, bookX, lockedHere = 0;
    private List<Cell> cells = List.of();
    private TackleRecipesMsg.Row ghost = null;
    private int ghostTick = 0;
    private Map<Item, Integer> stock = Map.of();
    private int stockTick = -1;
    private Set<String> seenUnlocks = Set.of();

    public AnglingCraftScreen(AnglingCraftMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, Component.literal("Angling Table"));
        this.titleLabelX = 29;
    }

    private boolean narrow() {
        return this.width < 379;
    }

    @Override
    protected void init() {
        super.init();
        com.li64.tide.client.MouseKeep.restore();
        if (bookOpen && !narrow()) this.leftPos = (this.width - this.imageWidth) / 2 + 77;
        this.bookX = bookOpen && narrow() ? (this.width - BOOK_W) / 2 : this.leftPos - BOOK_W - 2;
        this.addRenderableWidget(new ImageButton(this.leftPos + 5, this.topPos + 34, 20, 18, BOOK_BUTTON, b -> {
            bookOpen = !bookOpen;
            this.rebuildWidgets();
        }, Component.literal("Fishing recipe book")));
        this.addRenderableWidget(Button.builder(Component.literal("Assemble rod"), b -> {
                    com.li64.tide.client.MouseKeep.save();
                    Tide.NETWORK.sendToServer(new BenchTabMsg(false));
                }).bounds(this.leftPos + this.imageWidth - 80, this.topPos - 17, 80, 16).build());
        seenUnlocks = TackleUnlocksMsg.CLIENT;
        rebuild();
    }

    private boolean ciPending = false;
    private static boolean ciScriptDone = false;
    private int ciTicks = 0;

    /**
     * CI only, once: what a player would do, step by step (mouse clicks in CI land in the wrong place too easily):
     * the Hooks filter, a diamond hook filled from the book, the result shift-clicked out, then the Assemble tab.
     * Each step logs "TIDE-CI table step N" so the smoke test can take a screenshot after it.
     */
    private void ciScript() {
        ciTicks++;
        if (ciTicks == 40) { filter = 5; page = 0; rebuild(); Tide.LOG.info("TIDE-CI table step 1 hooks: {} shown", cells.size()); }
        else if (ciTicks == 100) {
            var row = TackleRecipesMsg.CLIENT.stream().filter(r -> r.result().equals("tide:diamond_hook")).findFirst().orElse(null);
            if (row != null) Tide.NETWORK.sendToServer(new TackleFillMsg(row.id(), false));
            Tide.LOG.info("TIDE-CI table step 2 fill diamond hook ({})", row != null && canMake(row) ? "can make" : "can't make");
        } else if (ciTicks == 160) {
            Tide.LOG.info("TIDE-CI table step 3 result {}", this.menu.getSlot(AnglingCraftMenu.RESULT).getItem());
        } else if (ciTicks == 200) {
            this.slotClicked(this.menu.getSlot(AnglingCraftMenu.RESULT), AnglingCraftMenu.RESULT, 0, net.minecraft.world.inventory.ContainerInput.QUICK_MOVE);
        } else if (ciTicks == 260) {
            Inventory inv = this.minecraft.player.getInventory();
            String made = "none";
            for (int i = 0; i < inv.getContainerSize(); i++)
                if (inv.getItem(i).is(item("tide:diamond_hook"))) made = String.valueOf(com.li64.tide.data.rods.RodEffects.baseHook(inv.getItem(i)));
            Tide.LOG.info("TIDE-CI table step 4 diamond hook in inventory, base hook {}", made);
        } else if (ciTicks == 320) {
            ciScriptDone = true;
            Tide.LOG.info("TIDE-CI table step 5 assemble tab");
            Tide.NETWORK.sendToServer(new BenchTabMsg(false));
        }
    }

    /** CI: where things are on screen (window pixels), so the smoke test can click them. */
    private void ciLog() {
        double k = (double) this.minecraft.getWindow().getScreenWidth() / this.width;
        Tide.LOG.info("TIDE-CI table {} recipes, {} discovered, filter {}, {} shown, {} to discover, book {}", TackleRecipesMsg.CLIENT.size(),
                TackleUnlocksMsg.CLIENT.size(), filter, cells.size(), lockedHere, bookOpen);
        Tide.LOG.info("TIDE-CI table px hooks {} {} result {} {} assemble {} {}",
                (int) ((bookX + filterX(5) + 7) * k), (int) ((this.topPos + 17) * k),
                (int) ((this.leftPos + 124 + 8) * k), (int) ((this.topPos + 35 + 8) * k),
                (int) ((this.leftPos + this.imageWidth - 40) * k), (int) ((this.topPos - 9) * k));
        for (int i = 0; i < Math.min(cells.size() - page * COLS * ROWS, COLS * ROWS); i++) {
            Cell c = cells.get(page * COLS * ROWS + i);
            if (BuiltInRegistries.ITEM.getKey(c.item).getPath().equals("diamond_hook"))
                Tide.LOG.info("TIDE-CI table px diamond_hook {} {}", (int) ((bookX + CELLS_X + (i % COLS) * CELL + 12) * k),
                        (int) ((this.topPos + CELLS_Y + (i / COLS) * CELL + 12) * k));
        }
    }

    // ------------------------------------------------------------------ the book's contents

    private static Item item(String id) {
        Identifier key = Identifier.tryParse(id);
        return key == null ? Items.AIR : BuiltInRegistries.ITEM.getOptional(key).orElse(Items.AIR);
    }

    /** What the player has to craft with: inventory, offhand and whatever is already in the grid. */
    private Map<Item, Integer> stock() {
        if (stockTick == this.minecraft.player.tickCount) return stock;
        stockTick = this.minecraft.player.tickCount;
        Map<Item, Integer> out = new HashMap<>();
        Inventory inv = this.minecraft.player.getInventory();
        for (int i = 0; i < 36; i++) add(out, inv.getItem(i));
        add(out, inv.getItem(40));
        for (int i = AnglingCraftMenu.GRID_START; i < AnglingCraftMenu.INV_START; i++) add(out, this.menu.getSlot(i).getItem());
        return stock = out;
    }

    private static void add(Map<Item, Integer> out, ItemStack s) {
        if (!s.isEmpty()) out.merge(s.getItem(), s.getCount(), Integer::sum);
    }

    private boolean canMake(TackleRecipesMsg.Row row) {
        Map<Item, Integer> left = new HashMap<>(stock());
        for (List<String> opts : row.ingredients()) {
            Item pick = null;
            for (String o : opts) {
                Item it = item(o);
                if (left.getOrDefault(it, 0) > 0) { pick = it; break; }
            }
            if (pick == null) return false;
            left.merge(pick, -1, Integer::sum);
        }
        return true;
    }

    private static boolean known(TackleRecipesMsg.Row row) {
        return TackleUnlocksMsg.CLIENT.contains(row.id());
    }

    private void rebuild() {
        List<Cell> out = new ArrayList<>();
        List<TackleRecipesMsg.Row> rows = TackleRecipesMsg.CLIENT;
        lockedHere = 0;
        if (filter == 1) {
            RigSets.RigSet set = rig();
            for (String m : set.members()) {
                Item it = item(Tide.MOD_ID + ":" + m);
                if (it == Items.AIR) continue;
                TackleRecipesMsg.Row row = rows.stream().filter(r -> item(r.result()) == it).findFirst().orElse(null);
                boolean locked = row != null && !known(row);
                if (locked) lockedHere++;
                out.add(new Cell(row, it, locked));
            }
        } else {
            for (TackleRecipesMsg.Row r : rows) {
                if (filter != 0 && r.kind() != filter - 2) continue;
                if (known(r)) out.add(new Cell(r, item(r.result()), false));
                else lockedHere++;
            }
        }
        if (onlyMakeable) out.removeIf(c -> c.row == null || c.locked || !canMake(c.row));
        cells = out;
        page = Math.max(0, Math.min(page, pages() - 1));
        ciPending = Tide.CI;
    }

    private RigSets.RigSet rig() {
        return RigSets.SETS.get(Math.floorMod(setIndex, RigSets.SETS.size()));
    }

    private int pages() {
        return Math.max(1, (cells.size() + COLS * ROWS - 1) / (COLS * ROWS));
    }

    private static int filterX(int i) {
        return 8 + i * 14 + (i > 1 ? 3 : 0);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (Tide.CI && !ciScriptDone) ciScript();
        if (!TackleUnlocksMsg.CLIENT.equals(seenUnlocks)) { seenUnlocks = TackleUnlocksMsg.CLIENT; rebuild(); }
        else if (onlyMakeable && this.minecraft.player.tickCount % 10 == 0) rebuild();
        if (ghost != null && this.minecraft.player.tickCount - ghostTick > 10) for (int i = AnglingCraftMenu.GRID_START; i < AnglingCraftMenu.INV_START; i++)
            if (this.menu.getSlot(i).hasItem()) { ghost = null; break; }
    }

    // ------------------------------------------------------------------ drawing

    @Override
    public void extractBackground(@NotNull GuiGraphicsExtractor g, int mouseX, int mouseY, float a) {
        super.extractBackground(g, mouseX, mouseY, a);
        g.blit(RenderPipelines.GUI_TEXTURED, BG, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        if (ghost != null) drawGhost(g);
        if (bookOpen) drawBook(g, mouseX, mouseY);
    }

    private void drawGhost(GuiGraphicsExtractor g) {
        for (int i = 0; i < ghost.ingredients().size() && i < ghost.grid().size(); i++) {
            Item it = ghostItem(i);
            if (it == Items.AIR) continue;
            Slot s = this.menu.getSlot(AnglingCraftMenu.GRID_START + ghost.grid().get(i));
            int x = this.leftPos + s.x, y = this.topPos + s.y;
            g.item(new ItemStack(it), x, y);
            g.fill(x, y, x + 16, y + 16, 0x80_8B8B8B);
        }
        Slot r = this.menu.getSlot(AnglingCraftMenu.RESULT);
        int x = this.leftPos + r.x, y = this.topPos + r.y;
        g.item(new ItemStack(item(ghost.result()), ghost.count()), x, y);
        g.fill(x, y, x + 16, y + 16, 0x80_8B8B8B);
    }

    private void drawBook(GuiGraphicsExtractor g, int mouseX, int mouseY) {
        int x = bookX, y = this.topPos;
        // the book: a light frame around a dark page, like the crafting table's recipe book
        g.fill(x, y + 1, x + BOOK_W, y + BOOK_H - 1, 0xFF000000);
        g.fill(x + 1, y, x + BOOK_W - 1, y + BOOK_H, 0xFF000000);
        g.fill(x + 1, y + 1, x + BOOK_W - 1, y + BOOK_H - 1, 0xFFC6C6C6);
        g.fill(x + 1, y + 1, x + BOOK_W - 3, y + 3, 0xFFFFFFFF);
        g.fill(x + 1, y + 1, x + 3, y + BOOK_H - 3, 0xFFFFFFFF);
        g.fill(x + 3, y + BOOK_H - 3, x + BOOK_W - 1, y + BOOK_H - 1, 0xFF555555);
        g.fill(x + BOOK_W - 3, y + 3, x + BOOK_W - 1, y + BOOK_H - 1, 0xFF555555);
        g.fill(x + 7, y + 7, x + BOOK_W - 7, y + BOOK_H - 7, 0xFF373737);

        // filters: small icons in a row (everything and rigs, a gap, then the kinds)
        for (int i = 0; i < FILTER_ICONS.length; i++) {
            int fx = x + filterX(i), fy = y + 10;
            boolean hover = mouseX >= fx && mouseX < fx + 14 && mouseY >= fy && mouseY < fy + 14;
            g.fill(fx, fy, fx + 14, fy + 14, i == filter ? 0xFFE8C46A : hover ? 0xFF8A8A8A : 0xFF5A5A5A);
            g.fill(fx + 1, fy + 1, fx + 13, fy + 13, i == filter ? 0xFF8A6A2A : 0xFF2A2A2A);
            g.pose().pushMatrix();
            g.pose().translate(fx + 1, fy + 1);
            g.pose().scale(0.75f, 0.75f);
            g.item(new ItemStack(item(FILTER_ICONS[i])), 0, 0);
            g.pose().popMatrix();
        }

        // the heading (or the rig picker), and the "only what I can make" switch
        int hy = y + 28;
        if (filter == 1) {
            String name = rig().name();
            Gui26.text(g, this.font, "<", x + 10, hy + 4, 0xFFFFFFFF, true);
            Gui26.text(g, this.font, ">", x + 104, hy + 4, 0xFFFFFFFF, true);
            Gui26.text(g, this.font, name, x + 10 + (98 - this.font.width(name)) / 2, hy + 4, 0xFFFFD27F, true);
        } else {
            Gui26.text(g, this.font, FILTER_NAMES[filter], x + 10, hy + 4, 0xFFFFFFFF, true);
        }
        int tx = x + 113, ty = hy + 1;
        boolean overToggle = mouseX >= tx && mouseX < tx + 26 && mouseY >= ty && mouseY < ty + 16;
        g.blitSprite(RenderPipelines.GUI_TEXTURED, onlyMakeable ? (overToggle ? FILTER_ON_HI : FILTER_ON) : (overToggle ? FILTER_OFF_HI : FILTER_OFF), tx, ty, 26, 16);

        // the recipes
        int first = page * COLS * ROWS;
        for (int i = 0; i < COLS * ROWS && first + i < cells.size(); i++) {
            Cell c = cells.get(first + i);
            int cx = x + CELLS_X + (i % COLS) * CELL, cy = y + CELLS_Y + (i / COLS) * CELL;
            if (c.locked) {
                g.fill(cx + 1, cy + 1, cx + 24, cy + 24, 0xFF202020);
                Gui26.text(g, this.font, "?", cx + 10, cy + 8, 0xFF7A7A7A, false);
                continue;
            }
            boolean make = c.row != null && canMake(c.row);
            g.blitSprite(RenderPipelines.GUI_TEXTURED, make ? SLOT_OK : SLOT_NO, cx, cy, CELL, CELL);
            g.item(new ItemStack(c.item, c.row == null ? 1 : c.row.count()), cx + 4, cy + 4);
            if (c.row != null && c.row.count() > 1) g.itemDecorations(this.font, new ItemStack(c.item, c.row.count()), cx + 4, cy + 4);
            if (c.row == null) g.fill(cx + 4, cy + 4, cx + 20, cy + 20, 0x90_202020);       // found, not made
        }
        if (cells.isEmpty()) {
            String msg = onlyMakeable ? "Nothing you can make yet" : "Nothing discovered yet";
            Gui26.text(g, this.font, msg, x + (BOOK_W - this.font.width(msg)) / 2, y + 80, 0xFFAAAAAA, false);
        }
        // how many more there are to find, and the pages
        if (lockedHere > 0) {
            g.pose().pushMatrix();
            g.pose().translate(x + 10, y + 149);
            g.pose().scale(0.75f, 0.75f);
            Gui26.text(g, this.font, lockedHere + " more to discover", 0, 0, 0xFF9AA8B4, false);
            g.pose().popMatrix();
        }
        if (pages() > 1) {
            String p = (page + 1) + "/" + pages();
            Gui26.text(g, this.font, p, x + 113 - this.font.width(p) / 2, y + 147, 0xFFFFFFFF, false);
            if (page > 0) g.blitSprite(RenderPipelines.GUI_TEXTURED, PAGE_BACK, x + 92, y + 143, 12, 17);
            if (page < pages() - 1) g.blitSprite(RenderPipelines.GUI_TEXTURED, PAGE_FWD, x + 123, y + 143, 12, 17);
        }
    }

    @Override
    protected void extractLabels(@NotNull GuiGraphicsExtractor g, int mouseX, int mouseY) {
        g.text(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xFF404040, false);
        g.text(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0xFF404040, false);
    }

    @Override
    public void extractRenderState(@NotNull GuiGraphicsExtractor g, int mouseX, int mouseY, float a) {
        super.extractRenderState(g, mouseX, mouseY, a);
        if (ciPending) { ciPending = false; ciLog(); }
        if (!bookOpen) return;
        int x = bookX, y = this.topPos;
        for (int i = 0; i < FILTER_ICONS.length; i++) {
            int fx = x + filterX(i), fy = y + 10;
            if (mouseX >= fx && mouseX < fx + 14 && mouseY >= fy && mouseY < fy + 14)
                g.setTooltipForNextFrame(this.font, Component.literal(FILTER_NAMES[i]), mouseX, mouseY);
        }
        if (mouseX >= x + 113 && mouseX < x + 139 && mouseY >= y + 29 && mouseY < y + 45)
            g.setTooltipForNextFrame(this.font, Component.literal(onlyMakeable ? "Showing what you can make" : "Showing everything you've discovered"), mouseX, mouseY);
        if (filter == 1) {
            // only over the rig's name, not the arrows; wrapped, and held above the cursor so the name stays visible
            RigSets.RigSet set = rig();
            int nameW = this.font.width(set.name()), nameX = x + 10 + (98 - nameW) / 2;
            if (mouseX >= nameX && mouseX < nameX + nameW && mouseY >= y + 28 && mouseY < y + 42) {
                List<Component> tip = new ArrayList<>();
                wrap(tip, set.name(), ChatFormatting.LIGHT_PURPLE);
                wrap(tip, "2 pieces: " + set.twoText(), ChatFormatting.WHITE);
                wrap(tip, "3 pieces: " + set.bonusText(), ChatFormatting.WHITE);
                wrap(tip, "Click the arrows for other rigs", ChatFormatting.GRAY);
                int top = Math.max(16, mouseY - 8 - (tip.size() * 10 + 6));     // the tooltip draws from 12 above the y it is given
                g.setTooltipForNextFrame(this.font, tip, Optional.empty(), mouseX, top + 12);
            }
        }
        if (ghost != null) {
            List<Component> tip = ghostTooltip(mouseX, mouseY);
            if (tip != null) { g.setTooltipForNextFrame(this.font, tip, Optional.empty(), mouseX, mouseY); return; }
        }
        Cell c = cellAt(mouseX, mouseY);
        if (c == null) return;
        if (c.locked) {
            g.setTooltipForNextFrame(this.font, List.of(Component.literal("Not discovered yet").withStyle(ChatFormatting.GRAY),
                    Component.literal("Have the right materials to learn it").withStyle(ChatFormatting.DARK_GRAY)), Optional.empty(), mouseX, mouseY);
        } else if (c.row == null) {
            String where = TackleLog.FOUND.getOrDefault(BuiltInRegistries.ITEM.getKey(c.item).getPath(), "Found while fishing or exploring.");
            g.setTooltipForNextFrame(this.font, List.of(new ItemStack(c.item).getHoverName(),
                    Component.literal("Not crafted: " + where).withStyle(ChatFormatting.GOLD)), Optional.empty(), mouseX, mouseY);
        } else {
            List<Component> tip = new ArrayList<>(net.minecraft.client.gui.screens.Screen.getTooltipFromItem(this.minecraft, new ItemStack(c.item)));
            boolean make = canMake(c.row);
            tip.add(Component.literal(make ? "Click to fill the grid (shift: as many as you can)" : "Click to see what it takes")
                    .withStyle(make ? ChatFormatting.GREEN : ChatFormatting.YELLOW));
            g.setTooltipForNextFrame(this.font, tip, Optional.empty(), mouseX, mouseY);
        }
    }

    /** Adds a line of text, wrapped to a comfortable tooltip width. */
    private void wrap(List<Component> out, String text, ChatFormatting colour) {
        for (var part : this.font.getSplitter().splitLines(Component.literal(text), 170, net.minecraft.network.chat.Style.EMPTY))
            out.add(Component.literal(part.getString()).withStyle(colour));
    }

    /** The item the ghost shows for one of its grid ingredients (cycling through what would do). */
    private Item ghostItem(int i) {
        List<String> opts = ghost.ingredients().get(i);
        return opts.isEmpty() ? Items.AIR : item(opts.get((int) ((System.currentTimeMillis() / 1000) % opts.size())));
    }

    /** A tooltip for the ghost item under the cursor (an ingredient in the grid, or what it makes), if any. */
    private List<Component> ghostTooltip(int mouseX, int mouseY) {
        for (int i = 0; i < ghost.ingredients().size() && i < ghost.grid().size(); i++) {
            Item it = ghostItem(i);
            Slot s = this.menu.getSlot(AnglingCraftMenu.GRID_START + ghost.grid().get(i));
            if (it != Items.AIR && over(s, mouseX, mouseY)) return new ArrayList<>(net.minecraft.client.gui.screens.Screen.getTooltipFromItem(this.minecraft, new ItemStack(it)));
        }
        if (over(this.menu.getSlot(AnglingCraftMenu.RESULT), mouseX, mouseY))
            return new ArrayList<>(net.minecraft.client.gui.screens.Screen.getTooltipFromItem(this.minecraft, new ItemStack(item(ghost.result()), ghost.count())));
        return null;
    }

    private boolean over(Slot s, int mouseX, int mouseY) {
        int sx = this.leftPos + s.x, sy = this.topPos + s.y;
        return mouseX >= sx && mouseX < sx + 16 && mouseY >= sy && mouseY < sy + 16;
    }

    private Cell cellAt(double mx, double my) {
        if (!bookOpen) return null;
        int gx = (int) Math.floor(mx) - bookX - CELLS_X, gy = (int) Math.floor(my) - this.topPos - CELLS_Y;
        if (gx < 0 || gy < 0 || gx >= COLS * CELL || gy >= ROWS * CELL) return null;
        int i = page * COLS * ROWS + (gy / CELL) * COLS + gx / CELL;
        return i < cells.size() ? cells.get(i) : null;
    }

    // ------------------------------------------------------------------ input

    private boolean inBook(double mx, double my) {
        return bookOpen && mx >= bookX && mx < bookX + BOOK_W && my >= this.topPos && my < this.topPos + BOOK_H;
    }

    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent e, boolean doubleClick) {
        if (!inBook(e.x(), e.y())) return super.mouseClicked(e, doubleClick);
        double mx = e.x(), my = e.y();
        int x = bookX, y = this.topPos;
        for (int i = 0; i < FILTER_ICONS.length; i++) {
            int fx = x + filterX(i), fy = y + 10;
            if (mx >= fx && mx < fx + 14 && my >= fy && my < fy + 14) {
                filter = i; page = 0; rebuild(); click();
                return true;
            }
        }
        if (mx >= x + 113 && mx < x + 139 && my >= y + 29 && my < y + 45) {
            onlyMakeable = !onlyMakeable; page = 0; rebuild(); click(); return true;
        }
        if (filter == 1 && my >= y + 28 && my < y + 42 && mx >= x + 8 && mx < x + 112) {
            setIndex += mx < x + 60 ? -1 : 1; page = 0; rebuild(); click(); return true;
        }
        if (pages() > 1 && my >= y + 143 && my < y + 160) {
            if (mx >= x + 92 && mx < x + 104 && page > 0) { page--; click(); return true; }
            if (mx >= x + 123 && mx < x + 135 && page < pages() - 1) { page++; click(); return true; }
        }
        Cell c = cellAt(mx, my);
        if (c != null && c.row != null && !c.locked) {
            if (canMake(c.row)) {
                ghost = null;
                Tide.NETWORK.sendToServer(new TackleFillMsg(c.row.id(), e.hasShiftDown()));
            } else {
                // like the crafting table's book: whatever is in the grid goes back to the inventory, then the recipe shows as a ghost
                boolean gridEmpty = true;
                for (int i = AnglingCraftMenu.GRID_START; i < AnglingCraftMenu.INV_START; i++) if (this.menu.getSlot(i).hasItem()) gridEmpty = false;
                if (!gridEmpty) Tide.NETWORK.sendToServer(new TackleFillMsg(c.row.id(), false));      // (it clears the grid, then comes up short)
                ghost = c.row;
                ghostTick = this.minecraft.player.tickCount;
            }
            click();
        }
        return true;                                              // a click on the book never reaches the slots behind it
    }

    @Override
    protected boolean hasClickedOutside(double mx, double my, int left, int top) {
        return !inBook(mx, my) && super.hasClickedOutside(mx, my, left, top);
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double dx, double dy) {
        if (inBook(mx, my) && pages() > 1) {
            page = Math.max(0, Math.min(pages() - 1, page - (int) Math.signum(dy)));
            return true;
        }
        return super.mouseScrolled(mx, my, dx, dy);
    }

    private void click() {
        this.minecraft.getSoundManager().play(net.minecraft.client.resources.sounds.SimpleSoundInstance.forUI(net.minecraft.sounds.SoundEvents.UI_BUTTON_CLICK, 1.0f));
    }
}
