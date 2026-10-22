package net.minecraft.world.item.crafting;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record CraftingRecipe$CraftingBookInfo(CraftingBookCategory category, String group) implements Recipe$BookInfo<CraftingBookCategory> {
   public static final MapCodec<CraftingRecipe$CraftingBookInfo> MAP_CODEC = Recipe$BookInfo.mapCodec(
      CraftingBookCategory.CODEC, CraftingBookCategory.MISC, CraftingRecipe$CraftingBookInfo::new
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, CraftingRecipe$CraftingBookInfo> STREAM_CODEC = Recipe$BookInfo.streamCodec(
      CraftingBookCategory.STREAM_CODEC, CraftingRecipe$CraftingBookInfo::new
   );
}
