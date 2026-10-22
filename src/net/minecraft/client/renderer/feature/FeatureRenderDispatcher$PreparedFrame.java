package net.minecraft.client.renderer.feature;

import com.mojang.renderpearl.api.commands.RenderPass;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.feature.phase.FeatureRenderPhase;
import net.minecraft.client.renderer.feature.submit.SubmitNode;
import net.minecraft.client.renderer.oit.OitStage;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class FeatureRenderDispatcher$PreparedFrame implements AutoCloseable {
   @Nullable
   private FeatureFrameContext context;
   @Nullable
   private SubmitNodeStorage submitNodeStorage;
   private final List<SubmitNode> allSubmits;
   private final Map<FeatureRenderPhase<?>, List<FeatureRenderDispatcher$PreparedGroup<?>>> groupsByPhase;
   private final Map<FeatureRendererType<?>, List<FeatureRenderDispatcher$PreparedGroup<?>>> groupsByFeature;

   public FeatureRenderDispatcher$PreparedFrame(final FeatureRenderDispatcher this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.allSubmits = new ArrayList<>();
      this.groupsByPhase = new IdentityHashMap<>();
      this.groupsByFeature = new IdentityHashMap<>();
   }

   private FeatureRenderDispatcher$PreparedFrame begin(final FeatureFrameContext context, final SubmitNodeStorage submitNodeStorage) {
      if (this.context != null) {
         throw new IllegalStateException("PreparedFrame already in use");
      } else {
         this.context = context;
         this.submitNodeStorage = submitNodeStorage;
         return this;
      }
   }

   public void executeSolid(final RenderPass renderPass) {
      FeatureFrameContext context = Objects.requireNonNull(this.context);
      SubmitNodeStorage submitNodeStorage = Objects.requireNonNull(this.submitNodeStorage);
      ObjectIterator var4 = submitNodeStorage.getSubmitsPerOrder().values().iterator();

      while (var4.hasNext()) {
         SubmitNodeCollection collection = (SubmitNodeCollection)var4.next();
         this.executePhase(collection.solid, context, renderPass);
      }
   }

   public void executeTranslucent(final RenderPass renderPass) {
      FeatureFrameContext context = Objects.requireNonNull(this.context);
      SubmitNodeStorage submitNodeStorage = Objects.requireNonNull(this.submitNodeStorage);
      ObjectIterator var4 = submitNodeStorage.getSubmitsPerOrder().values().iterator();

      while (var4.hasNext()) {
         SubmitNodeCollection collection = (SubmitNodeCollection)var4.next();
         this.executePhase(collection.shadows, context, renderPass);
         this.executePhase(collection.translucentModels, context, renderPass);
         this.executePhase(collection.nameTags, context, renderPass);
         this.executePhase(collection.texts, context, renderPass);
         this.executePhase(collection.translucentCustomGeometry, context, renderPass);
      }

      var4 = submitNodeStorage.getSubmitsPerOrder().values().iterator();

      while (var4.hasNext()) {
         SubmitNodeCollection collection = (SubmitNodeCollection)var4.next();
         this.executePhase(collection.shapeOutlines, context, renderPass);
         this.executePhase(collection.translucentGizmos, context, renderPass);
      }

      var4 = submitNodeStorage.getSubmitsPerOrder().values().iterator();

      while (var4.hasNext()) {
         SubmitNodeCollection collection = (SubmitNodeCollection)var4.next();
         this.executePhase(collection.translucentBlocksAndItems, context, renderPass);
         this.executePhase(collection.breakingOverlay, context, renderPass);
         this.executePhase(collection.waterMask, context, renderPass);
      }
   }

   public void executeWaterMask(final RenderPass renderPass) {
      FeatureFrameContext context = Objects.requireNonNull(this.context);
      SubmitNodeStorage submitNodeStorage = Objects.requireNonNull(this.submitNodeStorage);
      ObjectIterator var4 = submitNodeStorage.getSubmitsPerOrder().values().iterator();

      while (var4.hasNext()) {
         SubmitNodeCollection collection = (SubmitNodeCollection)var4.next();
         this.executePhase(collection.waterMask, context, renderPass);
      }
   }

   public void executeOit(final OitStage stage, final RenderPass renderPass) {
      FeatureFrameContext context = Objects.requireNonNull(this.context);
      SubmitNodeStorage submitNodeStorage = Objects.requireNonNull(this.submitNodeStorage);
      ObjectIterator var5 = submitNodeStorage.getSubmitsPerOrder().values().iterator();

      while (var5.hasNext()) {
         SubmitNodeCollection collection = (SubmitNodeCollection)var5.next();
         this.executePhase(collection.oitTranslucent, context, stage, renderPass);
      }
   }

   public void executeOutline(final RenderPass renderPass) {
      FeatureFrameContext context = Objects.requireNonNull(this.context);
      SubmitNodeStorage submitNodeStorage = Objects.requireNonNull(this.submitNodeStorage);
      ObjectIterator var4 = submitNodeStorage.getSubmitsPerOrder().values().iterator();

      while (var4.hasNext()) {
         SubmitNodeCollection collection = (SubmitNodeCollection)var4.next();
         this.executePhase(collection.outline, context, renderPass);
      }
   }

   public void executeTranslucentAfterTerrain(final RenderPass renderPass) {
      FeatureFrameContext context = Objects.requireNonNull(this.context);
      SubmitNodeStorage submitNodeStorage = Objects.requireNonNull(this.submitNodeStorage);
      ObjectIterator var4 = submitNodeStorage.getSubmitsPerOrder().values().iterator();

      while (var4.hasNext()) {
         SubmitNodeCollection collection = (SubmitNodeCollection)var4.next();
         this.executePhase(collection.afterTerrain, context, renderPass);
      }
   }

   public void executeSeeThrough(final RenderPass renderPass) {
      FeatureFrameContext context = Objects.requireNonNull(this.context);
      SubmitNodeStorage submitNodeStorage = Objects.requireNonNull(this.submitNodeStorage);
      this.executePhase(submitNodeStorage.seeThrough(), context, renderPass);
   }

   public void executeAlwaysOnTop(final RenderPass renderPass) {
      FeatureFrameContext context = Objects.requireNonNull(this.context);
      SubmitNodeStorage submitNodeStorage = Objects.requireNonNull(this.submitNodeStorage);
      ObjectIterator var4 = submitNodeStorage.getSubmitsPerOrder().values().iterator();

      while (var4.hasNext()) {
         SubmitNodeCollection collection = (SubmitNodeCollection)var4.next();
         this.executePhase(collection.alwaysOnTopGizmos, context, renderPass);
      }
   }

   private void executePhase(final FeatureRenderPhase<?> phase, final FeatureFrameContext context, final RenderPass renderPass) {
      this.executePhase(phase, context, null, renderPass);
   }

   private void executePhase(final FeatureRenderPhase<?> phase, final FeatureFrameContext context, @Nullable final OitStage stage, final RenderPass renderPass) {
      ProfilerFiller profiler = Profiler.get();

      for (FeatureRenderDispatcher$PreparedGroup<?> group : this.groupsByPhase.getOrDefault(phase, List.of())) {
         String featureTypeName = group.featureType.toString();
         profiler.push(featureTypeName);
         renderPass.pushDebugGroup(() -> featureTypeName);
         group.execute(context, stage, renderPass, this.this$0.featureRenderers, this.allSubmits);
         renderPass.popDebugGroup();
         profiler.pop();
      }
   }

   public boolean hasAnyOutline() {
      SubmitNodeStorage submitNodeStorage = Objects.requireNonNull(this.submitNodeStorage);
      ObjectIterator var2 = submitNodeStorage.getSubmitsPerOrder().values().iterator();

      while (var2.hasNext()) {
         SubmitNodeCollection collection = (SubmitNodeCollection)var2.next();
         if (!this.groupsByPhase.getOrDefault(collection.outline, List.of()).isEmpty()) {
            return true;
         }
      }

      return false;
   }

   public boolean hasAnyWaterMask() {
      SubmitNodeStorage submitNodeStorage = Objects.requireNonNull(this.submitNodeStorage);
      ObjectIterator var2 = submitNodeStorage.getSubmitsPerOrder().values().iterator();

      while (var2.hasNext()) {
         SubmitNodeCollection collection = (SubmitNodeCollection)var2.next();
         if (!this.groupsByPhase.getOrDefault(collection.waterMask, List.of()).isEmpty()) {
            return true;
         }
      }

      return false;
   }

   public boolean hasAnySeeThrough() {
      SubmitNodeStorage submitNodeStorage = Objects.requireNonNull(this.submitNodeStorage);
      return !this.groupsByPhase.getOrDefault(submitNodeStorage.seeThrough(), List.of()).isEmpty();
   }

   public boolean isEmpty() {
      return this.allSubmits.isEmpty();
   }

   @Override
   public void close() {
      FeatureFrameContext context = Objects.requireNonNull(this.context, "Frame not in use");
      this.context = null;
      this.submitNodeStorage = null;

      for (FeatureRenderer<?> featureRenderer : this.this$0.featureRenderers.values()) {
         featureRenderer.finishExecute(context);
      }

      this.this$0.stagedVertexBuffer.endDraw();
      this.allSubmits.clear();
      clearGroups(this.groupsByPhase.values());
      clearGroups(this.groupsByFeature.values());
   }

   private static void clearGroups(final Collection<List<FeatureRenderDispatcher$PreparedGroup<?>>> groupsSet) {
      groupsSet.removeIf(groups -> {
         if (groups.isEmpty()) {
            return true;
         } else {
            groups.clear();
            return false;
         }
      });
   }
}
