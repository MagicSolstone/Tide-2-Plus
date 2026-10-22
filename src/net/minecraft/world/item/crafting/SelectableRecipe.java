package net.minecraft.world.item.crafting;

import java.util.Optional;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.display.SlotDisplay;

public record SelectableRecipe<T extends Recipe<?>>(SlotDisplay optionDisplay, Optional<RecipeHolder<T>> recipe) {
   public static <T extends Recipe<?>> StreamCodec<RegistryFriendlyByteBuf, SelectableRecipe<T>> noRecipeCodec() {
      return StreamCodec.composite(
         SlotDisplay.STREAM_CODEC, SelectableRecipe::optionDisplay, slotDisplay -> new SelectableRecipe<>(slotDisplay, Optional.empty())
      );
   }
}
