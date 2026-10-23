package com.li64.tide.client.gui.screens.journal;

import com.li64.tide.client.TidePipelines;
import net.minecraft.client.renderer.RenderPipelines;
import com.li64.tide.Tide;
import com.li64.tide.client.gui.screens.FishyNoteScreen;
import com.li64.tide.data.TideData;
import com.li64.tide.data.fishing.FishData;
import com.li64.tide.data.journal.JournalGroup;
import com.li64.tide.data.player.TidePlayerData;
import com.li64.tide.network.messages.ReadProfileMsg;
import com.li64.tide.registries.TideSoundEvents;
import com.li64.tide.util.TideUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.*;
import java.util.stream.Collectors;

public class FishingJournal extends Screen {
    public static final int TEXT_COLOR = 0xc09473;

    static final Identifier BACKGROUND = Tide.resource("textures/gui/journal/journal_bg.png");
    static final int BG_WIDTH = 400;
    static final int BG_HEIGHT = 260;

    static final Identifier LINE_TOP = TideUtils.sprite("journal/line_top");
    static final Identifier LINE_BOTTOM = TideUtils.sprite("journal/line_bottom");

    static final Identifier FISHY_NOTE_ICON = Tide.resource("textures/gui/journal/fishy_note_icon.png");
    static final Identifier FISHY_NOTE_ICON_SELECTED = Tide.resource("textures/gui/journal/fishy_note_icon_selected.png");

    private static final int AREA_HEIGHT = 190;
    private static final int CELL_SIZE = 22;
    private static final int GROUP_SPACING = 10;
    private static final int FISH_PER_ROW = 14;

    private final Player player;
    private final List<Map<JournalGroup, List<Holder<Item>>>> fishByPage;
    private int page = 0;

    private @Nullable ItemStack activeFish = null;
    private @Nullable FishyNoteScreen activeNote = null;
    private @Nullable FishProfile profile = null;
    private @Nullable Button xButton = null;
    private @Nullable Button leftButton = null;
    private @Nullable Button rightButton = null;
    private @Nullable Button anglerButton = null;
    private boolean showAngler = false;
    private boolean showTackle = false;
    private @Nullable Button tackleButton = null;
    private final TackleCard tackle = new TackleCard();
    private float yOffset = 3f;
    private boolean didClick;

    public FishingJournal(Player player) {
        super(GameNarrator.NO_TITLE);
        this.player = player;
        this.fishByPage = paginate(TideData.FISH.get().valueStream()
                .filter(FishData::hasJournalEntry)
                .sorted(Comparator.comparing((FishData a) -> a.profile().group().ordinal())
                        .thenComparing(a -> a.profile().rarity().ordinal())
                        .thenComparingDouble(d -> -d.weight()))
                .collect(Collectors.groupingBy(
                        data -> data.profile().group(),
                        LinkedHashMap::new,
                        Collectors.mapping(FishData::fish, Collectors.toList())
                )));

        this.init();
        player.playSound(TideSoundEvents.JOURNAL_OPEN, 0.9f, 1.0f + new Random().nextFloat() * 0.2f);
    }

