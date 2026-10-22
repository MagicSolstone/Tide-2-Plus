package net.minecraft.world.level.block.entity.vault;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

enum VaultState$2 {
   private VaultState$2(final String stateName, final VaultState$LightLevel lightLevel) {
   }

   @Override
   protected void onEnter(
      final ServerLevel serverLevel, final BlockPos pos, final VaultConfig config, final VaultSharedData sharedData, final boolean isOminous
   ) {
      if (!sharedData.hasDisplayItem()) {
         VaultBlockEntity$Server.cycleDisplayItemFromLootTable(serverLevel, this, config, sharedData, pos);
      }

      serverLevel.levelEvent(3015, pos, isOminous ? 1 : 0);
   }
}
