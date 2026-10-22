package net.minecraft.world.level.chunk.status;

import java.util.concurrent.CompletableFuture;
import net.minecraft.server.level.GenerationChunkHolder;
import net.minecraft.util.StaticCache2D;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.Zone;
import net.minecraft.util.profiling.jfr.JvmProfiler;
import net.minecraft.util.profiling.jfr.callback.ProfiledDuration;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ProtoChunk;
import org.jspecify.annotations.Nullable;

public record ChunkStep(
   ChunkStatus targetStatus, ChunkDependencies directDependencies, ChunkDependencies accumulatedDependencies, int blockStateWriteRadius, ChunkStatusTask task
) {
   public int getAccumulatedRadiusOf(final ChunkStatus status) {
      return status == this.targetStatus ? 0 : this.accumulatedDependencies.getRadiusOf(status);
   }

   public CompletableFuture<ChunkAccess> apply(final WorldGenContext context, final StaticCache2D<GenerationChunkHolder> cache, final ChunkAccess chunk) {
      if (chunk.getPersistedStatus().isBefore(this.targetStatus)) {
         String stepName = this.targetStatus.getName();

         CompletableFuture var7;
         try (Zone var5 = Profiler.get().zone(stepName)) {
            ProfiledDuration profiledDuration = JvmProfiler.INSTANCE.onChunkGenerate(chunk.getPos(), context.level().dimension(), stepName);
            var7 = this.task.doWork(context, this, cache, chunk).thenApply(newCenterChunk -> this.completeChunkGeneration(newCenterChunk, profiledDuration));
         }

         return var7;
      } else {
         return this.task.doWork(context, this, cache, chunk);
      }
   }

   private ChunkAccess completeChunkGeneration(final ChunkAccess newCenterChunk, @Nullable final ProfiledDuration profiledDuration) {
      if (newCenterChunk instanceof ProtoChunk protochunk && protochunk.getPersistedStatus().isBefore(this.targetStatus)) {
         protochunk.setPersistedStatus(this.targetStatus);
      }

      if (profiledDuration != null) {
         profiledDuration.finish(true);
      }

      return newCenterChunk;
   }
}
