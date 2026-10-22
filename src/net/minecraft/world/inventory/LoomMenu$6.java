package net.minecraft.world.inventory;

import java.util.Objects;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

class LoomMenu$6 extends Slot {
   LoomMenu$6(final LoomMenu this$0, final Container container, final int slot, final int x, final int y, final ContainerLevelAccess val$access) {
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
      this.this$0.bannerSlot.remove(1);
      this.this$0.dyeSlot.remove(1);
      if (!this.this$0.bannerSlot.hasItem() || !this.this$0.dyeSlot.hasItem()) {
         this.this$0.selectedBannerPatternIndex.set(-1);
      }

      this.val$access.execute((level, pos) -> {
         long gameTime = level.getGameTime();
         if (this.this$0.lastSoundTime != gameTime) {
            level.playSound(null, pos, SoundEvents.UI_LOOM_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
            this.this$0.lastSoundTime = gameTime;
         }
      });
      super.onTake(player, carried);
   }
}
