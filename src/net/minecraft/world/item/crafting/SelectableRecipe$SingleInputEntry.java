package net.minecraft.world.item.crafting;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record SelectableRecipe$SingleInputEntry<T extends Recipe<?>>(Ingredient input, SelectableRecipe<T> recipe) {
   public static <T extends Recipe<?>> StreamCodec<RegistryFriendlyByteBuf, SelectableRecipe$SingleInputEntry<T>> noRecipeCodec() {
      return StreamCodec.composite(
         Ingredient.CONTENTS_STREAM_CODEC,
         SelectableRecipe$SingleInputEntry::input,
         SelectableRecipe.noRecipeCodec(),
         SelectableRecipe$SingleInputEntry::recipe,
         SelectableRecipe$SingleInputEntry::new
      );
   }
}
