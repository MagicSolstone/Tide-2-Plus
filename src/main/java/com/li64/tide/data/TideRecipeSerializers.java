package com.li64.tide.data;

import com.li64.tide.Tide;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class TideRecipeSerializers {
    public static final RecipeSerializer<FishyNoteRecipe> FISHY_NOTE_RECIPE = Tide.PLATFORM.register(
            BuiltInRegistries.RECIPE_SERIALIZER, Tide.resource("fishy_note_recipe"), FishyNoteRecipe.SERIALIZER);

    public static void init() {}
}
