package net.minecraft.client.resources.palette;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.mojang.logging.LogUtils;
import java.time.Duration;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class PaletteMappingCache {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final LoadingCache<PaletteMappingCache$IdPair, PaletteMapping> cache;

   public PaletteMappingCache(final Map<Identifier, Palette> palettes) {
      this.cache = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofMinutes(5L)).maximumSize(256L).build(new PaletteMappingCache$1(this, palettes));
   }

   public PaletteMapping get(final Identifier baseId, final Identifier targetId) {
      return (PaletteMapping)this.cache.getUnchecked(new PaletteMappingCache$IdPair(baseId, targetId));
   }
}
