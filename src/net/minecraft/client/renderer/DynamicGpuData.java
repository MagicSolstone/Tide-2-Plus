package net.minecraft.client.renderer;

import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class DynamicGpuData implements AutoCloseable {
   private static final Vector4fc WHITE = new Vector4f(1.0F, 1.0F, 1.0F, 1.0F);
   private static final Vector3fc NO_OFFSET = new Vector3f();
   private static final Matrix4fc IDENTITY_TEXTURE_TRANSFORM = new Matrix4f();
   public static final int TRANSFORM_UBO_SIZE = new Std140SizeCalculator().putMat4f().putMat4f().putVec4().putVec3().get();
   public static final int TERRAIN_TRANSFORM_UBO_SIZE = new Std140SizeCalculator().putMat4f().putIVec2().get();
   public static final int CHUNK_SECTION_UBO_SIZE = new Std140SizeCalculator().putIVec3().putFloat().get();
   private static final int INITIAL_CAPACITY = 2;
   private final DynamicGpuDataStorageMapped<DynamicGpuData$Transform> transforms;
   private final DynamicGpuDataStorageMapped<DynamicGpuData$TerrainTransform> terrain;
   @Nullable
   private DynamicGpuDataStorage<DynamicGpuData$ChunkSectionInfo> chunkSections = null;
   @Nullable
   private DynamicGpuDataStorageMapped<DynamicGpuData$IndexedDraw> chunkSectionsCommandBuffer = null;

   public DynamicGpuData() {
      this.transforms = new DynamicGpuDataStorageMapped<>("Dynamic Transforms UBO", TRANSFORM_UBO_SIZE, 128, 2);
      this.terrain = new DynamicGpuDataStorageMapped<>("Terrain UBO", TERRAIN_TRANSFORM_UBO_SIZE, 128, 1);
   }

   public void reset() {
      this.transforms.endFrame();
      this.terrain.endFrame();
      if (this.chunkSections != null) {
         this.chunkSections.endFrame();
      }

      if (this.chunkSectionsCommandBuffer != null) {
         this.chunkSectionsCommandBuffer.endFrame();
      }
   }

   @Override
   public void close() {
      this.transforms.close();
      this.terrain.close();
      if (this.chunkSections != null) {
         this.chunkSections.close();
      }

      if (this.chunkSectionsCommandBuffer != null) {
         this.chunkSectionsCommandBuffer.close();
      }
   }

   public GpuBufferSlice writeTransform(final Matrix4f modelView) {
      return this.writeTransform(new DynamicGpuData$Transform(modelView, WHITE, NO_OFFSET, IDENTITY_TEXTURE_TRANSFORM));
   }

   public GpuBufferSlice writeTransform(final Matrix4f modelView, final Vector4f colorModulator) {
      return this.writeTransform(new DynamicGpuData$Transform(modelView, colorModulator, NO_OFFSET, IDENTITY_TEXTURE_TRANSFORM));
   }

   public GpuBufferSlice writeTransform(final Matrix4f modelView, final Matrix4f textureMatrix) {
      return this.writeTransform(new DynamicGpuData$Transform(modelView, WHITE, NO_OFFSET, textureMatrix));
   }

   public GpuBufferSlice writeTransform(final Matrix4f modelView, final Vector4f colorModulator, final Vector3f modelOffset, final Matrix4f textureMatrix) {
      return this.writeTransform(new DynamicGpuData$Transform(modelView, colorModulator, modelOffset, textureMatrix));
   }

   public GpuBufferSlice writeTransform(final DynamicGpuData$Transform transform) {
      return this.transforms.writeData(transform);
   }

   public GpuBufferSlice[] writeTransforms(final DynamicGpuData$Transform... transforms) {
      return this.transforms.writeData(transforms);
   }

   public GpuBufferSlice writeTerrainTransform(final Matrix4fc modelView, final int textureAtlasWidth, final int textureAtlasHeight) {
      return this.terrain.writeData(new DynamicGpuData$TerrainTransform(modelView, textureAtlasWidth, textureAtlasHeight));
   }

   public GpuBufferSlice[] writeChunkSections(final DynamicGpuData$ChunkSectionInfo... infos) {
      if (this.chunkSections != null && (this.chunkSections.usage() & 128) == 0) {
         this.chunkSections.close();
         this.chunkSections = null;
      }

      if (this.chunkSections == null) {
         this.chunkSections = new DynamicGpuDataStorageMapped<>("Chunk Sections UBO", CHUNK_SECTION_UBO_SIZE, 128, 2);
      }

      return this.chunkSections.writeData(infos);
   }

   public GpuBufferSlice writeChunkSectionsInstanced(final List<DynamicGpuData$ChunkSectionInfo> infos) {
      if (this.chunkSections != null && (this.chunkSections.usage() & 32) == 0) {
         this.chunkSections.close();
         this.chunkSections = null;
      }

      if (this.chunkSections == null) {
         this.chunkSections = new DynamicGpuDataStorageNonMapped<>("Chunk Sections Instanced", CHUNK_SECTION_UBO_SIZE, 32, 2);
      }

      return this.chunkSections.writeDataBatched(infos);
   }

   public GpuBufferSlice[] writeChunkSectionCommands(final List<List<DynamicGpuData$IndexedDraw>> draws) {
      if (this.chunkSectionsCommandBuffer == null) {
         this.chunkSectionsCommandBuffer = new DynamicGpuDataStorageMapped<>("Chunk Sections Command Buffer", 20, 512, 2);
      }

      return this.chunkSectionsCommandBuffer.writeDataBatchedMultiple(draws);
   }
}
