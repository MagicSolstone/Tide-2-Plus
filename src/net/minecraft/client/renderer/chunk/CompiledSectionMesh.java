package net.minecraft.client.renderer.chunk;

import com.mojang.blaze3d.vertex.MeshData$SortState;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;
import net.minecraft.util.Util;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class CompiledSectionMesh implements SectionMesh {
   public static final SectionMesh UNCOMPILED = new CompiledSectionMesh$1();
   public static final SectionMesh EMPTY = new CompiledSectionMesh$2();
   private final List<BlockEntity> renderableBlockEntities;
   private final VisibilitySet visibilitySet;
   @Nullable
   private final MeshData$SortState transparencyState;
   @Nullable
   private TranslucencyPointOfView translucencyPointOfView;
   private final Map<ChunkSectionLayer, SectionMesh$SectionDraw> draws = new EnumMap<>(ChunkSectionLayer.class);
   private final Map<ChunkSectionLayer, AtomicBoolean> vertexBufferUploaded = Util.makeEnumMap(ChunkSectionLayer.class, layer -> new AtomicBoolean());
   private final Map<ChunkSectionLayer, AtomicBoolean> indexBufferUploaded = Util.makeEnumMap(ChunkSectionLayer.class, layer -> new AtomicBoolean());
   private final long compileTaskStartTimeNs;

   public CompiledSectionMesh(final TranslucencyPointOfView translucencyPointOfView, final SectionCompiler$Results results, final long compileTaskStartTimeNs) {
      this.translucencyPointOfView = translucencyPointOfView;
      this.visibilitySet = results.visibilitySet;
      this.renderableBlockEntities = results.blockEntities;
      this.transparencyState = results.transparencyState;
      results.renderedLayers
         .forEach(
            (layer, mesh) -> this.draws
               .put(layer, new SectionMesh$SectionDraw(mesh.drawState().indexCount(), mesh.drawState().indexType(), mesh.indexBuffer() != null))
         );
      this.compileTaskStartTimeNs = compileTaskStartTimeNs;
   }

   public void setTranslucencyPointOfView(final TranslucencyPointOfView translucencyPointOfView) {
      this.translucencyPointOfView = translucencyPointOfView;
   }

   @Override
   public boolean isDifferentPointOfView(final TranslucencyPointOfView pointOfView) {
      return !pointOfView.equals(this.translucencyPointOfView);
   }

   @Override
   public boolean hasRenderableLayers() {
      return !this.draws.isEmpty();
   }

   @Override
   public boolean isEmpty(final ChunkSectionLayer layer) {
      return !this.draws.containsKey(layer);
   }

   @Override
   public List<BlockEntity> getRenderableBlockEntities() {
      return this.renderableBlockEntities;
   }

   @Override
   public boolean facesCanSeeEachother(final Direction direction1, final Direction direction2) {
      return this.visibilitySet.visibilityBetween(direction1, direction2);
   }

   @Nullable
   @Override
   public SectionMesh$SectionDraw getSectionDraw(final ChunkSectionLayer layer) {
      return this.draws.get(layer);
   }

   @Override
   public long getCompileTaskStartTime() {
      return this.compileTaskStartTimeNs;
   }

   public boolean isVertexBufferUploaded(final ChunkSectionLayer layer) {
      return this.vertexBufferUploaded.get(layer).get();
   }

   public boolean isIndexBufferUploaded(final ChunkSectionLayer layer) {
      return this.indexBufferUploaded.get(layer).get();
   }

   public void setVertexBufferUploaded(final ChunkSectionLayer layer) {
      this.vertexBufferUploaded.get(layer).set(true);
   }

   public void setIndexBufferUploaded(final ChunkSectionLayer layer) {
      this.indexBufferUploaded.get(layer).set(true);
   }

   @Override
   public boolean hasTranslucentGeometry() {
      return this.draws.containsKey(ChunkSectionLayer.TRANSLUCENT);
   }

   @Nullable
   public MeshData$SortState getTransparencyState() {
      return this.transparencyState;
   }

   @Override
   public void close() {
      this.draws.clear();
      this.vertexBufferUploaded.clear();
      this.indexBufferUploaded.clear();
   }
}
