package net.minecraft.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack$Pose;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.Objects;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.cuboid.ItemTransform;
import net.minecraft.client.resources.model.geometry.ItemQuads;
import net.minecraft.client.resources.model.sprite.Material$Baked;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ItemStackRenderState$LayerRenderState {
   private static final Vector3fc[] NO_EXTENTS = new Vector3fc[0];
   public static final Supplier<Vector3fc[]> NO_EXTENTS_SUPPLIER = () -> NO_EXTENTS;
   public static final int[] EMPTY_TINTS = new int[0];
   private ItemQuads quads;
   private boolean usesBlockLight;
   @Nullable
   private Material$Baked particleMaterial;
   private ItemTransform itemTransform;
   private final Matrix4f localTransform;
   private ItemStackRenderState$FoilType foilType;
   @Nullable
   private IntList tintLayers;
   @Nullable
   private SpecialModelRenderer<Object> specialRenderer;
   @Nullable
   private Object argumentForSpecialRendering;
   private Supplier<Vector3fc[]> extents;

   public ItemStackRenderState$LayerRenderState(final ItemStackRenderState this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.quads = ItemQuads.EMPTY;
      this.itemTransform = ItemTransform.NO_TRANSFORM;
      this.localTransform = new Matrix4f();
      this.foilType = ItemStackRenderState$FoilType.NONE;
      this.extents = NO_EXTENTS_SUPPLIER;
   }

   public void clear() {
      this.quads = ItemQuads.EMPTY;
      this.foilType = ItemStackRenderState$FoilType.NONE;
      this.specialRenderer = null;
      this.argumentForSpecialRendering = null;
      if (this.tintLayers != null) {
         this.tintLayers.clear();
      }

      this.usesBlockLight = false;
      this.particleMaterial = null;
      this.itemTransform = ItemTransform.NO_TRANSFORM;
      this.localTransform.identity();
      this.extents = NO_EXTENTS_SUPPLIER;
   }

   public void setQuads(final ItemQuads quads) {
      this.quads = quads;
   }

   public void setUsesBlockLight(final boolean usesBlockLight) {
      this.usesBlockLight = usesBlockLight;
   }

   public void setExtents(final Supplier<Vector3fc[]> extents) {
      this.extents = extents;
   }

   public void setParticleMaterial(final Material$Baked particleMaterial) {
      this.particleMaterial = particleMaterial;
   }

   public void setItemTransform(final ItemTransform transform) {
      this.itemTransform = transform;
   }

   public void setLocalTransform(final Matrix4fc transform) {
      this.localTransform.set(transform);
   }

   public <T> void setupSpecialModel(final SpecialModelRenderer<T> renderer, @Nullable final T argument) {
      this.specialRenderer = eraseSpecialRenderer(renderer);
      this.argumentForSpecialRendering = argument;
   }

   private static SpecialModelRenderer<Object> eraseSpecialRenderer(final SpecialModelRenderer<?> renderer) {
      return (SpecialModelRenderer<Object>)renderer;
   }

   public void setFoilType(final ItemStackRenderState$FoilType foilType) {
      this.foilType = foilType;
   }

   public IntList tintLayers() {
      if (this.tintLayers == null) {
         this.tintLayers = new IntArrayList();
      }

      return this.tintLayers;
   }

   private void submit(
      final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final int lightCoords, final int overlayCoords, final int outlineColor
   ) {
      poseStack.pushPose();
      this.applyTransform(poseStack.last());
      if (this.specialRenderer != null) {
         this.specialRenderer
            .submit(
               this.argumentForSpecialRendering,
               poseStack,
               submitNodeCollector,
               lightCoords,
               overlayCoords,
               this.foilType != ItemStackRenderState$FoilType.NONE,
               outlineColor
            );
      } else {
         int[] tints = this.tintLayers != null ? this.tintLayers.toArray(EMPTY_TINTS) : EMPTY_TINTS;
         submitNodeCollector.submitItem(poseStack, this.this$0.displayContext, lightCoords, overlayCoords, outlineColor, tints, this.quads, this.foilType);
      }

      poseStack.popPose();
   }

   private void applyTransform(final PoseStack$Pose localPose) {
      this.itemTransform.apply(this.this$0.displayContext.leftHand(), localPose);
      localPose.mulPose(this.localTransform);
   }
}
