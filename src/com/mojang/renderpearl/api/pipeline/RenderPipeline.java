package com.mojang.renderpearl.api.pipeline;

import com.mojang.renderpearl.api.vertex.VertexFormat;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import it.unimi.dsi.fastutil.objects.ReferenceLists;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.renderer.ShaderDefines;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class RenderPipeline {
   private final Identifier location;
   private final Map<ShaderType, Identifier> shaders;
   private final ShaderDefines shaderDefines;
   private final List<BindGroupLayout> bindGroupLayouts;
   @Nullable
   private final DepthStencilState depthStencilState;
   private final PolygonMode polygonMode;
   private final boolean cull;
   private final List<ColorTargetState> colorTargetStates;
   private final List<VertexFormat> vertexFormatPerBuffer;
   private final PrimitiveTopology primitiveTopology;
   private final int pushConstantSize;
   private final int sortKey;
   private static int sortKeySeed;

   protected RenderPipeline(
      final Identifier location,
      final Map<ShaderType, Identifier> shaders,
      final ShaderDefines shaderDefines,
      final Collection<BindGroupLayout> bindGroupLayouts,
      final ColorTargetState[] colorTargetStates,
      @Nullable final DepthStencilState depthStencilState,
      final PolygonMode polygonMode,
      final boolean cull,
      final VertexFormat[] vertexFormatPerBuffer,
      final PrimitiveTopology primitiveTopology,
      final int pushConstantSize,
      final int sortKey
   ) {
      this.location = location;
      this.shaders = Collections.unmodifiableMap(new EnumMap<>(shaders));
      this.shaderDefines = shaderDefines;
      this.bindGroupLayouts = List.copyOf(bindGroupLayouts);
      this.depthStencilState = depthStencilState;
      this.polygonMode = polygonMode;
      this.cull = cull;
      this.colorTargetStates = ReferenceLists.unmodifiable(new ReferenceArrayList(colorTargetStates));
      this.vertexFormatPerBuffer = ReferenceLists.unmodifiable(new ReferenceArrayList(vertexFormatPerBuffer));
      this.primitiveTopology = primitiveTopology;
      this.pushConstantSize = pushConstantSize;
      this.sortKey = sortKey;
   }

   public int getSortKey() {
      return SharedConstants.DEBUG_SHUFFLE_UI_RENDERING_ORDER ? super.hashCode() * (sortKeySeed + 1) : this.sortKey;
   }

   public static void updateSortKeySeed() {
      sortKeySeed = Math.round(100000.0F * (float)Math.random());
   }

   @Override
   public String toString() {
      return this.location.toString();
   }

   public PolygonMode getPolygonMode() {
      return this.polygonMode;
   }

   public boolean isCull() {
      return this.cull;
   }

   public List<ColorTargetState> getColorTargetStates() {
      return this.colorTargetStates;
   }

   @Nullable
   public DepthStencilState getDepthStencilState() {
      return this.depthStencilState;
   }

   public Identifier getLocation() {
      return this.location;
   }

   public List<VertexFormat> getVertexFormatBindings() {
      return this.vertexFormatPerBuffer;
   }

   @Nullable
   public VertexFormat getVertexFormatBinding(final int bindingIndex) {
      return this.vertexFormatPerBuffer.get(bindingIndex);
   }

   public PrimitiveTopology getPrimitiveTopology() {
      return this.primitiveTopology;
   }

   public Map<ShaderType, Identifier> getShaders() {
      return this.shaders;
   }

   public ShaderDefines getShaderDefines() {
      return this.shaderDefines;
   }

   public List<BindGroupLayout> getBindGroupLayouts() {
      return this.bindGroupLayouts;
   }

   public boolean wantsDepthTexture() {
      return this.depthStencilState != null;
   }

   public int pushConstantSize() {
      return this.pushConstantSize;
   }

   public static RenderPipeline$Builder builder(final RenderPipeline$Snippet... snippets) {
      RenderPipeline$Builder builder = new RenderPipeline$Builder();

      for (RenderPipeline$Snippet snippet : snippets) {
         builder.withSnippet(snippet);
      }

      return builder;
   }
}
