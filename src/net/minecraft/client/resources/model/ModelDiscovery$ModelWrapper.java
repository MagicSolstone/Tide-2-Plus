package net.minecraft.client.resources.model;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.resources.model.cuboid.ItemTransforms;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.geometry.UnbakedGeometry;
import net.minecraft.client.resources.model.sprite.Material$Baked;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class ModelDiscovery$ModelWrapper implements ResolvedModel {
   private static final ModelDiscovery$Slot<Boolean> KEY_AMBIENT_OCCLUSION = slot(0);
   private static final ModelDiscovery$Slot<UnbakedModel$GuiLight> KEY_GUI_LIGHT = slot(1);
   private static final ModelDiscovery$Slot<UnbakedGeometry> KEY_GEOMETRY = slot(2);
   private static final ModelDiscovery$Slot<ItemTransforms> KEY_TRANSFORMS = slot(3);
   private static final ModelDiscovery$Slot<TextureSlots> KEY_TEXTURE_SLOTS = slot(4);
   private static final ModelDiscovery$Slot<Material$Baked> KEY_PARTICLE_SPRITE = slot(5);
   private static final ModelDiscovery$Slot<QuadCollection> KEY_DEFAULT_GEOMETRY = slot(6);
   private static final int SLOT_COUNT = 7;
   private final Identifier id;
   private boolean valid;
   @Nullable
   private ModelDiscovery$ModelWrapper parent;
   private final UnbakedModel wrapped;
   private final AtomicReferenceArray<Object> fixedSlots = new AtomicReferenceArray<>(7);
   private final Map<ModelState, QuadCollection> modelBakeCache = new ConcurrentHashMap<>();

   private static <T> ModelDiscovery$Slot<T> slot(final int index) {
      Objects.checkIndex(index, 7);
      return new ModelDiscovery$Slot<>(index);
   }

   private ModelDiscovery$ModelWrapper(final Identifier id, final UnbakedModel wrapped, final boolean valid) {
      this.id = id;
      this.wrapped = wrapped;
      this.valid = valid;
   }

   @Override
   public UnbakedModel wrapped() {
      return this.wrapped;
   }

   @Nullable
   @Override
   public ResolvedModel parent() {
      return this.parent;
   }

   @Override
   public String debugName() {
      return this.id.toString();
   }

   @Nullable
   private <T> T getSlot(final ModelDiscovery$Slot<T> key) {
      return (T)this.fixedSlots.get(key.index);
   }

   private <T> T updateSlot(final ModelDiscovery$Slot<T> key, final T value) {
      T currentValue = (T)this.fixedSlots.compareAndExchange(key.index, null, value);
      return currentValue == null ? value : currentValue;
   }

   private <T> T getSimpleProperty(final ModelDiscovery$Slot<T> key, final Function<ResolvedModel, T> getter) {
      T result = this.getSlot(key);
      return result != null ? result : this.updateSlot(key, getter.apply(this));
   }

   @Override
   public boolean getTopAmbientOcclusion() {
      return this.getSimpleProperty(KEY_AMBIENT_OCCLUSION, ResolvedModel::findTopAmbientOcclusion);
   }

   @Override
   public UnbakedModel$GuiLight getTopGuiLight() {
      return this.getSimpleProperty(KEY_GUI_LIGHT, ResolvedModel::findTopGuiLight);
   }

   @Override
   public ItemTransforms getTopTransforms() {
      return this.getSimpleProperty(KEY_TRANSFORMS, ResolvedModel::findTopTransforms);
   }

   @Override
   public UnbakedGeometry getTopGeometry() {
      return this.getSimpleProperty(KEY_GEOMETRY, ResolvedModel::findTopGeometry);
   }

   @Override
   public TextureSlots getTopTextureSlots() {
      return this.getSimpleProperty(KEY_TEXTURE_SLOTS, ResolvedModel::findTopTextureSlots);
   }

   @Override
   public Material$Baked resolveParticleMaterial(final TextureSlots textureSlots, final ModelBaker baker) {
      Material$Baked result = this.getSlot(KEY_PARTICLE_SPRITE);
      return result != null ? result : this.updateSlot(KEY_PARTICLE_SPRITE, ResolvedModel.resolveParticleMaterial(textureSlots, baker, this));
   }

   private QuadCollection bakeDefaultState(final TextureSlots textureSlots, final ModelBaker baker, final ModelState state) {
      QuadCollection result = this.getSlot(KEY_DEFAULT_GEOMETRY);
      return result != null ? result : this.updateSlot(KEY_DEFAULT_GEOMETRY, this.getTopGeometry().bake(textureSlots, baker, state, this));
   }

   @Override
   public QuadCollection bakeTopGeometry(final TextureSlots textureSlots, final ModelBaker baker, final ModelState state) {
      return state == BlockModelRotation.IDENTITY ? this.bakeDefaultState(textureSlots, baker, state) : this.modelBakeCache.computeIfAbsent(state, s -> {
         UnbakedGeometry topGeometry = this.getTopGeometry();
         return topGeometry.bake(textureSlots, baker, s, this);
      });
   }
}
