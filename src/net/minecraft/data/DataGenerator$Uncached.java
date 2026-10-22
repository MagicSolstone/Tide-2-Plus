package net.minecraft.data;

import com.google.common.base.Stopwatch;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class DataGenerator$Uncached extends DataGenerator {
   public DataGenerator$Uncached(final Path output) {
      super(output);
   }

   @Override
   public void run() throws IOException {
      Stopwatch totalTime = Stopwatch.createStarted();
      Stopwatch stopwatch = Stopwatch.createUnstarted();
      this.providersToRun.forEach((providerId, provider) -> {
         DataGenerator.LOGGER.info("Starting uncached provider: {}", providerId);
         stopwatch.start();
         provider.run(CachedOutput.NO_CACHE).join();
         stopwatch.stop();
         DataGenerator.LOGGER.info("{} finished after {} ms", providerId, stopwatch.elapsed(TimeUnit.MILLISECONDS));
         stopwatch.reset();
      });
      DataGenerator.LOGGER.info("All providers took: {} ms", totalTime.elapsed(TimeUnit.MILLISECONDS));
   }
}
