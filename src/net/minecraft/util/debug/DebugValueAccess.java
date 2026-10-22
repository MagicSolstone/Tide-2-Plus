package net.minecraft.util.debug;

import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import org.jspecify.annotations.Nullable;

public interface DebugValueAccess {
   <T> void forEachChunk(DebugSubscription<T> subscription, BiConsumer<ChunkPos, T> consumer);

   @Nullable
   <T> T getChunkValue(DebugSubscription<T> subscription, ChunkPos chunkPos);

   <T> void forEachBlock(DebugSubscription<T> subscription, BiConsumer<BlockPos, T> consumer);

   @Nullable
   <T> T getBlockValue(DebugSubscription<T> subscription, BlockPos blockPos);

   <T> void forEachEntity(DebugSubscription<T> subscription, BiConsumer<Entity, T> consumer);

   @Nullable
   <T> T getEntityValue(DebugSubscription<T> subscription, Entity entity);

   <T> void forEachEvent(DebugSubscription<T> subscription, DebugValueAccess$EventVisitor<T> visitor);
}
