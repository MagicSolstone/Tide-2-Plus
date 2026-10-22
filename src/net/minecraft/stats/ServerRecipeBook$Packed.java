package net.minecraft.stats;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;

public record ServerRecipeBook$Packed(RecipeBookSettings settings, List<ResourceKey<Recipe<?>>> known, List<ResourceKey<Recipe<?>>> highlight) {
   public static final Codec<ServerRecipeBook$Packed> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            RecipeBookSettings.MAP_CODEC.forGetter(ServerRecipeBook$Packed::settings),
            Recipe.KEY_CODEC.listOf().fieldOf("recipes").forGetter(ServerRecipeBook$Packed::known),
            Recipe.KEY_CODEC.listOf().fieldOf("toBeDisplayed").forGetter(ServerRecipeBook$Packed::highlight)
         )
         .apply(i, ServerRecipeBook$Packed::new)
   );
}
