package net.minecraft.world.level.block.entity.vault;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public enum VaultState implements StringRepresentable {
   INACTIVE("inactive", VaultState$LightLevel.HALF_LIT),
   ACTIVE("active", VaultState$LightLevel.LIT),
   UNLOCKING("unlocking", VaultState$LightLevel.LIT),
   EJECTING("ejecting", VaultState$LightLevel.LIT);

   private static final int UPDATE_CONNECTED_PLAYERS_TICK_RATE = 20;
   private static final int DELAY_BETWEEN_EJECTIONS_TICKS = 20;
   private static final int DELAY_AFTER_LAST_EJECTION_TICKS = 20;
   private static final int DELAY_BEFORE_FIRST_EJECTION_TICKS = 20;
   private final String stateName;
   private final VaultState$LightLevel lightLevel;

   private VaultState(final String stateName, final VaultState$LightLevel lightLevel) {
      this.stateName = stateName;
      this.lightLevel = lightLevel;
   }

   @Override
   public String getSerializedName() {
      return this.stateName;
   }

   public int lightLevel() {
      return this.lightLevel.value;
   }

   public VaultState tickAndGetNext(
      final ServerLevel serverLevel, final BlockPos pos, final VaultConfig config, final VaultServerData serverData, final VaultSharedData sharedData
   ) {
      return switch (this) {
         case INACTIVE -> updateStateForConnectedPlayers(serverLevel, pos, config, serverData, sharedData, config.activationRange());
         case ACTIVE -> updateStateForConnectedPlayers(serverLevel, pos, config, serverData, sharedData, config.deactivationRange());
         case UNLOCKING -> {
            serverData.pauseStateUpdatingUntil(serverLevel.getGameTime() + 20L);
            yield EJECTING;
         }
         case EJECTING -> {
            if (serverData.getItemsToEject().isEmpty()) {
               serverData.markEjectionFinished();
               yield updateStateForConnectedPlayers(serverLevel, pos, config, serverData, sharedData, config.deactivationRange());
            } else {
               float ejectionSoundProgress = serverData.ejectionProgress();
               ejectResultItem(serverLevel, pos, serverData.popNextItemToEject(), ejectionSoundProgress);
               sharedData.setDisplayItem(serverData.getNextItemToEject());
               boolean isLastEjection = serverData.getItemsToEject().isEmpty();
               int ejectionDelay = isLastEjection ? 20 : 20;
               serverData.pauseStateUpdatingUntil(serverLevel.getGameTime() + ejectionDelay);
               yield EJECTING;
            }
         }
      };
   }

   private static VaultState updateStateForConnectedPlayers(
      final ServerLevel serverLevel,
      final BlockPos pos,
      final VaultConfig config,
      final VaultServerData serverData,
      final VaultSharedData sharedData,
      final double activationRange
   ) {
      sharedData.updateConnectedPlayersWithinRange(serverLevel, pos, serverData, config, activationRange);
      serverData.pauseStateUpdatingUntil(serverLevel.getGameTime() + 20L);
      return sharedData.hasConnectedPlayers() ? ACTIVE : INACTIVE;
   }

   public void onTransition(
      final ServerLevel serverLevel,
      final BlockPos pos,
      final VaultState to,
      final VaultConfig config,
      final VaultSharedData sharedData,
      final boolean isOminous
   ) {
      this.onExit(serverLevel, pos, config, sharedData);
      to.onEnter(serverLevel, pos, config, sharedData, isOminous);
   }

   protected void onEnter(
      final ServerLevel serverLevel, final BlockPos pos, final VaultConfig config, final VaultSharedData sharedData, final boolean isOminous
   ) {
   }

   protected void onExit(final ServerLevel serverLevel, final BlockPos pos, final VaultConfig config, final VaultSharedData sharedData) {
   }

   private static void ejectResultItem(final ServerLevel serverLevel, final BlockPos pos, final ItemStack itemToEject, final float ejectionSoundProgress) {
      DefaultDispenseItemBehavior.spawnItem(serverLevel, itemToEject, 2, Direction.UP, Vec3.atBottomCenterOf(pos).relative(Direction.UP, 1.2));
      serverLevel.levelEvent(3017, pos, 0);
      serverLevel.playSound(null, pos, SoundEvents.VAULT_EJECT_ITEM, SoundSource.BLOCKS, 1.0F, 0.8F + 0.4F * ejectionSoundProgress);
   }
}
