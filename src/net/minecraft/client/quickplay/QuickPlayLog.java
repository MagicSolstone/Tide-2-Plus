package net.minecraft.client.quickplay;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class QuickPlayLog {
   private static final QuickPlayLog INACTIVE = new QuickPlayLog$1("");
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final Gson GSON = new GsonBuilder().create();
   private final Path path;
   @Nullable
   private QuickPlayLog$QuickPlayWorld worldData;

   private QuickPlayLog(final String quickPlayPath) {
      this.path = Minecraft.getInstance().gameDirectory.toPath().resolve(quickPlayPath);
   }

   public static QuickPlayLog of(@Nullable final String path) {
      return path == null ? INACTIVE : new QuickPlayLog(path);
   }

   public void setWorldData(final QuickPlayLog$Type type, final String id, final String name) {
      this.worldData = new QuickPlayLog$QuickPlayWorld(type, id, name);
   }

   public void log(final Minecraft minecraft) {
      if (minecraft.gameMode != null && this.worldData != null) {
         Util.ioPool()
            .execute(
               () -> {
                  try {
                     Files.deleteIfExists(this.path);
                  } catch (IOException var3) {
                     LOGGER.error("Failed to delete quickplay log file {}", this.path, var3);
                  }

                  QuickPlayLog$QuickPlayEntry quickPlayEntry = new QuickPlayLog$QuickPlayEntry(
                     this.worldData, Instant.now(), minecraft.gameMode.getPlayerMode()
                  );
                  Codec.list(QuickPlayLog$QuickPlayEntry.CODEC)
                     .encodeStart(JsonOps.INSTANCE, List.of(quickPlayEntry))
                     .resultOrPartial(Util.prefix("Quick Play: ", LOGGER::error))
                     .ifPresent(json -> {
                        try {
                           Files.createDirectories(this.path.getParent());
                           Files.writeString(this.path, GSON.toJson(json));
                        } catch (IOException var3x) {
                           LOGGER.error("Failed to write to quickplay log file {}", this.path, var3x);
                        }
                     });
               }
            );
      } else {
         LOGGER.error("Failed to log session for quickplay. Missing world data or gamemode");
      }
   }
}
