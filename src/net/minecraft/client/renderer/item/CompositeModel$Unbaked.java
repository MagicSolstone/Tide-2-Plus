package net.minecraft.client.renderer.item;

import com.mojang.math.Transformation;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.ResolvableModel$Resolver;
import org.joml.Matrix4fc;

@Environment(EnvType.CLIENT)
public record CompositeModel$Unbaked(List<ItemModel$Unbaked> models, Optional<Transformation> transformation) implements ItemModel$Unbaked {
   public static final MapCodec<CompositeModel$Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            ItemModels.CODEC.listOf().fieldOf("models").forGetter(CompositeModel$Unbaked::models),
            Transformation.EXTENDED_CODEC.optionalFieldOf("transformation").forGetter(CompositeModel$Unbaked::transformation)
         )
         .apply(i, CompositeModel$Unbaked::new)
   );

   @Override
   public MapCodec<CompositeModel$Unbaked> type() {
      return MAP_CODEC;
   }

   @Override
   public void resolveDependencies(final ResolvableModel$Resolver resolver) {
      for (ItemModel$Unbaked model : this.models) {
         model.resolveDependencies(resolver);
      }
   }

   @Override
   public ItemModel bake(final ItemModel$BakingContext context, final Matrix4fc transformation) {
      if (this.models.isEmpty()) {
         return EmptyModel.INSTANCE;
      } else {
         Matrix4fc childTransform = Transformation.compose(transformation, this.transformation);
         return (ItemModel)(this.models.size() == 1
            ? this.models.getFirst().bake(context, childTransform)
            : new CompositeModel(this.models.stream().map(m -> m.bake(context, childTransform)).toList()));
      }
   }
}
