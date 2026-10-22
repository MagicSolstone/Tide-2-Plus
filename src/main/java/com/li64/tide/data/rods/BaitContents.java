package com.li64.tide.data.rods;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BaitContents {
    public static final Codec<BaitContents> CODEC = ItemStack.CODEC.listOf().xmap(BaitContents::new, contents -> contents.items);
    public static final StreamCodec<RegistryFriendlyByteBuf, BaitContents> STREAM_CODEC = ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()).map(BaitContents::new, contents -> contents.items);

    private final List<ItemStack> items;

    public BaitContents() {
        this(List.of());
    }

    public BaitContents(List<ItemStack> items) {
        this.items = items;
    }

    public List<ItemStack> items() {
        return items;
    }

    public ItemStack get(int index) {
        return items().get(index);
    }

    public int size() {
        return this.items.size();
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    @SuppressWarnings("deprecation")
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other instanceof BaitContents contents)
            return ItemStack.listMatches(this.items, contents.items);
        return false;
    }

    @SuppressWarnings("deprecation")
    public int hashCode() {
        return ItemStack.hashStackList(this.items);
    }

    public String toString() {
        return "BaitContents" + this.items;
    }

    @SuppressWarnings("SimplifyStreamApiCallChains")
    public static class Mutable {
        private final List<ItemStack> items;

        public Mutable(BaitContents contents) {
            if (contents == null) this.items = new ArrayList<>();
            else this.items = new ArrayList<>(contents.items);
        }

        private int findStackIndex(ItemStack stack) {
            for (int i = 0; i < this.items.size(); i++) {
                if (ItemStack.isSameItemSameComponents(this.items.get(i), stack)) return i;
            }
            return -1;
        }

        // inserts stack to the contents
        public void tryInsert(ItemStack stack, int maxStacks) {
            if (stack.isEmpty() || !stack.getItem().canFitInsideContainerItems()) return;

            int index = this.findStackIndex(stack);
            int count = stack.getCount();

            if (index != -1 && stack.isStackable()) {
                ItemStack current = this.items.get(index);

                int stackSize = stack.getMaxStackSize();
                int amountToAdd = Math.min(stackSize - current.getCount(), stack.getCount());

                ItemStack added = current.copyWithCount(current.getCount() + amountToAdd);
                stack.shrink(amountToAdd);

                this.items.set(index, added);
            }
            else if (items.size() < maxStacks) this.items.add(stack.split(count));
        }

        // transfer items from slot to contents
        public void tryTransfer(Slot slot, Player player, int maxStacks) {
            ItemStack slotStack = slot.safeTake(slot.getItem().getCount(), slot.getMaxStackSize(), player);
            this.tryInsert(slotStack, maxStacks);
            if (!slotStack.isEmpty()) slot.safeInsert(slotStack);
        }

        @Nullable
        public ItemStack removeStack() {
            if (this.items.isEmpty()) return null;
            return this.items.removeFirst().copy();
        }

        public void shrinkStack(ItemStack stack) {
            if (stack == null || stack.isEmpty()) return;
            int index = findStackIndex(stack);
            if (index == -1) return;
            this.items.get(index).shrink(1);
        }

        public void shrinkAll() {
            this.items.stream().forEach(this::shrinkStack);
            this.items.removeIf(stack -> stack == null || stack.isEmpty());
        }

        public BaitContents toImmutable() {
            return new BaitContents(List.copyOf(this.items));
        }

        public boolean isEmpty() {
            return this.items.isEmpty();
        }
    }
}