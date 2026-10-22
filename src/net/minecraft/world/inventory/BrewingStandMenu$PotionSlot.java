package net.minecraft.world.inventory;

import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.PotionIngredient;
import net.minecraft.world.item.crafting.RecipeAccess;

class BrewingStandMenu$PotionSlot extends Slot {
   private final RecipeAccess recipeAccess;

   public BrewingStandMenu$PotionSlot(final RecipeAccess recipeAccess, final Container container, final int slot, final int x, final int y) {
      super(container, slot, x, y);
      this.recipeAccess = recipeAccess;
   }

   @Override
   public boolean mayPlace(final ItemStack itemStack) {
      return PotionIngredient.isPotionInput(itemStack, this.recipeAccess);
   }

   @Override
   public int getMaxStackSize() {
      return 1;
   }

   @Override
   public void onTake(final Player player, final ItemStack carried) {
      PotionContents potionContents = carried.get(DataComponents.POTION_CONTENTS);
      if (potionContents != null && player instanceof ServerPlayer serverPlayer) {
         CriteriaTriggers.BREWED_POTION.trigger(serverPlayer, potionContents);
      }

      super.onTake(player, carried);
   }

   @Override
   public Identifier getNoItemIcon() {
      return BrewingStandMenu.EMPTY_SLOT_POTION;
   }
}