    public static List<Map<JournalGroup, List<Holder<Item>>>> paginate(Map<JournalGroup, List<Holder<Item>>> groupedFish) {
        List<Map<JournalGroup, List<Holder<Item>>>> pages = new ArrayList<>();
        Map<JournalGroup, List<Holder<Item>>> currentPage = new LinkedHashMap<>();
        int usedHeight = 0;

        for (var entry : groupedFish.entrySet()) {
            JournalGroup group = entry.getKey();
            List<Holder<Item>> fish = entry.getValue();

            int totalRows = (int) Math.ceil(fish.size() / (double) FISH_PER_ROW);
            int start = 0;
            for (int row = 0; row < totalRows; row++) {
                int end = Math.min(start + FISH_PER_ROW, fish.size());
                List<Holder<Item>> rowFish = fish.subList(start, end);

                if (usedHeight + CELL_SIZE > AREA_HEIGHT) {
                    pages.add(currentPage);
                    currentPage = new LinkedHashMap<>();
                    usedHeight = 0;
                }

                currentPage.computeIfAbsent(group, g -> new ArrayList<>()).addAll(rowFish);
                usedHeight += CELL_SIZE;
                start = end;
            }

            if (usedHeight > 0 && usedHeight + GROUP_SPACING <= AREA_HEIGHT)
                usedHeight += GROUP_SPACING;
        }

        if (!currentPage.isEmpty()) pages.add(currentPage);
        return pages;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void pageChanged() {
        yOffset = 3f;
        player.playSound(TideSoundEvents.PAGE_FLIP, 1.0f, 1.0f + new Random().nextFloat() * 0.2f);
        this.init();
    }

    @Override
    protected void init() {
        this.clearWidgets();
        this.leftButton = null;
        this.rightButton = null;
        this.anglerButton = null;
        this.tackleButton = null;
        int xX = Math.min(this.width - 19, (width + BG_WIDTH) / 2 - 27);
        int xY = Math.max(3, (height - BG_HEIGHT) / 2 + 12);
        int leftX = Math.max(3, (width - BG_WIDTH) / 2 + 11);
        int rightX = Math.min(this.width - 19, (width + BG_WIDTH) / 2 - 27);
        if (activeFish != null) {
            this.activeNote = null;
            this.profile = new FishProfile(activeFish, this);
            this.xButton = Button.builder(Component.literal("X"), button -> {
                this.activeFish = null;
                this.pageChanged();
            }).bounds(xX, xY, 16, 16).build();
        }
        else if (activeNote != null) {
            this.profile = null;
            xX = Math.min(width - 19, (width + FishyNoteScreen.WIDTH) / 2 - 16);
            xY = Math.max(3, (height - FishyNoteScreen.HEIGHT) / 2);
            this.xButton = Button.builder(Component.literal("X"), button -> {
                this.activeNote = null;
                this.pageChanged();
            }).bounds(xX, xY, 16, 16).build();
        }
        else if (showTackle) {
            this.profile = null;
            this.xButton = Button.builder(Component.literal("X"), button -> {
                if (tackle.selected != null) tackle.selected = null;
                else this.showTackle = false;
                this.pageChanged();
            }).bounds(xX, xY, 16, 16).build();
            if (tackle.selected == null && tackle.pageCount() > 1) {
                this.leftButton = Button.builder(Component.literal("<<"), button -> tacklePage(-1))
                        .bounds(leftX, this.height / 2 - 8, 16, 16).build();
                this.addWidget(leftButton);
                this.rightButton = Button.builder(Component.literal(">>"), button -> tacklePage(1))
                        .bounds(rightX, this.height / 2 - 8, 16, 16).build();
                this.addWidget(rightButton);
            }
        }
        else if (showAngler) {
            this.profile = null;
            this.xButton = Button.builder(Component.literal("X"), button -> {
                this.showAngler = false;
                this.pageChanged();
            }).bounds(xX, xY, 16, 16).build();
        }
        else {
            this.profile = null;
            this.xButton = Button.builder(Component.literal("X"), button -> onClose())
                    .bounds(xX, xY, 16, 16).build();
            int lvl = com.li64.tide.data.angler.Angler.levelForXp(com.li64.tide.client.ClientAngler.xp);
            this.anglerButton = Button.builder(Component.literal("Lv " + lvl), button -> {
                this.showAngler = true;
                this.pageChanged();
            }).bounds(Math.max(3, (width - BG_WIDTH) / 2 + 30), xY, 40, 16).build();
            this.addWidget(anglerButton);
            this.tackleButton = Button.builder(Component.literal("Tackle"), button -> {
                this.showTackle = true;
                this.pageChanged();
            }).bounds(Math.max(3, (width - BG_WIDTH) / 2 + 30) + 44, xY, 46, 16).build();
            this.addWidget(tackleButton);
            if (page > 0) {
                this.leftButton = Button.builder(Component.literal("<<"), button -> pageLeft())
                        .bounds(leftX, this.height / 2 - 8, 16, 16).build();
                this.addWidget(leftButton);
            }
            if (page < fishByPage.size() - 1) {
                this.rightButton = Button.builder(Component.literal(">>"), button -> pageRight())
                        .bounds(rightX, this.height / 2 - 8, 16, 16).build();
                this.addWidget(rightButton);
            }
        }
        this.addWidget(xButton);
    }

    private void tacklePage(int by) {
        int n = tackle.pageCount();
        tackle.page = ((tackle.page + by) % n + n) % n;
        this.pageChanged();
    }

    private void pageLeft() {
        page--;
        if (page < 0) page = fishByPage.size() - 1;
        this.pageChanged();
    }

    private void pageRight() {
        page++;
        if (page == fishByPage.size()) page = 0;
        this.pageChanged();
    }

    @Override
    public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent event, boolean doubleClick) {
        if (!super.mouseClicked(event, doubleClick)) this.didClick = true;
        return true;
    }

