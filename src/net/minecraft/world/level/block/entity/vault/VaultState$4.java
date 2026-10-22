package net.minecraft.world.level.block.entity.vault;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

enum VaultState$4 {
   private VaultState$4(final String stateName, final VaultState$LightLevel lightLevel) {
   }

   @Override
   protected void onEnter(
      final ServerLevel serverLevel, final BlockPos pos, final VaultConfig config, final VaultSharedData sharedData, final boolean isOminous
   ) {
      serverLevel.playSound(null, pos, SoundEvents.VAULT_OPEN_SHUTTER, SoundSource.BLOCKS);
   }

   @Override
   protected void onExit(final ServerLevel serverLevel, final BlockPos pos, final VaultConfig config, final VaultSharedData sharedData) {
      serverLevel.playSound(null, pos, SoundEvents.VAULT_CLOSE_SHUTTER, SoundSource.BLOCKS);
   }
}
