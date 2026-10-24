package com.li64.tide.client.gui.menus;

import com.li64.tide.Tide;
import com.li64.tide.client.gui.TideMenuTypes;
import com.li64.tide.data.tackle.TackleRecipes;
import com.li64.tide.registries.blocks.AnglingTableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The Angling Table's Craft tab: a 3x3 grid and a result slot, like a crafting table, but for fishing gear (which the
 * crafting table no longer makes). Rod upgrades go in the grid too: the rod, the upgrade and its extra, any arrangement.
 * Slots: 0 result, 1-9 grid, 10-36 inventory, 37-45 hotbar (the crafting table's order).
 */
public class AnglingCraftMenu extends AbstractContainerMenu {
    public static final int RESULT = 0, GRID_START = 1, INV_START = 10, HOTBAR_START = 37, END = 46;
    private static final Map<UUID, Boolean> LAST_TAB = new ConcurrentHashMap<>();
    private final ContainerLevelAccess access;
    private final Player player;
    private final TransientCraftingContainer grid = new TransientCraftingContainer(this, 3, 3);
    private final ResultContainer result = new ResultContainer();
    private int resultCount = 0;
    private ItemStack lastResult = ItemStack.EMPTY;

    public AnglingCraftMenu(int id, Inventory inventory) {
        this(id, inventory, ContainerLevelAccess.NULL);
    }

    public AnglingCraftMenu(int id, Inventory inventory, ContainerLevelAccess access) {
        super(TideMenuTypes.ANGLING_CRAFT, id);
        this.access = access;
        this.player = inventory.player;
        this.addSlot(new TableResultSlot(this.result, 0, 124, 35));
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 3; col++)
                this.addSlot(new Slot(this.grid, col + row * 3, 30 + col * 18, 17 + row * 18));
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                this.addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
        for (int col = 0; col < 9; col++) this.addSlot(new Slot(inventory, col, 8 + col * 18, 142));
    }

    public ContainerLevelAccess access() {
        return access;
    }

    public Container grid() {
        return grid;
    }

    /** Opens the table on the tab the player used last (Craft the first time). */
    public static void openLast(ServerPlayer player, Level level, BlockPos pos) {
        open(player, level, pos, LAST_TAB.getOrDefault(player.getUUID(), true));
    }

    public static void open(ServerPlayer player, Level level, BlockPos pos, boolean craft) {
        LAST_TAB.put(player.getUUID(), craft);
        Component title = Component.translatable("container.tide.angling_table");
        if (craft) player.openMenu(new SimpleMenuProvider((id, inv, p) -> new AnglingCraftMenu(id, inv, ContainerLevelAccess.create(level, pos)), title));
        else player.openMenu(new SimpleMenuProvider((id, inv, p) -> new AnglingTableMenu(id, inv, ContainerLevelAccess.create(level, pos)), title));
    }

    // ------------------------------------------------------------------ crafting

    @Override
    public void slotsChanged(@NotNull Container container) {
        if (container != this.grid || this.player.level().isClientSide()) return;
        var m = TackleRecipes.match(this.grid.asPositionedCraftInput().input(), this.player.level());
        ItemStack out = m == null ? ItemStack.EMPTY : m.result().copy();
        this.resultCount = out.getCount();
        this.lastResult = out.copy();
        this.result.setItem(0, out);
        this.setRemoteSlot(RESULT, out);
        if (this.player instanceof ServerPlayer sp)
            sp.connection.send(new net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket(this.containerId, this.incrementStateId(), RESULT, out));
    }

    /** Takes one of everything in the grid (what the result was made from), leaving any remainders behind. */
    private void consume(Player player) {
        CraftingInput.Positioned pos = this.grid.asPositionedCraftInput();
        var m = TackleRecipes.match(pos.input(), player.level());
        List<ItemStack> rest = m == null ? List.of() : TackleRecipes.remainders(m);
        CraftingInput in = pos.input();
        for (int y = 0; y < in.height(); y++)
            for (int x = 0; x < in.width(); x++) {
                int slot = x + pos.left() + (y + pos.top()) * 3;
                ItemStack here = this.grid.getItem(slot);
                if (!here.isEmpty()) {
                    this.grid.removeItem(slot, 1);
                    here = this.grid.getItem(slot);
                }
                int k = x + y * in.width();
                ItemStack left = k < rest.size() ? rest.get(k) : ItemStack.EMPTY;
                if (left.isEmpty()) continue;
                if (here.isEmpty()) this.grid.setItem(slot, left);
                else if (ItemStack.isSameItemSameComponents(here, left)) { left.grow(here.getCount()); this.grid.setItem(slot, left); }
                else if (!player.getInventory().add(left)) player.drop(left, false, net.minecraft.util.Prediction.SERVER_ONLY);
            }
        this.slotsChanged(this.grid);
    }

    private class TableResultSlot extends Slot {
        TableResultSlot(Container container, int index, int x, int y) {
            super(container, index, x, y);
        }

        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            return false;
        }

        @Override
        public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
            if (!player.level().isClientSide()) {
                ItemStack made = lastResult.isEmpty() ? stack.copy() : lastResult.copy();      // (a shift-click hands over an emptied stack)
                made.onCraftedBy(player, made.getCount());
                player.awardStat(net.minecraft.stats.Stats.ITEM_CRAFTED.get(made.getItem()), made.getCount());
                if (Tide.CI) Tide.LOG.info("TIDE-CI table made {} (base hook {})", net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(made.getItem()),
                        com.li64.tide.data.rods.RodEffects.baseHook(made));
                consume(player);
            }
            super.onTake(player, stack);
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack stack = slot.getItem(), copy = stack.copy();
        if (index == RESULT) {
            if (!this.moveItemStackTo(stack, INV_START, END, true)) return ItemStack.EMPTY;
        } else if (index < INV_START) {
            if (!this.moveItemStackTo(stack, INV_START, END, false)) return ItemStack.EMPTY;
        } else if (index < HOTBAR_START) {
            if (!this.moveItemStackTo(stack, HOTBAR_START, END, false)) return ItemStack.EMPTY;
        } else if (!this.moveItemStackTo(stack, INV_START, HOTBAR_START, false)) return ItemStack.EMPTY;
        if (stack.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
        else slot.setChanged();
        if (stack.getCount() == copy.getCount()) return ItemStack.EMPTY;
        slot.onTake(player, stack);
        if (index == RESULT && !player.level().isClientSide()) player.drop(stack, false, net.minecraft.util.Prediction.SERVER_ONLY);
        return copy;
    }

    @Override
    public boolean canTakeItemForPickAll(@NotNull ItemStack stack, @NotNull Slot slot) {
        return slot.container != this.result && super.canTakeItemForPickAll(stack, slot);
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        this.access.execute((level, pos) -> this.clearContainer(player, this.grid));
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return this.access.evaluate((level, pos) -> level.getBlockState(pos).getBlock() instanceof AnglingTableBlock
                && player.isWithinBlockInteractionRange(pos, 4.0), true);
    }
}
