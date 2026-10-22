package net.minecraft.client.renderer.item;

import com.mojang.math.Transformation;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.ResolvableModel$Resolver;
import org.joml.Matrix4fc;

@Environment(EnvType.CLIENT)
public record SelectItemModel$Unbaked(
   Optional<Transformation> transformation, SelectItemModel$UnbakedSwitch<?, ?> unbakedSwitch, Optional<ItemModel$Unbaked> fallback
) implements ItemModel$Unbaked {
   public static final MapCodec<SelectItemModel$Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            Transformation.EXTENDED_CODEC.optionalFieldOf("transformation").forGetter(SelectItemModel$Unbaked::transformation),
            SelectItemModel$UnbakedSwitch.MAP_CODEC.forGetter(SelectItemModel$Unbaked::unbakedSwitch),
            ItemModels.CODEC.optionalFieldOf("fallback").forGetter(SelectItemModel$Unbaked::fallback)
         )
         .apply(i, SelectItemModel$Unbaked::new)
   );

   @Override
   public MapCodec<SelectItemModel$Unbaked> type() {
      return MAP_CODEC;
   }

   @Override
   public ItemModel bake(final ItemModel$BakingContext context, final Matrix4fc transformation) {
      Matrix4fc childTransform = Transformation.compose(transformation, this.transformation);
      ItemModel bakedFallback = this.fallback.<ItemModel>map(m -> m.bake(context, childTransform)).orElseGet(() -> context.missingItemModel(childTransform));
      return this.unbakedSwitch.bake(context, childTransform, bakedFallback);
   }

   @Override
   public void resolveDependencies(final ResolvableModel$Resolver resolver) {
      this.unbakedSwitch.resolveDependencies(resolver);
      this.fallback.ifPresent(m -> m.resolveDependencies(resolver));
   }
}
