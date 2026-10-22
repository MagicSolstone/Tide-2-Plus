package net.minecraft.util.worldupdate;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;

public class UpgradeProgress$Noop extends UpgradeProgress {
   @Override
   public void setFinished(final boolean finished) {
   }

   @Override
   public void addTotalFileFixOperations(final int additionalFileFixOperations) {
   }

   @Override
   public void setTotalProgress(final float totalProgress) {
   }

   @Override
   public void addTotalChunks(final int additionalTotalChunks) {
   }

   @Override
   public void setDimensionProgress(final ResourceKey<Level> dimensionKey, final float currentProgress) {
   }

   @Override
   public void incrementConverted() {
   }

   @Override
   public void incrementSkipped() {
   }

   @Override
   public void incrementFinishedOperations() {
   }

   @Override
   public void incrementFinishedOperationsBy(final int count) {
   }

   @Override
   public void setCanceled() {
   }

   @Override
   public void setStatus(final UpgradeProgress$Status status) {
   }

   @Override
   public void setApplicableFixerAmount(final int amount) {
   }

   @Override
   public void incrementRunningFileFixer() {
   }

   @Override
   public void reset(final DataFixTypes dataFixType) {
   }
}
