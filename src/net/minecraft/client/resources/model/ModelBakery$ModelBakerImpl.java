package net.minecraft.client.resources.model;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.sprite.MaterialBaker;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
class ModelBakery$ModelBakerImpl implements ModelBaker {
   private final MaterialBaker materials;
   private final ModelBaker$Interner interner;
   private final ModelBakery$MissingModels missingModels;
   private final Map<ModelBaker$SharedOperationKey<Object>, Object> operationCache;
   private final Function<ModelBaker$SharedOperationKey<Object>, Object> cacheComputeFunction;

   private ModelBakery$ModelBakerImpl(
      final ModelBakery this$0, final MaterialBaker materials, final ModelBaker$Interner interner, final ModelBakery$MissingModels missingModels
   ) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.operationCache = new ConcurrentHashMap<>();
      this.cacheComputeFunction = k -> k.compute(this);
      this.materials = materials;
      this.interner = interner;
      this.missingModels = missingModels;
   }

   @Override
   public BlockStateModelPart missingBlockModelPart() {
      return this.missingModels.blockPart;
   }

   @Override
   public MaterialBaker materials() {
      return this.materials;
   }

   @Override
   public ModelBaker$Interner interner() {
      return this.interner;
   }

   @Override
   public ResolvedModel getModel(final Identifier location) {
      ResolvedModel result = this.this$0.resolvedModels.get(location);
      if (result == null) {
         ModelBakery.LOGGER.warn("Requested a model that was not discovered previously: {}", location);
         return this.this$0.missingModel;
      } else {
         return result;
      }
   }

   @Override
   public <T> T compute(final ModelBaker$SharedOperationKey<T> key) {
      return (T)this.operationCache.computeIfAbsent(key, this.cacheComputeFunction);
   }
}
