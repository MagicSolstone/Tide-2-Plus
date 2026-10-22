package net.minecraft.client.renderer.item;

import com.mojang.math.Transformation;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer$Unbaked;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvableModel$Resolver;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4fc;

@Environment(EnvType.CLIENT)
public record SpecialModelWrapper$Unbaked(Identifier base, Optional<Transformation> transformation, SpecialModelRenderer$Unbaked<?> specialModel)
   implements ItemModel$Unbaked {
   public static final MapCodec<SpecialModelWrapper$Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            Identifier.CODEC.fieldOf("base").forGetter(SpecialModelWrapper$Unbaked::base),
            Transformation.EXTENDED_CODEC.optionalFieldOf("transformation").forGetter(SpecialModelWrapper$Unbaked::transformation),
            SpecialModelRenderers.CODEC.fieldOf("model").forGetter(SpecialModelWrapper$Unbaked::specialModel)
         )
         .apply(i, SpecialModelWrapper$Unbaked::new)
   );

   @Override
   public void resolveDependencies(final ResolvableModel$Resolver resolver) {
      resolver.markDependency(this.base);
   }

   @Override
   public ItemModel bake(final ItemModel$BakingContext context, final Matrix4fc transformation) {
      Matrix4fc modelTransform = Transformation.compose(transformation, this.transformation);
      SpecialModelRenderer<?> bakedSpecialModel = this.specialModel.bake(context);
      if (bakedSpecialModel == null) {
         return context.missingItemModel(modelTransform);
      } else {
         ModelRenderProperties properties = this.getProperties(context);
         return new SpecialModelWrapper<>(bakedSpecialModel, properties, modelTransform);
      }
   }

   private ModelRenderProperties getProperties(final ItemModel$BakingContext context) {
      ModelBaker baker = context.blockModelBaker();
      ResolvedModel model = baker.getModel(this.base);
      TextureSlots textureSlots = model.getTopTextureSlots();
      return ModelRenderProperties.fromResolvedModel(baker, model, textureSlots);
   }

   @Override
   public MapCodec<SpecialModelWrapper$Unbaked> type() {
      return MAP_CODEC;
   }
}
