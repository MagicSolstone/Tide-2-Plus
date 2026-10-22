package net.minecraft.world.item.crafting;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record AbstractCookingRecipe$CookingBookInfo(CookingBookCategory category, String group) implements Recipe$BookInfo<CookingBookCategory> {
   public static final MapCodec<AbstractCookingRecipe$CookingBookInfo> MAP_CODEC = Recipe$BookInfo.mapCodec(
      CookingBookCategory.CODEC, CookingBookCategory.MISC, AbstractCookingRecipe$CookingBookInfo::new
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, AbstractCookingRecipe$CookingBookInfo> STREAM_CODEC = Recipe$BookInfo.streamCodec(
      CookingBookCategory.STREAM_CODEC, AbstractCookingRecipe$CookingBookInfo::new
   );
}
