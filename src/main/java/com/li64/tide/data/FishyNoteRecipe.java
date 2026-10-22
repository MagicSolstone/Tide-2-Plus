package com.li64.tide.data;

import com.li64.tide.registries.items.FishyNoteItem;
import com.li64.tide.util.TideUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.HolderLookup;

public class FishyNoteRecipe extends CustomRecipe {
    public static final FishyNoteRecipe INSTANCE = new FishyNoteRecipe();
    public static final com.mojang.serialization.MapCodec<FishyNoteRecipe> MAP_CODEC = com.mojang.serialization.MapCodec.unit(INSTANCE);
    public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, FishyNoteRecipe> STREAM_CODEC =
            net.minecraft.network.codec.StreamCodec.unit(INSTANCE);
    public static final RecipeSerializer<FishyNoteRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    public FishyNoteRecipe() {}

    private ItemStack getFishToConvert(CraftingInput input) {
        ItemStack stack1 = null, stack2 = null;

        for (int i = 0; i < input.size(); i++) {
            ItemStack inSlot = input.getItem(i);
            if (inSlot.isEmpty()) continue;
            if (stack1 == null) stack1 = inSlot;
            else {
                if (stack2 != null) return null;
                stack2 = inSlot;
            }
        }

        return stack1 != null && stack2 != null ? getFishFromInputs(stack1, stack2) : null;
    }

    private static ItemStack getFishFromInputs(ItemStack stack1, ItemStack stack2) {
        if (TideUtils.isJournalFish(stack1) && stack2.is(Items.PAPER)) return stack1;
        if (TideUtils.isJournalFish(stack2) && stack1.is(Items.PAPER)) return stack2;
        return null;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return getFishToConvert(input) != null;
    }

    @Override
    public @NotNull ItemStack assemble(CraftingInput input) {
        ItemStack fish = getFishToConvert(input);
        if (fish == null || fish.isEmpty()) return ItemStack.EMPTY;
        return FishyNoteItem.create(new ItemStack(fish.getItem()));
    }

    @Override
    public @NotNull RecipeSerializer<FishyNoteRecipe> getSerializer() {
        return SERIALIZER;
    }
}
