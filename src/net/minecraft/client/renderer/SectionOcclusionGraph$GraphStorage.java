package net.minecraft.client.renderer;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class SectionOcclusionGraph$GraphStorage {
   public final SectionOcclusionGraph$SectionToNodeMap sectionToNodeMap;
   public final Octree sectionTree;
   public final Long2ObjectMap<LongList> sectionsWaitingForChunkLoads;

   public SectionOcclusionGraph$GraphStorage(final ViewArea viewArea) {
      this.sectionToNodeMap = new SectionOcclusionGraph$SectionToNodeMap(viewArea.size());
      this.sectionTree = new Octree(viewArea.getCameraSectionPos(), viewArea.getViewDistance(), viewArea.sectionCount(), viewArea.minY());
      this.sectionsWaitingForChunkLoads = new Long2ObjectOpenHashMap();
   }
}
