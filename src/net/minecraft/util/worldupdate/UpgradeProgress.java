package net.minecraft.util.worldupdate;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Reference2FloatMap;
import it.unimi.dsi.fastutil.objects.Reference2FloatMaps;
import it.unimi.dsi.fastutil.objects.Reference2FloatOpenHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.notifications.EmptyNotificationService;
import net.minecraft.server.notifications.NotificationService;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class UpgradeProgress {
   private static final Logger LOGGER = LogUtils.getLogger();
   private volatile boolean finished;
   private final UpgradeProgress$FileFixStats totalFileFixStats = new UpgradeProgress$FileFixStats();
   private final UpgradeProgress$FileFixStats typeFileFixStats = new UpgradeProgress$FileFixStats();
   private final UpgradeProgress$FileFixStats runningFileFixerStats = new UpgradeProgress$FileFixStats();
   private volatile float totalProgress;
   private final AtomicInteger totalChunks = new AtomicInteger();
   private final AtomicInteger converted = new AtomicInteger();
   private final AtomicInteger skipped = new AtomicInteger();
   private final Reference2FloatMap<ResourceKey<Level>> progressMap = Reference2FloatMaps.synchronize(new Reference2FloatOpenHashMap());
   private volatile boolean canceled = false;
   @Nullable
   private volatile DataFixTypes dataFixType;
   private volatile UpgradeProgress$Status status = UpgradeProgress$Status.COUNTING;
   @Nullable
   private volatile UpgradeProgress$Type type;
   private final AtomicLong lastLoggedProgressTime = new AtomicLong();
   private final NotificationService notificationService;

   public UpgradeProgress() {
      this(new EmptyNotificationService());
   }

   public UpgradeProgress(final NotificationService notificationService) {
      this.notificationService = notificationService;
   }

   public boolean isFinished() {
      return this.finished;
   }

   public void setFinished(final boolean finished) {
      this.finished = finished;
   }

   public UpgradeProgress$FileFixStats getTotalFileFixStats() {
      return this.totalFileFixStats;
   }

   public UpgradeProgress$FileFixStats getTypeFileFixStats() {
      return this.typeFileFixStats;
   }

   public UpgradeProgress$FileFixStats getRunningFileFixerStats() {
      return this.runningFileFixerStats;
   }

   public void addTotalFileFixOperations(final int additionalFileFixOperations) {
      this.totalFileFixStats.totalOperations.addAndGet(additionalFileFixOperations);
      this.typeFileFixStats.totalOperations.addAndGet(additionalFileFixOperations);
   }

   public float getTotalProgress() {
      return this.totalProgress;
   }

   public void setTotalProgress(final float totalProgress) {
      this.totalProgress = totalProgress;
   }

   public int getTotalChunks() {
      return this.totalChunks.get();
   }

   public void addTotalChunks(final int additionalTotalChunks) {
      this.totalChunks.addAndGet(additionalTotalChunks);
   }

   public int getConverted() {
      return this.converted.get();
   }

   public void setDimensionProgress(final ResourceKey<Level> dimensionKey, final float currentProgress) {
      this.progressMap.put(dimensionKey, currentProgress);
   }

   public float getDimensionProgress(final ResourceKey<Level> dimensionKey) {
      return this.progressMap.getFloat(dimensionKey);
   }

   public void incrementConverted() {
      this.converted.incrementAndGet();
   }

   public int getSkipped() {
      return this.skipped.get();
   }

   public void incrementSkipped() {
      this.skipped.incrementAndGet();
   }

   public void incrementFinishedOperations() {
      this.incrementFinishedOperationsBy(1);
   }

   public void incrementFinishedOperationsBy(final int count) {
      this.totalFileFixStats.finishedOperations.addAndGet(count);
      this.typeFileFixStats.finishedOperations.addAndGet(count);
      this.logProgress();
   }

   public void setCanceled() {
      this.canceled = true;
   }

   public boolean isCanceled() {
      return this.canceled;
   }

   public UpgradeProgress$Status getStatus() {
      return this.status;
   }

   public void setStatus(final UpgradeProgress$Status status) {
      this.status = status;
   }

   @Nullable
   public DataFixTypes getDataFixType() {
      return this.dataFixType;
   }

   public void setType(final UpgradeProgress$Type type) {
      this.type = type;
      this.typeFileFixStats.reset();
   }

   @Nullable
   public UpgradeProgress$Type getType() {
      return this.type;
   }

   public void setApplicableFixerAmount(final int amount) {
      this.runningFileFixerStats.totalOperations.set(amount);
   }

   public void incrementRunningFileFixer() {
      this.runningFileFixerStats.finishedOperations.incrementAndGet();
   }

   public void reset(final DataFixTypes dataFixType) {
      this.totalFileFixStats.reset();
      this.typeFileFixStats.reset();
      this.totalChunks.set(0);
      this.converted.set(0);
      this.skipped.set(0);
      this.dataFixType = dataFixType;
   }

   public void logProgress() {
      long now = Util.getMillis();
      if (now >= this.lastLoggedProgressTime.get() + 1000L) {
         float progress = (float)this.totalFileFixStats.finishedOperations() / this.totalFileFixStats.totalOperations();
         this.lastLoggedProgressTime.set(now);
         LOGGER.info("Upgrading progress: {}%", (int)(progress * 100.0F));
         this.notificationService.worldUpgradeProgress(progress);
      }
   }

   public NotificationService notifications() {
      return this.notificationService;
   }
}
