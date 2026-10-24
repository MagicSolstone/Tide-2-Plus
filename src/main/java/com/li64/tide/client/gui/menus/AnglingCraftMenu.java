package com.li64.tide.client.gui.menus;

import com.li64.tide.client.gui.TideMenuTypes;
import com.li64.tide.registries.blocks.AnglingTableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The Angling Table's Craft tab: just the player's inventory. The screen lists what the table can make, and the
 * Craft button asks the server (TackleCraftMsg) to make it from what's in the inventory.
 */
public class AnglingCraftMenu extends AbstractContainerMenu {
    /** Top of the inventory rows in the screen. */
    public static final int INV_X = 48, INV_Y = 140;
    private static final Map<UUID, Boolean> LAST_TAB = new ConcurrentHashMap<>();
    private final ContainerLevelAccess access;

    public AnglingCraftMenu(int id, Inventory inventory) {
        this(id, inventory, ContainerLevelAccess.NULL);
    }

    public AnglingCraftMenu(int id, Inventory inventory, ContainerLevelAccess access) {
        super(TideMenuTypes.ANGLING_CRAFT, id);
        this.access = access;
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                this.addSlot(new Slot(inventory, col + row * 9 + 9, INV_X + col * 18, INV_Y + row * 18));
        for (int col = 0; col < 9; col++) this.addSlot(new Slot(inventory, col, INV_X + col * 18, INV_Y + 58));
    }

    public ContainerLevelAccess access() {
        return access;
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

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        // shift-click moves between the hotbar and the rest of the inventory, like a crafting table does
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack stack = slot.getItem(), copy = stack.copy();
        boolean hotbar = index >= 27;
        if (!this.moveItemStackTo(stack, hotbar ? 0 : 27, hotbar ? 27 : 36, false)) return ItemStack.EMPTY;
        if (stack.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
        else slot.setChanged();
        return copy;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return this.access.evaluate((level, pos) -> level.getBlockState(pos).getBlock() instanceof AnglingTableBlock
                && player.isWithinBlockInteractionRange(pos, 4.0), true);
    }
}
