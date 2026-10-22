package net.minecraft.world.item.crafting;

import com.mojang.serialization.Codec;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.level.Level;

public interface Recipe<T extends RecipeInput> {
   Codec<Recipe<?>> DIRECT_CODEC = BuiltInRegistries.RECIPE_SERIALIZER.byNameCodec().dispatch(Recipe::getSerializer, RecipeSerializer::codec);
   Codec<ResourceKey<Recipe<?>>> KEY_CODEC = ResourceKey.codec(Registries.RECIPE);
   StreamCodec<RegistryFriendlyByteBuf, Recipe<?>> STREAM_CODEC = ByteBufCodecs.registry(Registries.RECIPE_SERIALIZER)
      .dispatch(Recipe::getSerializer, RecipeSerializer::streamCodec);
   Codec<Holder<Recipe<?>>> CODEC = RegistryCodecs.holder(Registries.RECIPE, DIRECT_CODEC);
   Codec<HolderSet<Recipe<?>>> LIST_CODEC = RegistryCodecs.holderSet(Registries.RECIPE, DIRECT_CODEC);

   boolean matches(T input, Level level);

   ItemStack assemble(T input);

   default boolean isSpecial() {
      return false;
   }

   boolean showNotification();

   String group();

   RecipeSerializer<? extends Recipe<T>> getSerializer();

   RecipeType<? extends Recipe<T>> getType();

   PlacementInfo placementInfo();

   default List<RecipeDisplay> display() {
      return List.of();
   }

   RecipeBookCategory recipeBookCategory();
}
