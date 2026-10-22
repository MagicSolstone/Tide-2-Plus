package com.li64.tide.data.item;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record SatchelContents(List<ItemStack> items) {
    public static final Codec<SatchelContents> CODEC = ItemStack.CODEC.listOf().xmap(SatchelContents::new, contents -> contents.items);
    public static final StreamCodec<RegistryFriendlyByteBuf, SatchelContents> STREAM_CODEC = ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()).map(SatchelContents::new, contents -> contents.items);
    public static final int MAX_STACKS = 64;

    public SatchelContents() {
        this(List.of());
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
        //noinspection DeconstructionCanBeUsed
        if (other instanceof SatchelContents contents)
            return ItemStack.listMatches(this.items, contents.items);

        return false;
    }

    @SuppressWarnings("deprecation")
    public int hashCode() {
        return ItemStack.hashStackList(this.items);
    }

    public String toString() {
        return "SatchelContents" + this.items;
    }

    public static class Mutable {
        private final List<ItemStack> items;

        public Mutable(SatchelContents contents) {
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
        public boolean tryInsert(ItemStack stack) {
            if (stack.isEmpty() || !stack.getItem().canFitInsideContainerItems()) return false;
            if (this.items.size() >= MAX_STACKS) return false;
            this.items.add(stack.split(1));
            return true;
        }

        // transfer items from slot to contents
        public boolean tryTransfer(Slot slot, Player player) {
            ItemStack stack = slot.safeTake(1, slot.getMaxStackSize(), player);
            return this.tryInsert(stack);
        }

        @Nullable
        public ItemStack removeStack() {
            if (this.items.isEmpty()) return null;
            return this.items.remove(0).copy();
        }

        public SatchelContents toImmutable() {
            return new SatchelContents(List.copyOf(this.items));
        }

        public boolean isEmpty() {
            return this.items.isEmpty();
        }
    }
}