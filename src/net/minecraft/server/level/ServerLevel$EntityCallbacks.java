package net.minecraft.server.level;

import java.util.Objects;
import net.minecraft.util.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
import net.minecraft.world.level.entity.LevelCallback;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.waypoints.WaypointTransmitter;

final class ServerLevel$EntityCallbacks implements LevelCallback<Entity> {
   private ServerLevel$EntityCallbacks(final ServerLevel this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
   }

   public void onCreated(final Entity entity) {
      if (entity instanceof WaypointTransmitter waypoint && waypoint.isTransmittingWaypoint()) {
         this.this$0.getWaypointManager().trackWaypoint(waypoint);
      }
   }

   public void onDestroyed(final Entity entity) {
      if (entity instanceof WaypointTransmitter waypoint) {
         this.this$0.getWaypointManager().untrackWaypoint(waypoint);
      }

      this.this$0.getScoreboard().entityRemoved(entity);
   }

   public void onTickingStart(final Entity entity) {
      this.this$0.entityTickList.add(entity);
   }

   public void onTickingEnd(final Entity entity) {
      this.this$0.entityTickList.remove(entity);
   }

   public void onTrackingStart(final Entity entity) {
      this.this$0.getChunkSource().addEntity(entity);
      if (entity instanceof ServerPlayer player) {
         this.this$0.players.add(player);
         if (player.isReceivingWaypoints()) {
            this.this$0.getWaypointManager().addPlayer(player);
         }

         this.this$0.updateSleepingPlayerList();
      }

      if (entity instanceof WaypointTransmitter waypoint && waypoint.isTransmittingWaypoint()) {
         this.this$0.getWaypointManager().trackWaypoint(waypoint);
      }

      if (entity instanceof Mob mob) {
         if (this.this$0.isUpdatingNavigations) {
            String message = "onTrackingStart called during navigation iteration";
            Util.logAndPauseIfInIde(
               "onTrackingStart called during navigation iteration", new IllegalStateException("onTrackingStart called during navigation iteration")
            );
         }

         this.this$0.navigatingMobs.add(mob);
      }

      if (entity instanceof EnderDragon dragon) {
         for (EnderDragonPart subEntity : dragon.getSubEntities()) {
            this.this$0.dragonParts.put(subEntity.getId(), subEntity);
         }
      }

      entity.updateDynamicGameEventListener(DynamicGameEventListener::add);
   }

   public void onTrackingEnd(final Entity entity) {
      this.this$0.getChunkSource().removeEntity(entity);
      if (entity instanceof ServerPlayer player) {
         this.this$0.players.remove(player);
         this.this$0.getWaypointManager().removePlayer(player);
         this.this$0.updateSleepingPlayerList();
      }

      if (entity instanceof Mob mob) {
         if (this.this$0.isUpdatingNavigations) {
            String message = "onTrackingStart called during navigation iteration";
            Util.logAndPauseIfInIde(
               "onTrackingStart called during navigation iteration", new IllegalStateException("onTrackingStart called during navigation iteration")
            );
         }

         this.this$0.navigatingMobs.remove(mob);
      }

      if (entity instanceof EnderDragon dragon) {
         for (EnderDragonPart subEntity : dragon.getSubEntities()) {
            this.this$0.dragonParts.remove(subEntity.getId());
         }
      }

      entity.updateDynamicGameEventListener(DynamicGameEventListener::remove);
      this.this$0.debugSynchronizers.dropEntity(entity);
   }

   public void onSectionChange(final Entity entity) {
      entity.updateDynamicGameEventListener(DynamicGameEventListener::move);
   }
}
