package com.li64.tide.registries.items;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * A screen for an item that holds items (tackle box, fish satchel): its own slots first, then the player's
 * inventory. Every change is written back into the held item; the open item itself can't be moved.
 */
public abstract class HeldStorageMenu extends AbstractContainerMenu {
    protected final ItemStack holder;
    protected final SimpleContainer storage;
    private final int storageSlots;

    protected HeldStorageMenu(MenuType<?> type, int id, Inventory inventory, ItemStack holder, int size, int invLeft, int invTop) {
        super(type, id);
        this.holder = holder;
        this.storage = new SimpleContainer(size) {
            @Override
            public void setChanged() {
                super.setChanged();
                save(HeldStorageMenu.this.holder, this);
            }
        };
        load(holder, storage);
        addStorageSlots(storage);
        this.storageSlots = this.slots.size();
        this.addStandardInventorySlots(inventory, invLeft, invTop);
    }

    /** Lay out the item's own slots (called once, before the player's inventory). */
    protected abstract void addStorageSlots(SimpleContainer storage);

    protected abstract void load(ItemStack holder, SimpleContainer into);

    protected abstract void save(ItemStack holder, SimpleContainer from);

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack stack = slot.getItem(), copy = stack.copy();
        if (index < storageSlots) {
            if (!this.moveItemStackTo(stack, storageSlots, this.slots.size(), true)) return ItemStack.EMPTY;
        } else if (!this.moveItemStackTo(stack, 0, storageSlots, false)) return ItemStack.EMPTY;
        if (stack.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
        else slot.setChanged();
        return copy;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return holder.isEmpty() || player.getMainHandItem() == holder || player.getOffhandItem() == holder;
    }

    @Override
    public void clicked(int slot, int button, @NotNull ContainerInput input, @NotNull Player player) {
        // the open item itself can't be moved (or it could be emptied twice)
        if (!holder.isEmpty() && slot >= 0 && slot < this.slots.size() && this.slots.get(slot).getItem() == holder) return;
        if (!holder.isEmpty() && input == ContainerInput.SWAP && (player.getInventory().getItem(button) == holder
                || (button == 40 && player.getOffhandItem() == holder))) return;
        super.clicked(slot, button, input, player);
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        save(holder, storage);
        syncHeld(player, holder);
    }

    /**
     * Send the held item (a tackle box, say) to the client as it is now, when its screen closes. Otherwise the client
     * could still have an older copy (always, if it's in the off hand, which these screens don't show), and in creative
     * mode the inventory screen sends the client's copy back to the server, losing whatever had been put in it.
     */
    static void syncHeld(Player player, ItemStack held) {
        if (!(player instanceof net.minecraft.server.level.ServerPlayer sp) || held.isEmpty()) return;
        var inv = sp.getInventory();
        for (int i = 0; i < inv.getContainerSize(); i++)
            if (inv.getItem(i) == held) { sp.connection.send(inv.createInventoryUpdatePacket(i)); return; }
    }
}
