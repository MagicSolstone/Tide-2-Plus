package net.minecraft.client.multiplayer;

import java.util.Arrays;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.entity.LevelCallback;

@Environment(EnvType.CLIENT)
final class ClientLevel$EntityCallbacks implements LevelCallback<Entity> {
   private ClientLevel$EntityCallbacks(final ClientLevel this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
   }

   public void onCreated(final Entity entity) {
   }

   public void onDestroyed(final Entity entity) {
   }

   public void onTickingStart(final Entity entity) {
      this.this$0.tickingEntities.add(entity);
   }

   public void onTickingEnd(final Entity entity) {
      this.this$0.tickingEntities.remove(entity);
   }

   public void onTrackingStart(final Entity entity) {
      switch (entity) {
         case AbstractClientPlayer player:
            this.this$0.players.add(player);
            break;
         case EnderDragon dragon:
            this.this$0.dragonParts.addAll(Arrays.asList(dragon.getSubEntities()));
            break;
         default:
      }
   }

   public void onTrackingEnd(final Entity entity) {
      entity.unRide();
      switch (entity) {
         case AbstractClientPlayer player:
            this.this$0.players.remove(player);
            break;
         case EnderDragon dragon:
            this.this$0.dragonParts.removeAll(Arrays.asList(dragon.getSubEntities()));
            break;
         default:
      }
   }

   public void onSectionChange(final Entity entity) {
   }
}
