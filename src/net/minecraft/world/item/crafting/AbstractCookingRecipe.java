package net.minecraft.world.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.display.FurnaceRecipeDisplay;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay$AnyFuel;
import net.minecraft.world.item.crafting.display.SlotDisplay$ItemSlotDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay$ItemStackSlotDisplay;

public abstract class AbstractCookingRecipe extends SingleItemRecipe {
   protected final AbstractCookingRecipe$CookingBookInfo bookInfo;
   private final float experience;
   private final int cookingTime;

   public AbstractCookingRecipe(
      final Recipe$CommonInfo commonInfo,
      final AbstractCookingRecipe$CookingBookInfo bookInfo,
      final Ingredient ingredient,
      final ItemStackTemplate result,
      final float experience,
      final int cookingTime
   ) {
      super(commonInfo, ingredient, result);
      this.bookInfo = bookInfo;
      this.experience = experience;
      this.cookingTime = cookingTime;
   }

   @Override
   public abstract RecipeSerializer<? extends AbstractCookingRecipe> getSerializer();

   @Override
   public abstract RecipeType<? extends AbstractCookingRecipe> getType();

   public float experience() {
      return this.experience;
   }

   public int cookingTime() {
      return this.cookingTime;
   }

   public CookingBookCategory category() {
      return this.bookInfo.category;
   }

   @Override
   public String group() {
      return this.bookInfo.group;
   }

   protected abstract Item furnaceIcon();

   @Override
   public List<RecipeDisplay> display() {
      return List.of(
         new FurnaceRecipeDisplay(
            this.input().display(),
            SlotDisplay$AnyFuel.INSTANCE,
            new SlotDisplay$ItemStackSlotDisplay(this.result()),
            new SlotDisplay$ItemSlotDisplay(this.furnaceIcon()),
            this.cookingTime,
            this.experience
         )
      );
   }

   public static <T extends AbstractCookingRecipe> MapCodec<T> cookingMapCodec(final AbstractCookingRecipe$Factory<T> factory) {
      return RecordCodecBuilder.mapCodec(
         i -> i.group(
               Recipe$CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
               AbstractCookingRecipe$CookingBookInfo.MAP_CODEC.forGetter(o -> o.bookInfo),
               Ingredient.CODEC.fieldOf("ingredient").forGetter(SingleItemRecipe::input),
               ItemStackTemplate.CODEC.fieldOf("result").forGetter(SingleItemRecipe::result),
               Codec.FLOAT.optionalFieldOf("experience", 0.0F).forGetter(AbstractCookingRecipe::experience),
               Codec.INT.fieldOf("cookingtime").forGetter(AbstractCookingRecipe::cookingTime)
            )
            .apply(i, factory::create)
      );
   }

   public static <T extends AbstractCookingRecipe> StreamCodec<RegistryFriendlyByteBuf, T> cookingStreamCodec(final AbstractCookingRecipe$Factory<T> factory) {
      return StreamCodec.composite(
         Recipe$CommonInfo.STREAM_CODEC,
         o -> o.commonInfo,
         AbstractCookingRecipe$CookingBookInfo.STREAM_CODEC,
         o -> o.bookInfo,
         Ingredient.CONTENTS_STREAM_CODEC,
         SingleItemRecipe::input,
         ItemStackTemplate.STREAM_CODEC,
         SingleItemRecipe::result,
         ByteBufCodecs.FLOAT,
         AbstractCookingRecipe::experience,
         ByteBufCodecs.INT,
         AbstractCookingRecipe::cookingTime,
         factory::create
      );
   }
}
