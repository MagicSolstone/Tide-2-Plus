package net.minecraft.client.renderer.item;

import com.mojang.math.Transformation;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvableModel$Resolver;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4fc;

@Environment(EnvType.CLIENT)
public record CuboidItemModelWrapper$Unbaked(Identifier model, Optional<Transformation> transformation, List<ItemTintSource> tints)
   implements ItemModel$Unbaked {
   public static final MapCodec<CuboidItemModelWrapper$Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            Identifier.CODEC.fieldOf("model").forGetter(CuboidItemModelWrapper$Unbaked::model),
            Transformation.EXTENDED_CODEC.optionalFieldOf("transformation").forGetter(CuboidItemModelWrapper$Unbaked::transformation),
            ItemTintSources.CODEC.listOf().optionalFieldOf("tints", List.of()).forGetter(CuboidItemModelWrapper$Unbaked::tints)
         )
         .apply(i, CuboidItemModelWrapper$Unbaked::new)
   );

   @Override
   public void resolveDependencies(final ResolvableModel$Resolver resolver) {
      resolver.markDependency(this.model);
   }

   @Override
   public ItemModel bake(final ItemModel$BakingContext context, final Matrix4fc transformation) {
      ModelBaker baker = context.blockModelBaker();
      ResolvedModel resolvedModel = baker.getModel(this.model);
      TextureSlots textureSlots = resolvedModel.getTopTextureSlots();
      QuadCollection quads = resolvedModel.bakeTopGeometry(textureSlots, baker, BlockModelRotation.IDENTITY);
      ModelRenderProperties properties = ModelRenderProperties.fromResolvedModel(baker, resolvedModel, textureSlots);
      CuboidItemModelWrapper.validateAtlasUsage(quads.getAll());
      Matrix4fc modelTransform = Transformation.compose(transformation, this.transformation);
      return new CuboidItemModelWrapper(this.tints, quads, properties, modelTransform);
   }

   @Override
   public MapCodec<CuboidItemModelWrapper$Unbaked> type() {
      return MAP_CODEC;
   }
}
