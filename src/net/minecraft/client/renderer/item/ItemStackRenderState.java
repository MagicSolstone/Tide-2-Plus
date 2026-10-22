package net.minecraft.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack$Pose;
import java.util.Arrays;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.resources.model.sprite.Material$Baked;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.AABB$Builder;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ItemStackRenderState {
   ItemDisplayContext displayContext = ItemDisplayContext.NONE;
   private int activeLayerCount;
   private boolean animated;
   private boolean oversizedInGui;
   @Nullable
   private AABB cachedModelBoundingBox;
   private ItemStackRenderState$LayerRenderState[] layers = new ItemStackRenderState$LayerRenderState[]{new ItemStackRenderState$LayerRenderState(this)};

   public void ensureCapacity(final int requestedCount) {
      int currentCapacity = this.layers.length;
      int requiredNewCapacity = this.activeLayerCount + requestedCount;
      if (requiredNewCapacity > currentCapacity) {
         this.layers = Arrays.copyOf(this.layers, requiredNewCapacity);

         for (int i = currentCapacity; i < requiredNewCapacity; i++) {
            this.layers[i] = new ItemStackRenderState$LayerRenderState(this);
         }
      }
   }

   public ItemStackRenderState$LayerRenderState newLayer() {
      this.ensureCapacity(1);
      return this.layers[this.activeLayerCount++];
   }

   public void clear() {
      this.displayContext = ItemDisplayContext.NONE;

      for (int i = 0; i < this.activeLayerCount; i++) {
         this.layers[i].clear();
      }

      this.activeLayerCount = 0;
      this.animated = false;
      this.oversizedInGui = false;
      this.cachedModelBoundingBox = null;
   }

   public void setAnimated() {
      this.animated = true;
   }

   public boolean isAnimated() {
      return this.animated;
   }

   public void appendModelIdentityElement(final Object element) {
   }

   private ItemStackRenderState$LayerRenderState firstLayer() {
      return this.layers[0];
   }

   public boolean isEmpty() {
      return this.activeLayerCount == 0;
   }

   public boolean usesBlockLight() {
      return this.firstLayer().usesBlockLight;
   }

   @Nullable
   public Material$Baked pickParticleMaterial(final RandomSource randomSource) {
      return this.activeLayerCount == 0 ? null : this.layers[randomSource.nextInt(this.activeLayerCount)].particleMaterial;
   }

   public void visitExtents(final Consumer<Vector3fc> output) {
      Vector3f scratch = new Vector3f();
      PoseStack$Pose pose = new PoseStack$Pose();

      for (int i = 0; i < this.activeLayerCount; i++) {
         ItemStackRenderState$LayerRenderState layer = this.layers[i];
         layer.applyTransform(pose);
         Matrix4f poseTransform = pose.pose();
         Vector3fc[] layerExtents = layer.extents.get();

         for (Vector3fc extent : layerExtents) {
            output.accept(scratch.set(extent).mulPosition(poseTransform));
         }

         pose.setIdentity();
      }
   }

   public void submit(
      final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final int lightCoords, final int overlayCoords, final int outlineColor
   ) {
      for (int i = 0; i < this.activeLayerCount; i++) {
         this.layers[i].submit(poseStack, submitNodeCollector, lightCoords, overlayCoords, outlineColor);
      }
   }

   public AABB getModelBoundingBox() {
      if (this.cachedModelBoundingBox != null) {
         return this.cachedModelBoundingBox;
      } else {
         AABB$Builder collector = new AABB$Builder();
         this.visitExtents(collector::include);
         AABB aabb = collector.isDefined() ? collector.build() : AABB.ofSize(Vec3.ZERO, 0.0, 0.0, 0.0);
         this.cachedModelBoundingBox = aabb;
         return aabb;
      }
   }

   public void setOversizedInGui(final boolean oversizedInGui) {
      this.oversizedInGui = oversizedInGui;
   }

   public boolean isOversizedInGui() {
      return this.oversizedInGui;
   }
}
