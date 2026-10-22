package net.minecraft.world.level.chunk.status;

import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import org.jspecify.annotations.Nullable;

public class ChunkStep$Builder {
   private final ChunkStatus status;
   @Nullable
   private final ChunkStep parent;
   private ChunkStatus[] directDependenciesByRadius;
   private int blockStateWriteRadius = -1;
   private ChunkStatusTask task = ChunkStatusTasks::passThrough;

   protected ChunkStep$Builder(final ChunkStatus status) {
      if (status.getParent() != status) {
         throw new IllegalArgumentException("Not starting with the first status: " + status);
      } else {
         this.status = status;
         this.parent = null;
         this.directDependenciesByRadius = new ChunkStatus[0];
      }
   }

   protected ChunkStep$Builder(final ChunkStatus status, final ChunkStep parent) {
      if (parent.targetStatus.getIndex() != status.getIndex() - 1) {
         throw new IllegalArgumentException("Out of order status: " + status);
      } else {
         this.status = status;
         this.parent = parent;
         this.directDependenciesByRadius = new ChunkStatus[]{parent.targetStatus};
      }
   }

   public ChunkStep$Builder addRequirement(final ChunkStatus status, final int radius) {
      if (status.isOrAfter(this.status)) {
         throw new IllegalArgumentException("Status " + status + " can not be required by " + this.status);
      } else {
         ChunkStatus[] previous = this.directDependenciesByRadius;
         int newLength = radius + 1;
         if (newLength > previous.length) {
            this.directDependenciesByRadius = new ChunkStatus[newLength];
            Arrays.fill(this.directDependenciesByRadius, status);
         }

         for (int i = 0; i < Math.min(newLength, previous.length); i++) {
            this.directDependenciesByRadius[i] = ChunkStatus.max(previous[i], status);
         }

         return this;
      }
   }

   public ChunkStep$Builder blockStateWriteRadius(final int radius) {
      this.blockStateWriteRadius = radius;
      return this;
   }

   public ChunkStep$Builder setTask(final ChunkStatusTask task) {
      this.task = task;
      return this;
   }

   public ChunkStep build() {
      return new ChunkStep(
         this.status,
         new ChunkDependencies(ImmutableList.copyOf(this.directDependenciesByRadius)),
         new ChunkDependencies(ImmutableList.copyOf(this.buildAccumulatedDependencies())),
         this.blockStateWriteRadius,
         this.task
      );
   }

   private ChunkStatus[] buildAccumulatedDependencies() {
      if (this.parent == null) {
         return this.directDependenciesByRadius;
      } else {
         int radiusOfParent = this.getRadiusOfParent(this.parent.targetStatus);
         ChunkDependencies parentDependencies = this.parent.accumulatedDependencies;
         ChunkStatus[] accumulatedDependencies = new ChunkStatus[Math.max(radiusOfParent + parentDependencies.size(), this.directDependenciesByRadius.length)];

         for (int distance = 0; distance < accumulatedDependencies.length; distance++) {
            int distanceInParent = distance - radiusOfParent;
            if (distanceInParent < 0 || distanceInParent >= parentDependencies.size()) {
               accumulatedDependencies[distance] = this.directDependenciesByRadius[distance];
            } else if (distance >= this.directDependenciesByRadius.length) {
               accumulatedDependencies[distance] = parentDependencies.get(distanceInParent);
            } else {
               accumulatedDependencies[distance] = ChunkStatus.max(this.directDependenciesByRadius[distance], parentDependencies.get(distanceInParent));
            }
         }

         return accumulatedDependencies;
      }
   }

   private int getRadiusOfParent(final ChunkStatus status) {
      for (int i = this.directDependenciesByRadius.length - 1; i >= 0; i--) {
         if (this.directDependenciesByRadius[i].isOrAfter(status)) {
            return i;
         }
      }

      return 0;
   }
}
