package net.minecraft.client.gui.screens.inventory;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class CreativeModeInventoryScreen$ItemPickerMenu extends AbstractContainerMenu {
   public final NonNullList<ItemStack> items = NonNullList.create();
   private final AbstractContainerMenu inventoryMenu;

   public CreativeModeInventoryScreen$ItemPickerMenu(final Player player) {
      super(null, 0);
      this.inventoryMenu = player.inventoryMenu;
      Inventory inventory = player.getInventory();

      for (int y = 0; y < 5; y++) {
         for (int x = 0; x < 9; x++) {
            this.addSlot(new CreativeModeInventoryScreen$CustomCreativeSlot(CreativeModeInventoryScreen.CONTAINER, y * 9 + x, 9 + x * 18, 18 + y * 18));
         }
      }

      this.addInventoryHotbarSlots(inventory, 9, 112);
      this.scrollTo(0.0F);
   }

   @Override
   public boolean stillValid(final Player player) {
      return true;
   }

   protected int calculateRowCount() {
      return Mth.positiveCeilDiv(this.items.size(), 9) - 5;
   }

   protected int getRowIndexForScroll(final float scrollOffs) {
      return Math.max((int)(scrollOffs * this.calculateRowCount() + 0.5), 0);
   }

   protected float getScrollForRowIndex(final int rowIndex) {
      return Mth.clamp((float)rowIndex / this.calculateRowCount(), 0.0F, 1.0F);
   }

   protected float subtractInputFromScroll(final float scrollOffs, final double input) {
      return Mth.clamp(scrollOffs - (float)(input / this.calculateRowCount()), 0.0F, 1.0F);
   }

   public void scrollTo(final float scrollOffs) {
      int rowToScrollTo = this.getRowIndexForScroll(scrollOffs);

      for (int y = 0; y < 5; y++) {
         for (int x = 0; x < 9; x++) {
            int slot = x + (y + rowToScrollTo) * 9;
            if (slot >= 0 && slot < this.items.size()) {
               CreativeModeInventoryScreen.CONTAINER.setItem(x + y * 9, this.items.get(slot));
            } else {
               CreativeModeInventoryScreen.CONTAINER.setItem(x + y * 9, ItemStack.EMPTY);
            }
         }
      }
   }

   public boolean canScroll() {
      return this.items.size() > 45;
   }

   @Override
   public ItemStack quickMoveStack(final Player player, final int slotIndex) {
      if (slotIndex >= this.slots.size() - 9 && slotIndex < this.slots.size()) {
         Slot slot = this.slots.get(slotIndex);
         if (slot != null && slot.hasItem()) {
            slot.setByPlayer(ItemStack.EMPTY);
         }
      }

      return ItemStack.EMPTY;
   }

   @Override
   public boolean canTakeItemForPickAll(final ItemStack carried, final Slot target) {
      return target.container != CreativeModeInventoryScreen.CONTAINER;
   }

   @Override
   public boolean canDragTo(final Slot slot) {
      return slot.container != CreativeModeInventoryScreen.CONTAINER;
   }

   @Override
   public ItemStack getCarried() {
      return this.inventoryMenu.getCarried();
   }

   @Override
   public void setCarried(final ItemStack carried) {
      this.inventoryMenu.setCarried(carried);
   }
}
