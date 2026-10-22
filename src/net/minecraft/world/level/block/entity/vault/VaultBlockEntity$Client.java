package net.minecraft.world.level.block.entity.vault;

import java.util.Set;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.VaultBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public final class VaultBlockEntity$Client {
   private static final int PARTICLE_TICK_RATE = 20;
   private static final float IDLE_PARTICLE_CHANCE = 0.5F;
   private static final float AMBIENT_SOUND_CHANCE = 0.02F;
   private static final int ACTIVATION_PARTICLE_COUNT = 20;
   private static final int DEACTIVATION_PARTICLE_COUNT = 20;

   public static void tick(
      final Level clientLevel, final BlockPos pos, final BlockState blockState, final VaultClientData clientData, final VaultSharedData sharedData
   ) {
      clientData.updateDisplayItemSpin();
      if (clientLevel.getGameTime() % 20L == 0L) {
         emitConnectionParticlesForNearbyPlayers(clientLevel, pos, blockState, sharedData);
      }

      emitIdleParticles(clientLevel, pos, sharedData, blockState.getValue(VaultBlock.OMINOUS) ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.SMALL_FLAME);
      playIdleSounds(clientLevel, pos, sharedData);
   }

   public static void emitActivationParticles(
      final Level clientLevel, final BlockPos pos, final BlockState blockState, final VaultSharedData sharedData, final ParticleOptions flameParticle
   ) {
      emitConnectionParticlesForNearbyPlayers(clientLevel, pos, blockState, sharedData);
      RandomSource random = clientLevel.getRandom();

      for (int i = 0; i < 20; i++) {
         Vec3 particlePos = randomPosInsideCage(pos, random);
         clientLevel.addParticle(ParticleTypes.SMOKE, particlePos.x(), particlePos.y(), particlePos.z(), 0.0, 0.0, 0.0);
         clientLevel.addParticle(flameParticle, particlePos.x(), particlePos.y(), particlePos.z(), 0.0, 0.0, 0.0);
      }
   }

   public static void emitDeactivationParticles(final Level clientLevel, final BlockPos pos, final ParticleOptions flameParticle) {
      RandomSource random = clientLevel.getRandom();

      for (int i = 0; i < 20; i++) {
         Vec3 particlePos = randomPosCenterOfCage(pos, random);
         Vec3 dir = new Vec3(random.nextGaussian() * 0.02, random.nextGaussian() * 0.02, random.nextGaussian() * 0.02);
         clientLevel.addParticle(flameParticle, particlePos.x(), particlePos.y(), particlePos.z(), dir.x(), dir.y(), dir.z());
      }
   }

   private static void emitIdleParticles(final Level clientLevel, final BlockPos pos, final VaultSharedData sharedData, final ParticleOptions flameParticle) {
      RandomSource random = clientLevel.getRandom();
      if (random.nextFloat() <= 0.5F) {
         Vec3 particlePos = randomPosInsideCage(pos, random);
         clientLevel.addParticle(ParticleTypes.SMOKE, particlePos.x(), particlePos.y(), particlePos.z(), 0.0, 0.0, 0.0);
         if (shouldDisplayActiveEffects(sharedData)) {
            clientLevel.addParticle(flameParticle, particlePos.x(), particlePos.y(), particlePos.z(), 0.0, 0.0, 0.0);
         }
      }
   }

   private static void emitConnectionParticlesForPlayer(final Level level, final Vec3 flyTowards, final Player player) {
      RandomSource random = level.getRandom();
      Vec3 direction = flyTowards.vectorTo(player.position().add(0.0, player.getBbHeight() / 2.0F, 0.0));
      int particleCount = Mth.nextInt(random, 2, 5);

      for (int i = 0; i < particleCount; i++) {
         Vec3 randomDirection = direction.offsetRandom(random, 1.0F);
         level.addParticle(
            ParticleTypes.VAULT_CONNECTION, flyTowards.x(), flyTowards.y(), flyTowards.z(), randomDirection.x(), randomDirection.y(), randomDirection.z()
         );
      }
   }

   private static void emitConnectionParticlesForNearbyPlayers(
      final Level level, final BlockPos pos, final BlockState blockState, final VaultSharedData sharedData
   ) {
      Set<UUID> connectedPlayers = sharedData.getConnectedPlayers();
      if (!connectedPlayers.isEmpty()) {
         Vec3 keyholePos = keyholePos(pos, blockState.getValue(VaultBlock.FACING));

         for (UUID uuid : connectedPlayers) {
            Player player = level.getPlayerByUUID(uuid);
            if (player != null && isWithinConnectionRange(pos, sharedData, player)) {
               emitConnectionParticlesForPlayer(level, keyholePos, player);
            }
         }
      }
   }

   private static boolean isWithinConnectionRange(final BlockPos vaultPos, final VaultSharedData sharedData, final Player player) {
      return player.blockPosition().distSqr(vaultPos) <= Mth.square(sharedData.connectedParticlesRange());
   }

   private static void playIdleSounds(final Level clientLevel, final BlockPos pos, final VaultSharedData sharedData) {
      if (shouldDisplayActiveEffects(sharedData)) {
         RandomSource random = clientLevel.getRandom();
         if (random.nextFloat() <= 0.02F) {
            clientLevel.playLocalSound(pos, SoundEvents.VAULT_AMBIENT, SoundSource.BLOCKS, random.nextFloat() * 0.25F + 0.75F, random.nextFloat() + 0.5F, false);
         }
      }
   }

   public static boolean shouldDisplayActiveEffects(final VaultSharedData sharedData) {
      return sharedData.hasDisplayItem();
   }

   private static Vec3 randomPosCenterOfCage(final BlockPos blockPos, final RandomSource random) {
      return Vec3.atLowerCornerOf(blockPos).add(Mth.nextDouble(random, 0.4, 0.6), Mth.nextDouble(random, 0.4, 0.6), Mth.nextDouble(random, 0.4, 0.6));
   }

   private static Vec3 randomPosInsideCage(final BlockPos blockPos, final RandomSource random) {
      return Vec3.atLowerCornerOf(blockPos).add(Mth.nextDouble(random, 0.1, 0.9), Mth.nextDouble(random, 0.25, 0.75), Mth.nextDouble(random, 0.1, 0.9));
   }

   private static Vec3 keyholePos(final BlockPos blockPos, final Direction blockFacing) {
      return Vec3.atBottomCenterOf(blockPos).add(blockFacing.getStepX() * 0.5, 1.75, blockFacing.getStepZ() * 0.5);
   }
}