    @Override
    public boolean keyPressed(net.minecraft.client.input.KeyEvent event) {
        // 26.3 uses SDL key codes, so go through the event's helpers (and the inventory key) rather than raw numbers
        boolean back = event.isEscape() || Minecraft.getInstance().options.keyInventory.matches(event);
        if (back) {
            if (this.activeFish != null) {
                this.activeFish = null;
                this.pageChanged();
            }
            else if (this.activeNote != null) {
                this.activeNote = null;
                this.pageChanged();
            }
            else if (this.showAngler) {
                this.showAngler = false;
                this.pageChanged();
            }
            else if (this.showTackle) {
                if (tackle.selected != null) tackle.selected = null;
                else this.showTackle = false;
                this.pageChanged();
            }
            else this.onClose();
            return true;
        }
        if (event.isRight()) {
            if (showTackle) { if (tackle.selected == null) tacklePage(1); }
            else if (this.activeFish == null && !showAngler) pageRight();
            return true;
        }
        if (event.isLeft()) {
            if (showTackle) { if (tackle.selected == null) tacklePage(-1); }
            else if (this.activeFish == null && !showAngler) pageLeft();
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void onClose() {
        super.onClose();
        player.playSound(TideSoundEvents.JOURNAL_CLOSE, 0.9f, 1.0f + new Random().nextFloat() * 0.2f);
    }

    public Font getFont() {
        return this.font;
    }

    @Override
    public void extractRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        float deltaTicks = Minecraft.getInstance().getDeltaTracker().getRealtimeDeltaTicks();
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);

        boolean updatePage = false;

        this.yOffset = Math.max(yOffset - yOffset * 0.65f * deltaTicks, 0f);
        graphics.pose().pushMatrix();
        graphics.pose().translate(0f, -(int) yOffset);

        int tlX = (width - BG_WIDTH) / 2;
        int tlY = (height - BG_HEIGHT) / 2;
        if (activeNote == null) graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, tlX, tlY, 0, 0, BG_WIDTH, BG_HEIGHT, BG_WIDTH, BG_HEIGHT);

