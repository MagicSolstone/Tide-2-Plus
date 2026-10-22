package net.minecraft.world.item;

import com.mojang.datafixers.util.Pair;
import java.util.function.Function;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopperCollection;

record HoneycombItem$WaxedRecipeGroup(WeatheringCopperCollection<Block> block, Function<Block, Pair<RecipeCategory, String>> recipeIdProvider) {
}
