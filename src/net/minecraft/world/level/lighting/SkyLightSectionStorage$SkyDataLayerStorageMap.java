package net.minecraft.world.level.lighting;

import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.world.level.chunk.DataLayer;

public final class SkyLightSectionStorage$SkyDataLayerStorageMap extends DataLayerStorageMap<SkyLightSectionStorage$SkyDataLayerStorageMap> {
   private int currentLowestY;
   private final Long2IntOpenHashMap topSections;

   public SkyLightSectionStorage$SkyDataLayerStorageMap(
      final Long2ObjectOpenHashMap<DataLayer> map, final Long2IntOpenHashMap topSections, final int currentLowestY
   ) {
      super(map);
      this.topSections = topSections;
      topSections.defaultReturnValue(currentLowestY);
      this.currentLowestY = currentLowestY;
   }

   public SkyLightSectionStorage$SkyDataLayerStorageMap copy() {
      return new SkyLightSectionStorage$SkyDataLayerStorageMap(this.map.clone(), this.topSections.clone(), this.currentLowestY);
   }
}
