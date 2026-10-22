package net.minecraft.world.level.block.entity.vault;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

enum VaultState$1 {
   private VaultState$1(final String stateName, final VaultState$LightLevel lightLevel) {
   }

   @Override
   protected void onEnter(
      final ServerLevel serverLevel, final BlockPos pos, final VaultConfig config, final VaultSharedData sharedData, final boolean isOminous
   ) {
      sharedData.setDisplayItem(ItemStack.EMPTY);
      serverLevel.levelEvent(3016, pos, isOminous ? 1 : 0);
   }
}
