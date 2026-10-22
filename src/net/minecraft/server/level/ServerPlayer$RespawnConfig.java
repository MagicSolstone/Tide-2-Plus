package net.minecraft.server.level;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelData$RespawnData;
import org.jspecify.annotations.Nullable;

public record ServerPlayer$RespawnConfig(LevelData$RespawnData respawnData, boolean forced) {
   public static final Codec<ServerPlayer$RespawnConfig> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LevelData$RespawnData.MAP_CODEC.forGetter(ServerPlayer$RespawnConfig::respawnData),
            Codec.BOOL.optionalFieldOf("forced", false).forGetter(ServerPlayer$RespawnConfig::forced)
         )
         .apply(i, ServerPlayer$RespawnConfig::new)
   );

   private static ResourceKey<Level> getDimensionOrDefault(@Nullable final ServerPlayer$RespawnConfig respawnConfig) {
      return respawnConfig != null ? respawnConfig.respawnData().dimension() : Level.OVERWORLD;
   }

   public boolean isSamePosition(@Nullable final ServerPlayer$RespawnConfig other) {
      return other != null && this.respawnData.globalPos().equals(other.respawnData.globalPos());
   }
}
