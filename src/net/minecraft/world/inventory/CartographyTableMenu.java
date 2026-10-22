package net.minecraft.world.inventory;

import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.component.MapPostProcessing;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class CartographyTableMenu extends AbstractContainerMenu {
   public static final int MAP_SLOT = 0;
   public static final int ADDITIONAL_SLOT = 1;
   public static final int RESULT_SLOT = 2;
   private static final int INV_SLOT_START = 3;
   private static final int INV_SLOT_END = 30;
   private static final int USE_ROW_SLOT_START = 30;
   private static final int USE_ROW_SLOT_END = 39;
   private final ContainerLevelAccess access;
   private long lastSoundTime;
   public final Container container = new CartographyTableMenu$1(this, 2);
   private final ResultContainer resultContainer = new CartographyTableMenu$2(this);

   public CartographyTableMenu(final int containerId, final Inventory inventory) {
      this(containerId, inventory, ContainerLevelAccess.NULL);
   }

   public CartographyTableMenu(final int containerId, final Inventory inventory, final ContainerLevelAccess access) {
      super(MenuType.CARTOGRAPHY_TABLE, containerId);
      this.access = access;
      this.addSlot(new CartographyTableMenu$3(this, this.container, 0, 15, 15));
      this.addSlot(new CartographyTableMenu$4(this, this.container, 1, 15, 52));
      this.addSlot(new CartographyTableMenu$5(this, this.resultContainer, 2, 145, 39, access));
      this.addStandardInventorySlots(inventory, 8, 84);
   }

   @Override
   public boolean stillValid(final Player player) {
      return stillValid(this.access, player, Blocks.CARTOGRAPHY_TABLE);
   }

   @Override
   public void slotsChanged(final Container container) {
      ItemStack mapStack = this.container.getItem(0);
      ItemStack additionalStack = this.container.getItem(1);
      ItemStack resultStack = this.resultContainer.getItem(2);
      if (resultStack.isEmpty() || !mapStack.isEmpty() && !additionalStack.isEmpty()) {
         if (!mapStack.isEmpty() && !additionalStack.isEmpty()) {
            this.setupResultSlot(mapStack, additionalStack, resultStack);
         }
      } else {
         this.resultContainer.removeItemNoUpdate(2);
      }
   }

   private void setupResultSlot(final ItemStack mapStack, final ItemStack additionalStack, final ItemStack resultStack) {
      this.access.execute((level, pos) -> {
         MapItemSavedData mapData = MapItem.getSavedData(mapStack, level);
         if (mapData != null) {
            ItemStack result;
            if (additionalStack.is(Items.PAPER) && mapStack.is(ItemTags.EXTENDABLE_MAPS) && !mapData.locked && mapData.scale < 4) {
               result = mapStack.copyWithCount(1);
               result.set(DataComponents.MAP_POST_PROCESSING, MapPostProcessing.SCALE);
               this.broadcastChanges();
            } else if (additionalStack.is(Items.GLASS_PANE) && !mapData.locked) {
               result = mapStack.copyWithCount(1);
               result.set(DataComponents.MAP_POST_PROCESSING, MapPostProcessing.LOCK);
               this.broadcastChanges();
            } else {
               if (!additionalStack.is(Items.MAP)) {
                  this.resultContainer.removeItemNoUpdate(2);
                  this.broadcastChanges();
                  return;
               }

               result = mapStack.copyWithCount(2);
               this.broadcastChanges();
            }

            if (!ItemStack.matches(result, resultStack)) {
               this.resultContainer.setItem(2, result);
               this.broadcastChanges();
            }
         }
      });
   }

   @Override
   public boolean canTakeItemForPickAll(final ItemStack carried, final Slot target) {
      return target.container != this.resultContainer && super.canTakeItemForPickAll(carried, target);
   }

   @Override
   public ItemStack quickMoveStack(final Player player, final int slotIndex) {
      ItemStack clicked = ItemStack.EMPTY;
      Slot slot = this.slots.get(slotIndex);
      if (slot != null && slot.hasItem()) {
         ItemStack stack = slot.getItem();
         clicked = stack.copy();
         if (slotIndex == 2) {
            stack.getItem().onCraftedBy(stack, player);
            if (!this.moveItemStackTo(stack, 3, 39, true)) {
               return ItemStack.EMPTY;
            }

            slot.onQuickCraft(stack, clicked);
         } else if (slotIndex != 1 && slotIndex != 0) {
            if (stack.has(DataComponents.MAP_ID)) {
               if (!this.moveItemStackTo(stack, 0, 1, false)) {
                  return ItemStack.EMPTY;
               }
            } else if (!stack.is(Items.PAPER) && !stack.is(Items.MAP) && !stack.is(Items.GLASS_PANE)) {
               if (slotIndex >= 3 && slotIndex < 30) {
                  if (!this.moveItemStackTo(stack, 30, 39, false)) {
                     return ItemStack.EMPTY;
                  }
               } else if (slotIndex >= 30 && slotIndex < 39 && !this.moveItemStackTo(stack, 3, 30, false)) {
                  return ItemStack.EMPTY;
               }
            } else if (!this.moveItemStackTo(stack, 1, 2, false)) {
               return ItemStack.EMPTY;
            }
         } else if (!this.moveItemStackTo(stack, 3, 39, false)) {
            return ItemStack.EMPTY;
         }

         if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
         }

         slot.setChanged();
         if (stack.getCount() == clicked.getCount()) {
            return ItemStack.EMPTY;
         }

         slot.onTake(player, stack);
         this.broadcastChanges();
      }

      return clicked;
   }

   @Override
   public void removed(final Player player) {
      super.removed(player);
      this.resultContainer.removeItemNoUpdate(2);
      this.access.execute((level, pos) -> this.clearContainer(player, this.container));
   }
}
