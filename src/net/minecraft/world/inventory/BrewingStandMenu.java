package net.minecraft.world.inventory;

import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.PotionIngredient;
import net.minecraft.world.item.crafting.RecipeAccess;
import net.minecraft.world.item.crafting.RecipePropertySet;

public class BrewingStandMenu extends AbstractContainerMenu {
   private static final Identifier EMPTY_SLOT_FUEL = Identifier.withDefaultNamespace("container/slot/brewing_fuel");
   private static final Identifier EMPTY_SLOT_POTION = Identifier.withDefaultNamespace("container/slot/potion");
   private static final int BOTTLE_SLOT_START = 0;
   private static final int BOTTLE_SLOT_END = 2;
   private static final int INGREDIENT_SLOT = 3;
   private static final int FUEL_SLOT = 4;
   private static final int SLOT_COUNT = 5;
   private static final int DATA_COUNT = 4;
   private static final int INV_SLOT_START = 5;
   private static final int INV_SLOT_END = 32;
   private static final int USE_ROW_SLOT_START = 32;
   private static final int USE_ROW_SLOT_END = 41;
   private final Container brewingStand;
   private final ContainerData brewingStandData;
   private final Slot ingredientSlot;

   public BrewingStandMenu(final int containerId, final Inventory inventory) {
      this(containerId, inventory, new SimpleContainer(5), new SimpleContainerData(4));
   }

   public BrewingStandMenu(final int containerId, final Inventory inventory, final Container brewingStand, final ContainerData brewingStandData) {
      super(MenuType.BREWING_STAND, containerId);
      checkContainerSize(brewingStand, 5);
      checkContainerDataCount(brewingStandData, 4);
      this.brewingStand = brewingStand;
      this.brewingStandData = brewingStandData;
      RecipeAccess recipeAccess = inventory.player.level().recipeAccess();
      this.addSlot(new BrewingStandMenu$PotionSlot(recipeAccess, brewingStand, 0, 56, 51));
      this.addSlot(new BrewingStandMenu$PotionSlot(recipeAccess, brewingStand, 1, 79, 58));
      this.addSlot(new BrewingStandMenu$PotionSlot(recipeAccess, brewingStand, 2, 102, 51));
      this.ingredientSlot = this.addSlot(
         new BrewingStandMenu$IngredientsSlot(recipeAccess.propertySet(RecipePropertySet.BREWING_REAGENTS), brewingStand, 3, 79, 17)
      );
      this.addSlot(new BrewingStandMenu$FuelSlot(brewingStand, 4, 17, 17));
      this.addDataSlots(brewingStandData);
      this.addStandardInventorySlots(inventory, 8, 84);
   }

   @Override
   public boolean stillValid(final Player player) {
      return this.brewingStand.stillValid(player);
   }

   @Override
   public ItemStack quickMoveStack(final Player player, final int slotIndex) {
      ItemStack clicked = ItemStack.EMPTY;
      Slot slot = this.slots.get(slotIndex);
      RecipeAccess recipeAccess = player.level().recipeAccess();
      if (slot != null && slot.hasItem()) {
         ItemStack stack = slot.getItem();
         clicked = stack.copy();
         if ((slotIndex < 0 || slotIndex > 2) && slotIndex != 3 && slotIndex != 4) {
            if (BrewingStandMenu$FuelSlot.mayPlaceItem(clicked)) {
               if (this.moveItemStackTo(stack, 4, 5, false) || this.ingredientSlot.mayPlace(stack) && !this.moveItemStackTo(stack, 3, 4, false)) {
                  return ItemStack.EMPTY;
               }
            } else if (this.ingredientSlot.mayPlace(stack)) {
               if (!this.moveItemStackTo(stack, 3, 4, false)) {
                  return ItemStack.EMPTY;
               }
            } else if (PotionIngredient.isPotionInput(clicked, recipeAccess)) {
               if (!this.moveItemStackTo(stack, 0, 3, false)) {
                  return ItemStack.EMPTY;
               }
            } else if (slotIndex >= 5 && slotIndex < 32) {
               if (!this.moveItemStackTo(stack, 32, 41, false)) {
                  return ItemStack.EMPTY;
               }
            } else if (slotIndex >= 32 && slotIndex < 41) {
               if (!this.moveItemStackTo(stack, 5, 32, false)) {
                  return ItemStack.EMPTY;
               }
            } else if (!this.moveItemStackTo(stack, 5, 41, false)) {
               return ItemStack.EMPTY;
            }
         } else {
            if (!this.moveItemStackTo(stack, 5, 41, true)) {
               return ItemStack.EMPTY;
            }

            slot.onQuickCraft(stack, clicked);
         }

         if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
         } else {
            slot.setChanged();
         }

         if (stack.getCount() == clicked.getCount()) {
            return ItemStack.EMPTY;
         }

         slot.onTake(player, clicked);
      }

      return clicked;
   }

   public int getFuel() {
      return this.brewingStandData.get(1);
   }

   public int getTotalFuel() {
      return this.brewingStandData.get(3);
   }

   public int getBrewingTicks() {
      return this.brewingStandData.get(0);
   }

   public int getTotalBrewingTicks() {
      return this.brewingStandData.get(2);
   }
}
