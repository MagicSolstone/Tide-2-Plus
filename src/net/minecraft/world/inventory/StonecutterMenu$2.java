package net.minecraft.world.inventory;

import java.util.List;
import java.util.Objects;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

class StonecutterMenu$2 extends Slot {
   StonecutterMenu$2(final StonecutterMenu this$0, final Container container, final int slot, final int x, final int y, final ContainerLevelAccess val$access) {
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
      carried.onCraftedBy(player, carried.getCount());
      this.this$0.resultContainer.awardUsedRecipes(player, this.getRelevantItems());
      ItemStack remaining = this.this$0.inputSlot.remove(1);
      if (!remaining.isEmpty()) {
         this.this$0.setupResultSlot(this.this$0.selectedRecipeIndex.get());
      }

      this.val$access.execute((level, pos) -> {
         long gameTime = level.getGameTime();
         if (this.this$0.lastSoundTime != gameTime) {
            level.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
            this.this$0.lastSoundTime = gameTime;
         }
      });
      super.onTake(player, carried);
   }

   private List<ItemStack> getRelevantItems() {
      return List.of(this.this$0.inputSlot.getItem());
   }
}
