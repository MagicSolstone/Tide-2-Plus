package net.minecraft.client.renderer.chunk;

import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.MeshData$SortState;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public final class SectionCompiler$Results {
   public final List<BlockEntity> blockEntities = new ArrayList<>();
   public final Map<ChunkSectionLayer, MeshData> renderedLayers = new EnumMap<>(ChunkSectionLayer.class);
   public VisibilitySet visibilitySet = new VisibilitySet();
   @Nullable
   public MeshData$SortState transparencyState;

   public void release() {
      this.renderedLayers.values().forEach(MeshData::close);
   }
}
