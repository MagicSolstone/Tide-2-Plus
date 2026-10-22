package net.minecraft.client.renderer.feature;

import com.mojang.renderpearl.api.commands.RenderPass;
import java.util.List;
import java.util.Map.Entry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.StagedVertexBuffer;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.state.GameRenderState;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;

@Environment(EnvType.CLIENT)
public class FeatureRenderDispatcher implements AutoCloseable {
   private final ModelManager modelManager;
   private final AtlasManager atlasManager;
   private final Font font;
   private final GameRenderState gameRenderState;
   private final StagedVertexBuffer stagedVertexBuffer;
   private final FeatureRendererMap featureRenderers = new FeatureRendererMap();
   private final FeatureRenderDispatcher$PreparedFrame preparedFrame = new FeatureRenderDispatcher$PreparedFrame(this);

   public FeatureRenderDispatcher(
      final RenderBuffers renderBuffers,
      final ModelManager modelManager,
      final AtlasManager atlasManager,
      final Font font,
      final GameRenderState gameRenderState
   ) {
      this.modelManager = modelManager;
      this.atlasManager = atlasManager;
      this.font = font;
      this.gameRenderState = gameRenderState;
      this.stagedVertexBuffer = renderBuffers.stagedVertexBuffer();
      this.featureRenderers.put(ShadowFeatureRenderer.TYPE, new ShadowFeatureRenderer());
      this.featureRenderers.put(FlameFeatureRenderer.TYPE, new FlameFeatureRenderer());
      this.featureRenderers.put(ModelFeatureRenderer.TYPE, new ModelFeatureRenderer());
      this.featureRenderers.put(TextFeatureRenderer.TYPE, new TextFeatureRenderer());
      this.featureRenderers.put(LeashFeatureRenderer.TYPE, new LeashFeatureRenderer());
      this.featureRenderers.put(ItemFeatureRenderer.TYPE, new ItemFeatureRenderer());
      this.featureRenderers.put(CustomFeatureRenderer.TYPE, new CustomFeatureRenderer());
      this.featureRenderers.put(BlockModelFeatureRenderer.TYPE, new BlockModelFeatureRenderer());
      this.featureRenderers.put(MovingBlockFeatureRenderer.TYPE, new MovingBlockFeatureRenderer());
      this.featureRenderers.put(QuadParticleFeatureRenderer.TYPE, new QuadParticleFeatureRenderer());
      this.featureRenderers.put(ShapeOutlineFeatureRenderer.TYPE, new ShapeOutlineFeatureRenderer());
      this.featureRenderers.put(GizmoFeatureRenderer.TYPE, new GizmoFeatureRenderer());
   }

   public FeatureRenderDispatcher$PreparedFrame prepareFrame(final SubmitNodeStorage submitNodeStorage) {
      Minecraft minecraft = Minecraft.getInstance();
      return this.prepareFrameWithContext(
         new FeatureFrameContext(
            this.gameRenderState.optionsRenderState,
            this.font,
            this.modelManager.getBlockStateModelSet(),
            minecraft.getBlockColors(),
            minecraft.getTextureManager(),
            this.atlasManager,
            minecraft.gameRenderer.lightmap(),
            this.stagedVertexBuffer
         ),
         submitNodeStorage
      );
   }

   private FeatureRenderDispatcher$PreparedFrame prepareFrameWithContext(final FeatureFrameContext context, final SubmitNodeStorage submitNodeStorage) {
      FeatureRenderDispatcher$PreparedFrame frame = this.preparedFrame.begin(context, submitNodeStorage);
      ProfilerFiller profiler = Profiler.get();
      profiler.push("sort");
      submitNodeStorage.drainPhases(phase -> phase.sortInto(new FeatureRenderDispatcher$PhaseSubmitGrouper(frame, phase)));
      profiler.popPush("beginPrepare");

      for (FeatureRenderer<?> renderer : this.featureRenderers.values()) {
         renderer.beginPrepare(context);
      }

      profiler.popPush("prepare");

      for (Entry<FeatureRendererType<?>, List<FeatureRenderDispatcher$PreparedGroup<?>>> entry : frame.groupsByFeature.entrySet()) {
         profiler.push(entry.getKey().toString());

         for (FeatureRenderDispatcher$PreparedGroup<?> group : entry.getValue()) {
            group.prepare(context, this.featureRenderers, frame.allSubmits);
         }

         profiler.pop();
      }

      profiler.popPush("finishPrepare");

      for (FeatureRenderer<?> renderer : this.featureRenderers.values()) {
         renderer.finishPrepare(context);
      }

      profiler.popPush("uploadSharedVertexBuffer");
      this.stagedVertexBuffer.upload();
      profiler.pop();
      return frame;
   }

   public static void renderAllFeatures(final RenderPass renderPass, final FeatureRenderDispatcher$PreparedFrame frame) {
      frame.executeSolid(renderPass);
      frame.executeTranslucent(renderPass);
      frame.executeTranslucentAfterTerrain(renderPass);
      frame.executeSeeThrough(renderPass);
      frame.executeAlwaysOnTop(renderPass);
   }

   @Override
   public void close() {
      this.featureRenderers.close();
   }
}