        if (profile != null) profile.extractRenderState(graphics, mouseX, mouseY, partialTick);
        else if (activeNote != null) activeNote.extractRenderState(graphics, mouseX, mouseY, partialTick);
        else if (showAngler) AnglerCard.draw(graphics, font, player, tlX, tlY);
        else if (showTackle) { if (tackle.draw(graphics, font, tlX, tlY, mouseX, mouseY, didClick)) updatePage = true; }
        else {
            // render fish grid
            final int hoverTolerance = 3;
            int cursorY = 0;

            int groupIndex = 0;
            for (var fish : fishByPage.get(page).values()) {
                int gridX = tlX + 44;
                int gridY = tlY + 39 + cursorY;
                int i = 0;

                for (Holder<Item> fishItem : fish) {
                    int cellX = i % FISH_PER_ROW;
                    int cellY = Mth.floor((float) i / FISH_PER_ROW);
                    int x = gridX + cellX * CELL_SIZE;
                    int y = gridY + cellY * CELL_SIZE;
                    ItemStack stack = new ItemStack(fishItem);
                    boolean isUnlocked = TidePlayerData.CLIENT_DATA.isFishUnlocked(fishItem);
                    boolean isUnread = TidePlayerData.CLIENT_DATA.isUnread(stack);
                    boolean hasNote = TidePlayerData.CLIENT_DATA.hasNote(stack);
                    boolean isHovering = false;

                    if (mouseX > x - hoverTolerance && mouseY > y - hoverTolerance
                            && mouseX < x + 16 + hoverTolerance && mouseY < y + 16 + hoverTolerance) {
                        isHovering = true;
                        if (isUnread) {
                            TidePlayerData.CLIENT_DATA.markAsRead(stack);
                            Tide.NETWORK.sendToServer(new ReadProfileMsg(stack));
                        }
                        if (isUnlocked) {
                            graphics.setTooltipForNextFrame(this.font, stack.getHoverName(), mouseX, mouseY);
                            if (didClick && !updatePage) {
                                activeFish = stack;
                                updatePage = true;
                            }
                        }
                        else {
                            FishData data = FishData.get(stack).orElse(null);
                            if (data != null) {
                                // TODO: render rarity in a tooltip
                                int numStars = data.profile().rarity().getNumStars();
                                Component stars = Component.literal("* ".repeat(numStars).stripTrailing());
                                //graphics.setTooltipForNextFrame(this.font, stars, mouseX, mouseY);
                            }
                            if (hasNote && didClick && !updatePage) {
                                activeNote = new FishyNoteScreen(stack, true);
                                updatePage = true;
                            }
                        }
                    }

                    // render shadow
                    renderItemSilhouette(graphics, stack, x + 1, y + 1, SHADOW_COLOR);

                    // render outline
                    if (isHovering) {
                        drawOutline(graphics, stack, x, y, 0xFFFFFFFF);
                    }
                    else if (Tide.CLIENT_CONFIG.journal.showUnread && (isUnlocked || hasNote) && isUnread) {
                        drawOutline(graphics, stack, x, y, 0xFFFFE000);
                    }

                    // render fish
                    if (isUnlocked) graphics.item(stack, x, y);
                    else renderItemSilhouette(graphics, stack, x, y, 0xFF000000);

                    // render fishy note icon
                    if (!isUnlocked && hasNote) {
                        graphics.pose().pushMatrix();
                        graphics.pose().translate(0f, 0f);
                        if (isHovering) graphics.blit(RenderPipelines.GUI_TEXTURED, FISHY_NOTE_ICON_SELECTED, x -2, y - 2,
                                0f, 0f, 9, 9, 9, 9);
                        else graphics.blit(RenderPipelines.GUI_TEXTURED, FISHY_NOTE_ICON, x - 2, y - 2,
                                0f, 0f, 9, 9, 9, 9);
                        graphics.pose().popMatrix();
                    }

                    i++;
                }

                cursorY += Mth.floor((float) (i - 1) / FISH_PER_ROW + 1) * CELL_SIZE + GROUP_SPACING;

                groupIndex++;
                if (groupIndex < fishByPage.get(page).size())
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, LINE_BOTTOM, tlX + 40, tlY + 36 + cursorY - GROUP_SPACING / 2, 327, 2);
            }
        }

        if (xButton != null) xButton.extractRenderState(graphics, mouseX, mouseY, partialTick);
        if (leftButton != null) leftButton.extractRenderState(graphics, mouseX, mouseY, partialTick);
        if (rightButton != null) rightButton.extractRenderState(graphics, mouseX, mouseY, partialTick);
        if (anglerButton != null) anglerButton.extractRenderState(graphics, mouseX, mouseY, partialTick);
        if (tackleButton != null) tackleButton.extractRenderState(graphics, mouseX, mouseY, partialTick);
        graphics.pose().popMatrix();

        if (updatePage) pageChanged();
        this.didClick = false;
    }

    /** The tan of the journal's fish shadows (#d7b594). */
    public static final int SHADOW_COLOR = 0xFFD7B594;

    private void drawOutline(@NotNull GuiGraphicsExtractor graphics, ItemStack stack, int x, int y, int color) {
        final int[][] offs = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        for (int[] off : offs) renderItemSilhouette(graphics, stack, x + off[0], y + off[1], color);
    }

    public static void renderTextureSilhouette(GuiGraphicsExtractor graphics, Identifier texture, int x, int y, int w, int h, int color) {
        TidePipelines.silhouette(graphics, texture, x, y, w, h, color);
    }

    public static void renderItemSilhouette(GuiGraphicsExtractor graphics, ItemStack stack, int x, int y, int color) {
        TidePipelines.itemSilhouette(graphics, stack, x, y, color);
    }
}
