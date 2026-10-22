package net.minecraft.world.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public interface Recipe$BookInfo<CategoryType> {
   CategoryType category();

   String group();

   static <CategoryType, SelfType extends Recipe$BookInfo<CategoryType>> MapCodec<SelfType> mapCodec(
      final Codec<CategoryType> categoryCodec, final CategoryType defaultCategory, final Recipe$BookInfo$Constructor<CategoryType, SelfType> constructor
   ) {
      return RecordCodecBuilder.mapCodec(
         i -> i.group(
               categoryCodec.optionalFieldOf("category", defaultCategory).forGetter(Recipe$BookInfo::category),
               Codec.STRING.optionalFieldOf("group", "").forGetter(Recipe$BookInfo::group)
            )
            .apply(i, constructor)
      );
   }

   static <CategoryType, SelfType extends Recipe$BookInfo<CategoryType>> StreamCodec<RegistryFriendlyByteBuf, SelfType> streamCodec(
      final StreamCodec<? super RegistryFriendlyByteBuf, CategoryType> categoryCodec, final Recipe$BookInfo$Constructor<CategoryType, SelfType> constructor
   ) {
      return StreamCodec.composite(categoryCodec, Recipe$BookInfo::category, ByteBufCodecs.STRING_UTF8, Recipe$BookInfo::group, constructor);
   }
}
