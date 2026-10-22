package net.minecraft.client.renderer;

import com.mojang.blaze3d.vertex.ByteBufferBuilder$Result;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexSorting;
import com.mojang.renderpearl.api.pipeline.IndexType;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.vertex.VertexFormat;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class StagedVertexBuffer$Draw {
   private final VertexFormat format;
   private final PrimitiveTopology primitiveTopology;
   @Nullable
   private final VertexSorting quadSorting;
   private final List<ByteBufferBuilder$Result> vertexBufferSlices = new ArrayList<>();
   private int vertexBufferSize;
   private int vertexCount;
   private int indexCount;
   private int vertexOffset;
   private int indexOffset;

   private StagedVertexBuffer$Draw(final VertexFormat format, final PrimitiveTopology primitiveTopology, @Nullable final VertexSorting quadSorting) {
      this.format = format;
      this.primitiveTopology = primitiveTopology;
      this.quadSorting = quadSorting;
   }

   private void append(final MeshData mesh) {
      if (!$assertionsDisabled && mesh.indexBuffer() != null) {
         throw new AssertionError();
      } else {
         this.vertexBufferSlices.add(mesh.vertexBufferSlice());
         this.vertexBufferSize = this.vertexBufferSize + mesh.vertexBuffer().remaining();
         this.vertexCount = this.vertexCount + mesh.drawState().vertexCount();
         this.indexCount = this.indexCount + mesh.drawState().indexCount();
      }
   }

   private IndexType indexType() {
      return IndexType.least(this.vertexCount);
   }

   private void freeVertexData() {
      this.vertexBufferSlices.forEach(ByteBufferBuilder$Result::close);
      this.vertexBufferSlices.clear();
   }

   public boolean isEmpty() {
      return this.vertexCount == 0;
   }
}
