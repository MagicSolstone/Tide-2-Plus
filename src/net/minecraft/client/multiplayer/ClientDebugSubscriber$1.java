package net.minecraft.client.multiplayer;

import java.util.Objects;
import java.util.function.BiConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.util.debug.DebugSubscription;
import net.minecraft.util.debug.DebugValueAccess;
import net.minecraft.util.debug.DebugValueAccess$EventVisitor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class ClientDebugSubscriber$1 implements DebugValueAccess {
   ClientDebugSubscriber$1(final ClientDebugSubscriber this$0, final Level val$level) {
      this.val$level = val$levelx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public <T> void forEachChunk(final DebugSubscription<T> subscription, final BiConsumer<ChunkPos, T> consumer) {
      this.this$0.forEachValue(subscription, ClientDebugSubscriber.chunks(), consumer);
   }

   @Nullable
   @Override
   public <T> T getChunkValue(final DebugSubscription<T> subscription, final ChunkPos chunkPos) {
      return this.this$0.getValue(subscription, chunkPos, ClientDebugSubscriber.chunks());
   }

   @Override
   public <T> void forEachBlock(final DebugSubscription<T> subscription, final BiConsumer<BlockPos, T> consumer) {
      this.this$0.forEachValue(subscription, ClientDebugSubscriber.blocks(), consumer);
   }

   @Nullable
   @Override
   public <T> T getBlockValue(final DebugSubscription<T> subscription, final BlockPos blockPos) {
      return this.this$0.getValue(subscription, blockPos, ClientDebugSubscriber.blocks());
   }

   @Override
   public <T> void forEachEntity(final DebugSubscription<T> subscription, final BiConsumer<Entity, T> consumer) {
      this.this$0.forEachValue(subscription, ClientDebugSubscriber.entities(), (entityId, value) -> {
         Entity entity = level.getEntity(entityId);
         if (entity != null) {
            consumer.accept(entity, value);
         }
      });
   }

   @Nullable
   @Override
   public <T> T getEntityValue(final DebugSubscription<T> subscription, final Entity entity) {
      return this.this$0.getValue(subscription, entity.getUUID(), ClientDebugSubscriber.entities());
   }

   @Override
   public <T> void forEachEvent(final DebugSubscription<T> subscription, final DebugValueAccess$EventVisitor<T> visitor) {
      ClientDebugSubscriber$ValueMaps<T> values = this.this$0.getValueMaps(subscription);
      if (values != null) {
         long gameTime = this.val$level.getGameTime();

         for (ClientDebugSubscriber$ValueWrapper<T> event : values.events) {
            int remainingTicks = (int)(event.expiresAfterTime() - gameTime);
            int totalLifetime = subscription.expireAfterTicks();
            visitor.accept(event.value(), remainingTicks, totalLifetime);
         }
      }
   }
}
