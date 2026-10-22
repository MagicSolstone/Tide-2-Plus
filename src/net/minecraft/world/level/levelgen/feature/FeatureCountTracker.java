package net.minecraft.world.level.levelgen.feature;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.mojang.logging.LogUtils;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.slf4j.Logger;

public class FeatureCountTracker {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final LoadingCache<ServerLevel, FeatureCountTracker$LevelData> data = CacheBuilder.newBuilder()
      .weakKeys()
      .expireAfterAccess(5L, TimeUnit.MINUTES)
      .build(new FeatureCountTracker$1());

   public static void chunkDecorated(final ServerLevel level) {
      try {
         ((FeatureCountTracker$LevelData)data.get(level)).chunksWithFeatures().increment();
      } catch (Exception var2) {
         LOGGER.error("Failed to increment chunk count", var2);
      }
   }

   public static void featurePlaced(final ServerLevel level, final Feature feature, final Optional<PlacedFeature> topFeature) {
      try {
         ((FeatureCountTracker$LevelData)data.get(level))
            .featureData()
            .computeInt(new FeatureCountTracker$FeatureData(feature, topFeature), (f, old) -> old == null ? 1 : old + 1);
      } catch (Exception var4) {
         LOGGER.error("Failed to increment feature count", var4);
      }
   }

   public static void clearCounts() {
      data.invalidateAll();
      LOGGER.debug("Cleared feature counts");
   }

   public static void logCounts() {
      LOGGER.debug("Logging feature counts:");
      data.asMap()
         .forEach(
            (level, featureCounts) -> {
               String name = level.dimension().identifier().toString();
               boolean running = level.getServer().isRunning();
               Registry<PlacedFeature> featureRegistry = level.registryAccess().lookupOrThrow(Registries.PLACED_FEATURE);
               String prefix = (running ? "running" : "dead") + " " + name;
               int chunks = featureCounts.chunksWithFeatures().intValue();
               LOGGER.debug("{} total_chunks: {}", prefix, chunks);
               featureCounts.featureData()
                  .forEach(
                     (data, count) -> LOGGER.debug(
                        "{} {} {} {} {}",
                        new Object[]{
                           prefix,
                           String.format(Locale.ROOT, "%10d", count),
                           String.format(Locale.ROOT, "%10f", (double)count / chunks),
                           data.topFeature().flatMap(featureRegistry::getResourceKey).map(ResourceKey::identifier),
                           data.feature()
                        }
                     )
                  );
            }
         );
   }
}
