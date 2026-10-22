package net.minecraft.world.level.block.entity.vault;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Util;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.VaultBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootParams$Builder;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public final class VaultBlockEntity$Server {
   private static final int UNLOCKING_DELAY_TICKS = 14;
   private static final int DISPLAY_CYCLE_TICK_RATE = 20;
   private static final int INSERT_FAIL_SOUND_BUFFER_TICKS = 15;

   public static void tick(
      final ServerLevel serverLevel,
      final BlockPos pos,
      final BlockState blockState,
      final VaultConfig config,
      final VaultServerData serverData,
      final VaultSharedData sharedData
   ) {
      VaultState currentState = blockState.getValue(VaultBlock.STATE);
      if (shouldCycleDisplayItem(serverLevel.getGameTime(), currentState)) {
         cycleDisplayItemFromLootTable(serverLevel, currentState, config, sharedData, pos);
      }

      BlockState nextBlockState = blockState;
      if (serverLevel.getGameTime() >= serverData.stateUpdatingResumesAt()) {
         nextBlockState = blockState.setValue(VaultBlock.STATE, currentState.tickAndGetNext(serverLevel, pos, config, serverData, sharedData));
         if (blockState != nextBlockState) {
            setVaultState(serverLevel, pos, blockState, nextBlockState, config, sharedData);
         }
      }

      if (serverData.isDirty || sharedData.isDirty) {
         VaultBlockEntity.access$000(serverLevel, pos, blockState);
         if (sharedData.isDirty) {
            serverLevel.sendBlockUpdated(pos, blockState, nextBlockState, 2);
         }

         serverData.isDirty = false;
         sharedData.isDirty = false;
      }
   }

   public static void tryInsertKey(
      final ServerLevel serverLevel,
      final BlockPos pos,
      final BlockState blockState,
      final VaultConfig config,
      final VaultServerData serverData,
      final VaultSharedData sharedData,
      final Player player,
      final ItemStack stackToInsert
   ) {
      VaultState vaultState = blockState.getValue(VaultBlock.STATE);
      if (canEjectReward(config, vaultState)) {
         if (!isValidToInsert(config, stackToInsert)) {
            playInsertFailSound(serverLevel, serverData, pos, SoundEvents.VAULT_INSERT_ITEM_FAIL);
         } else if (serverData.hasRewardedPlayer(player)) {
            playInsertFailSound(serverLevel, serverData, pos, SoundEvents.VAULT_REJECT_REWARDED_PLAYER);
         } else {
            List<ItemStack> itemsToEject = resolveItemsToEject(serverLevel, config, pos, player, stackToInsert);
            if (!itemsToEject.isEmpty()) {
               player.awardStat(Stats.ITEM_USED.get(stackToInsert.getItem()));
               stackToInsert.consume(config.keyItem().getCount(), player);
               unlock(serverLevel, blockState, pos, config, serverData, sharedData, itemsToEject);
               serverData.addToRewardedPlayers(player);
               sharedData.updateConnectedPlayersWithinRange(serverLevel, pos, serverData, config, config.deactivationRange());
            }
         }
      }
   }

   private static void setVaultState(
      final ServerLevel serverLevel,
      final BlockPos pos,
      final BlockState currentBlockState,
      final BlockState newBlockState,
      final VaultConfig config,
      final VaultSharedData sharedData
   ) {
      VaultState currentVaultState = currentBlockState.getValue(VaultBlock.STATE);
      VaultState newVaultState = newBlockState.getValue(VaultBlock.STATE);
      serverLevel.setBlockAndUpdate(pos, newBlockState);
      currentVaultState.onTransition(serverLevel, pos, newVaultState, config, sharedData, newBlockState.getValue(VaultBlock.OMINOUS));
   }

   static void cycleDisplayItemFromLootTable(
      final ServerLevel serverLevel, final VaultState vaultState, final VaultConfig config, final VaultSharedData sharedData, final BlockPos pos
   ) {
      if (!canEjectReward(config, vaultState)) {
         sharedData.setDisplayItem(ItemStack.EMPTY);
      } else {
         ItemStack displayItem = getRandomDisplayItemFromLootTable(serverLevel, pos, config.overrideLootTableToDisplay().orElse(config.lootTable()));
         sharedData.setDisplayItem(displayItem);
      }
   }

   private static ItemStack getRandomDisplayItemFromLootTable(final ServerLevel serverLevel, final BlockPos pos, final ResourceKey<LootTable> lootTableId) {
      LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(lootTableId);
      LootParams params = new LootParams$Builder(serverLevel).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).create(LootContextParamSets.VAULT);
      List<ItemStack> results = lootTable.getRandomItems(params, serverLevel.getRandom());
      return results.isEmpty() ? ItemStack.EMPTY : Util.getRandom(results, serverLevel.getRandom());
   }

   private static void unlock(
      final ServerLevel serverLevel,
      final BlockState blockState,
      final BlockPos pos,
      final VaultConfig config,
      final VaultServerData serverData,
      final VaultSharedData sharedData,
      final List<ItemStack> itemsToEject
   ) {
      serverData.setItemsToEject(itemsToEject);
      sharedData.setDisplayItem(serverData.getNextItemToEject());
      serverData.pauseStateUpdatingUntil(serverLevel.getGameTime() + 14L);
      setVaultState(serverLevel, pos, blockState, blockState.setValue(VaultBlock.STATE, VaultState.UNLOCKING), config, sharedData);
   }

   private static List<ItemStack> resolveItemsToEject(
      final ServerLevel serverLevel, final VaultConfig config, final BlockPos pos, final Player player, final ItemInstance insertedStack
   ) {
      LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(config.lootTable());
      LootParams params = new LootParams$Builder(serverLevel)
         .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
         .withLuck(player.getLuck())
         .withParameter(LootContextParams.THIS_ENTITY, player)
         .withParameter(LootContextParams.TOOL, insertedStack)
         .create(LootContextParamSets.VAULT);
      return lootTable.getRandomItems(params);
   }

   private static boolean canEjectReward(final VaultConfig config, final VaultState vaultState) {
      return !config.keyItem().isEmpty() && vaultState != VaultState.INACTIVE;
   }

   private static boolean isValidToInsert(final VaultConfig config, final ItemStack stackToInsert) {
      return ItemStack.isSameItemSameComponents(stackToInsert, config.keyItem()) && stackToInsert.getCount() >= config.keyItem().getCount();
   }

   private static boolean shouldCycleDisplayItem(final long gameTime, final VaultState vaultState) {
      return gameTime % 20L == 0L && vaultState == VaultState.ACTIVE;
   }

   private static void playInsertFailSound(final ServerLevel serverLevel, final VaultServerData serverData, final BlockPos pos, final SoundEvent sound) {
      if (serverLevel.getGameTime() >= serverData.getLastInsertFailTimestamp() + 15L) {
         serverLevel.playSound(null, pos, sound, SoundSource.BLOCKS);
         serverData.setLastInsertFailTimestamp(serverLevel.getGameTime());
      }
   }
}
