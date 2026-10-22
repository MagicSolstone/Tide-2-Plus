package net.minecraft.world.level.lighting;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.world.level.chunk.DataLayer;

public final class BlockLightSectionStorage$BlockDataLayerStorageMap extends DataLayerStorageMap<BlockLightSectionStorage$BlockDataLayerStorageMap> {
   public BlockLightSectionStorage$BlockDataLayerStorageMap(final Long2ObjectOpenHashMap<DataLayer> map) {
      super(map);
   }

   public BlockLightSectionStorage$BlockDataLayerStorageMap copy() {
      return new BlockLightSectionStorage$BlockDataLayerStorageMap(this.map.clone());
   }
}
