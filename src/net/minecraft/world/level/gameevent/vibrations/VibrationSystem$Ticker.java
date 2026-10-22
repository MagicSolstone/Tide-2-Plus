package net.minecraft.world.level.gameevent.vibrations;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;

public interface VibrationSystem$Ticker {
   static void tick(final Level level, final VibrationSystem$Data data, final VibrationSystem$User user) {
      if (level instanceof ServerLevel serverLevel) {
         if (data.currentVibration == null) {
            trySelectAndScheduleVibration(serverLevel, data, user);
         }

         if (data.currentVibration != null) {
            boolean hasChanged = data.getTravelTimeInTicks() > 0;
            tryReloadVibrationParticle(serverLevel, data, user);
            data.decrementTravelTime();
            if (data.getTravelTimeInTicks() <= 0) {
               hasChanged = receiveVibration(serverLevel, data, user, data.currentVibration);
            }

            if (hasChanged) {
               user.onDataChanged();
            }
         }
      }
   }

   private static void trySelectAndScheduleVibration(final ServerLevel serverLevel, final VibrationSystem$Data data, final VibrationSystem$User user) {
      data.getSelectionStrategy()
         .chosenCandidate(serverLevel.getGameTime())
         .ifPresent(
            context -> {
               data.setCurrentVibration(context);
               Vec3 origin = context.pos();
               data.setTravelTimeInTicks(user.calculateTravelTimeInTicks(context.distance()));
               serverLevel.sendParticles(
                  new VibrationParticleOption(user.getPositionSource(), data.getTravelTimeInTicks()), origin.x, origin.y, origin.z, 1, 0.0, 0.0, 0.0, 0.0
               );
               user.onDataChanged();
               data.getSelectionStrategy().startOver();
            }
         );
   }

   private static void tryReloadVibrationParticle(final ServerLevel level, final VibrationSystem$Data data, final VibrationSystem$User user) {
      if (data.shouldReloadVibrationParticle()) {
         if (data.currentVibration == null) {
            data.setReloadVibrationParticle(false);
         } else {
            Vec3 origin = data.currentVibration.pos();
            PositionSource positionSource = user.getPositionSource();
            Vec3 destination = positionSource.getPosition(level).orElse(origin);
            int travelTimeInTicks = data.getTravelTimeInTicks();
            int initialTravelTime = user.calculateTravelTimeInTicks(data.currentVibration.distance());
            double alpha = 1.0 - (double)travelTimeInTicks / initialTravelTime;
            double newInitialX = Mth.lerp(alpha, origin.x, destination.x);
            double newInitialY = Mth.lerp(alpha, origin.y, destination.y);
            double newInitialZ = Mth.lerp(alpha, origin.z, destination.z);
            boolean particleWasSent = level.sendParticles(
                  new VibrationParticleOption(positionSource, travelTimeInTicks), newInitialX, newInitialY, newInitialZ, 1, 0.0, 0.0, 0.0, 0.0
               )
               > 0;
            if (particleWasSent) {
               data.setReloadVibrationParticle(false);
            }
         }
      }
   }

   private static boolean receiveVibration(
      final ServerLevel serverLevel, final VibrationSystem$Data data, final VibrationSystem$User user, final VibrationInfo currentVibration
   ) {
      BlockPos origin = BlockPos.containing(currentVibration.pos());
      BlockPos destination = user.getPositionSource().getPosition(serverLevel).map(BlockPos::containing).orElse(origin);
      if (user.requiresAdjacentChunksToBeTicking() && !areAdjacentChunksTicking(serverLevel, destination)) {
         return false;
      } else {
         user.onReceiveVibration(
            serverLevel,
            origin,
            currentVibration.gameEvent(),
            currentVibration.getEntity(serverLevel).orElse(null),
            currentVibration.getProjectileOwner(serverLevel).orElse(null),
            VibrationSystem$Listener.distanceBetweenInBlocks(origin, destination)
         );
         data.setCurrentVibration(null);
         return true;
      }
   }

   private static boolean areAdjacentChunksTicking(final Level level, final BlockPos listenerPos) {
      ChunkPos listenerChunkPos = ChunkPos.containing(listenerPos);

      for (int x = listenerChunkPos.x() - 1; x <= listenerChunkPos.x() + 1; x++) {
         for (int z = listenerChunkPos.z() - 1; z <= listenerChunkPos.z() + 1; z++) {
            if (!level.shouldTickBlocksAt(ChunkPos.pack(x, z)) || level.getChunkSource().getChunkNow(x, z) == null) {
               return false;
            }
         }
      }

      return true;
   }
}
