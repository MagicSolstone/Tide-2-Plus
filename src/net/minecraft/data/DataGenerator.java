package net.minecraft.data;

import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.server.Bootstrap;
import org.slf4j.Logger;

public abstract class DataGenerator {
   private static final Logger LOGGER = LogUtils.getLogger();
   protected final PackOutput vanillaPackOutput;
   protected final Set<String> allProviderIds = new HashSet<>();
   protected final Map<String, DataProvider> providersToRun = new LinkedHashMap<>();

   public DataGenerator(final Path output) {
      this.vanillaPackOutput = new PackOutput(output);
   }

   public abstract void run() throws IOException;

   public DataGenerator$PackGenerator getVanillaPack(final boolean toRun) {
      return new DataGenerator$PackGenerator(this, toRun, "vanilla", this.vanillaPackOutput);
   }

   public DataGenerator$PackGenerator getBuiltinDatapack(final boolean toRun, final String packId) {
      Path packOutputDir = this.vanillaPackOutput.getOutputFolder(PackOutput$Target.DATA_PACK).resolve("minecraft").resolve("datapacks").resolve(packId);
      return new DataGenerator$PackGenerator(this, toRun, packId, new PackOutput(packOutputDir));
   }

   static {
      Bootstrap.bootStrap();
   }
}
