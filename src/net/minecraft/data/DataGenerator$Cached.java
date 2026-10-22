package net.minecraft.data;

import com.google.common.base.Stopwatch;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import net.minecraft.WorldVersion;

public class DataGenerator$Cached extends DataGenerator {
   private final Path rootOutputFolder;
   private final WorldVersion version;
   private final boolean alwaysGenerate;

   public DataGenerator$Cached(final Path output, final WorldVersion version, final boolean alwaysGenerate) {
      super(output);
      this.rootOutputFolder = output;
      this.alwaysGenerate = alwaysGenerate;
      this.version = version;
   }

   @Override
   public void run() throws IOException {
      HashCache cache = new HashCache(this.rootOutputFolder, this.allProviderIds, this.version);
      Stopwatch totalTime = Stopwatch.createStarted();
      Stopwatch stopwatch = Stopwatch.createUnstarted();
      this.providersToRun.forEach((providerId, provider) -> {
         if (!this.alwaysGenerate && !cache.shouldRunInThisVersion(providerId)) {
            DataGenerator.LOGGER.debug("Generator {} already run for version {}", providerId, this.version.name());
         } else {
            DataGenerator.LOGGER.info("Starting provider: {}", providerId);
            stopwatch.start();
            cache.applyUpdate(cache.generateUpdate(providerId, provider::run).join());
            stopwatch.stop();
            DataGenerator.LOGGER.info("{} finished after {} ms", providerId, stopwatch.elapsed(TimeUnit.MILLISECONDS));
            stopwatch.reset();
         }
      });
      DataGenerator.LOGGER.info("All providers took: {} ms", totalTime.elapsed(TimeUnit.MILLISECONDS));
      cache.purgeStaleAndWrite();
   }
}
