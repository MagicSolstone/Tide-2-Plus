package net.minecraft.world.item.crafting;

import java.util.List;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record SelectableRecipe$SingleInputSet<T extends Recipe<?>>(List<SelectableRecipe$SingleInputEntry<T>> entries) {
   public static <T extends Recipe<?>> SelectableRecipe$SingleInputSet<T> empty() {
      return new SelectableRecipe$SingleInputSet<>(List.of());
   }

   public static <T extends Recipe<?>> StreamCodec<RegistryFriendlyByteBuf, SelectableRecipe$SingleInputSet<T>> noRecipeCodec() {
      return StreamCodec.composite(
         SelectableRecipe$SingleInputEntry.<T>noRecipeCodec().apply(ByteBufCodecs.list()),
         SelectableRecipe$SingleInputSet::entries,
         SelectableRecipe$SingleInputSet::new
      );
   }

   public boolean acceptsInput(final ItemStack input) {
      return this.entries.stream().anyMatch(e -> e.input.test(input));
   }

   public SelectableRecipe$SingleInputSet<T> selectByInput(final ItemStack input) {
      return new SelectableRecipe$SingleInputSet<>(this.entries.stream().filter(e -> e.input.test(input)).toList());
   }

   public boolean isEmpty() {
      return this.entries.isEmpty();
   }

   public int size() {
      return this.entries.size();
   }
}
