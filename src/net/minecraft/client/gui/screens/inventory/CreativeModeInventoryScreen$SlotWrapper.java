package net.minecraft.client.gui.screens.inventory;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class CreativeModeInventoryScreen$SlotWrapper extends Slot {
   private final Slot target;

   public CreativeModeInventoryScreen$SlotWrapper(final Slot target, final int index, final int x, final int y) {
      super(target.container, index, x, y);
      this.target = target;
   }

   @Override
   public void onTake(final Player player, final ItemStack carried) {
      this.target.onTake(player, carried);
   }

   @Override
   public boolean mayPlace(final ItemStack itemStack) {
      return this.target.mayPlace(itemStack);
   }

   @Override
   public ItemStack getItem() {
      return this.target.getItem();
   }

   @Override
   public boolean hasItem() {
      return this.target.hasItem();
   }

   @Override
   public void setByPlayer(final ItemStack itemStack, final ItemStack previous) {
      this.target.setByPlayer(itemStack, previous);
   }

   @Override
   public void set(final ItemStack itemStack) {
      this.target.set(itemStack);
   }

   @Override
   public void setChanged() {
      this.target.setChanged();
   }

   @Override
   public int getMaxStackSize() {
      return this.target.getMaxStackSize();
   }

   @Override
   public int getMaxStackSize(final ItemStack itemStack) {
      return this.target.getMaxStackSize(itemStack);
   }

   @Nullable
   @Override
   public Identifier getNoItemIcon() {
      return this.target.getNoItemIcon();
   }

   @Override
   public ItemStack remove(final int amount) {
      return this.target.remove(amount);
   }

   @Override
   public boolean isActive() {
      return this.target.isActive();
   }

   @Override
   public boolean mayPickup(final Player player) {
      return this.target.mayPickup(player);
   }
}
