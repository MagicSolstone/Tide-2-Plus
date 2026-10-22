package net.minecraft.client.renderer.item;

import com.mojang.math.Transformation;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperties;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.client.resources.model.ResolvableModel$Resolver;
import org.joml.Matrix4fc;

@Environment(EnvType.CLIENT)
public record RangeSelectItemModel$Unbaked(
   Optional<Transformation> transformation,
   RangeSelectItemModelProperty property,
   float scale,
   List<RangeSelectItemModel$Entry> entries,
   Optional<ItemModel$Unbaked> fallback
) implements ItemModel$Unbaked {
   public static final MapCodec<RangeSelectItemModel$Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            Transformation.EXTENDED_CODEC.optionalFieldOf("transformation").forGetter(RangeSelectItemModel$Unbaked::transformation),
            RangeSelectItemModelProperties.MAP_CODEC.forGetter(RangeSelectItemModel$Unbaked::property),
            Codec.FLOAT.optionalFieldOf("scale", 1.0F).forGetter(RangeSelectItemModel$Unbaked::scale),
            RangeSelectItemModel$Entry.CODEC.listOf().fieldOf("entries").forGetter(RangeSelectItemModel$Unbaked::entries),
            ItemModels.CODEC.optionalFieldOf("fallback").forGetter(RangeSelectItemModel$Unbaked::fallback)
         )
         .apply(i, RangeSelectItemModel$Unbaked::new)
   );

   @Override
   public MapCodec<RangeSelectItemModel$Unbaked> type() {
      return MAP_CODEC;
   }

   @Override
   public ItemModel bake(final ItemModel$BakingContext context, final Matrix4fc transformation) {
      Matrix4fc childTransform = Transformation.compose(transformation, this.transformation);
      float[] thresholds = new float[this.entries.size()];
      ItemModel[] models = new ItemModel[this.entries.size()];
      List<RangeSelectItemModel$Entry> mutableEntries = new ArrayList<>(this.entries);
      mutableEntries.sort(RangeSelectItemModel$Entry.BY_THRESHOLD);

      for (int i = 0; i < mutableEntries.size(); i++) {
         RangeSelectItemModel$Entry entry = mutableEntries.get(i);
         thresholds[i] = entry.threshold;
         models[i] = entry.model.bake(context, childTransform);
      }

      ItemModel bakedFallback = this.fallback.<ItemModel>map(m -> m.bake(context, childTransform)).orElseGet(() -> context.missingItemModel(childTransform));
      return new RangeSelectItemModel(this.property, this.scale, thresholds, models, bakedFallback);
   }

   @Override
   public void resolveDependencies(final ResolvableModel$Resolver resolver) {
      this.fallback.ifPresent(m -> m.resolveDependencies(resolver));
      this.entries.forEach(entry -> entry.model.resolveDependencies(resolver));
   }
}
