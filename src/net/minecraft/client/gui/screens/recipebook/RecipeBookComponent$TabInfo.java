package net.minecraft.client.gui.screens.recipebook;

import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.ExtendedRecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeBookCategory;

@Environment(EnvType.CLIENT)
public record RecipeBookComponent$TabInfo(ItemStack primaryIcon, Optional<ItemStack> secondaryIcon, ExtendedRecipeBookCategory category) {
   public RecipeBookComponent$TabInfo(final SearchRecipeBookCategory category) {
      this(new ItemStack(Items.COMPASS), Optional.empty(), category);
   }

   public RecipeBookComponent$TabInfo(final Item icon, final RecipeBookCategory category) {
      this(new ItemStack(icon), Optional.empty(), category);
   }

   public RecipeBookComponent$TabInfo(final Item primaryIcon, final Item secondaryIcon, final RecipeBookCategory category) {
      this(new ItemStack(primaryIcon), Optional.of(new ItemStack(secondaryIcon)), category);
   }
}
