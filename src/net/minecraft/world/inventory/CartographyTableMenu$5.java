package net.minecraft.world.inventory;

import java.util.Objects;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

class CartographyTableMenu$5 extends Slot {
   CartographyTableMenu$5(
      final CartographyTableMenu this$0, final Container container, final int slot, final int x, final int y, final ContainerLevelAccess val$access
   ) {
      this.val$access = val$accessx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(container, slot, x, y);
   }

   @Override
   public boolean mayPlace(final ItemStack itemStack) {
      return false;
   }

   @Override
   public void onTake(final Player player, final ItemStack carried) {
      this.this$0.slots.get(0).remove(1);
      this.this$0.slots.get(1).remove(1);
      carried.getItem().onCraftedBy(carried, player);
      this.val$access.execute((level, pos) -> {
         long gameTime = level.getGameTime();
         if (this.this$0.lastSoundTime != gameTime) {
            level.playSound(null, pos, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
            this.this$0.lastSoundTime = gameTime;
         }
      });
      super.onTake(player, carried);
   }

   @Override
   public ItemStack safeClone(final Player player) {
      ItemStack result = super.safeClone(player);
      result.getItem().onCraftedBy(result, player);
      return result;
   }
}
