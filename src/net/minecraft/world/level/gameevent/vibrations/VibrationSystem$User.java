package net.minecraft.world.level.gameevent.vibrations;

import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent$Context;
import net.minecraft.world.level.gameevent.PositionSource;
import org.jspecify.annotations.Nullable;

public interface VibrationSystem$User {
   int getListenerRadius();

   PositionSource getPositionSource();

   boolean canReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> event, GameEvent$Context context);

   void onReceiveVibration(
      ServerLevel level, BlockPos pos, Holder<GameEvent> event, @Nullable Entity sourceEntity, @Nullable Entity projectileOwner, float receivingDistance
   );

   default TagKey<GameEvent> getListenableEvents() {
      return GameEventTags.VIBRATIONS;
   }

   default boolean canTriggerAvoidVibration() {
      return false;
   }

   default boolean requiresAdjacentChunksToBeTicking() {
      return false;
   }

   default int calculateTravelTimeInTicks(final float distanceToDestination) {
      return Mth.floor(distanceToDestination);
   }

   default boolean isValidVibration(final Holder<GameEvent> event, final GameEvent$Context context) {
      if (!event.is(this.getListenableEvents())) {
         return false;
      } else {
         Entity sourceEntity = context.sourceEntity();
         if (sourceEntity != null) {
            if (sourceEntity.isSpectator()) {
               return false;
            }

            if (sourceEntity.isSteppingCarefully() && event.is(GameEventTags.IGNORE_VIBRATIONS_SNEAKING)) {
               if (this.canTriggerAvoidVibration() && sourceEntity instanceof ServerPlayer player) {
                  CriteriaTriggers.AVOID_VIBRATION.trigger(player);
               }

               return false;
            }

            if (sourceEntity.dampensVibrations()) {
               return false;
            }
         }

         return context.affectedState() != null ? !context.affectedState().is(BlockTags.DAMPENS_VIBRATIONS) : true;
      }
   }

   default void onDataChanged() {
   }
}
