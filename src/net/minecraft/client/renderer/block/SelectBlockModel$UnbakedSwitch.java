package net.minecraft.client.renderer.block;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.BlockModel$BakingContext;
import net.minecraft.client.renderer.block.model.BlockModel$Unbaked;
import net.minecraft.client.renderer.block.model.properties.select.SelectBlockModelProperty;
import org.joml.Matrix4fc;

@Environment(EnvType.CLIENT)
public record SelectBlockModel$UnbakedSwitch<P extends SelectBlockModelProperty<T>, T>(P property, List<SelectBlockModel$SwitchCase<T>> cases) {
   public BlockModel bake(final BlockModel$BakingContext context, final Matrix4fc transformation, final BlockModel fallback) {
      Object2ObjectMap<T, BlockModel> bakedModels = new Object2ObjectOpenHashMap();

      for (SelectBlockModel$SwitchCase<T> c : this.cases) {
         BlockModel$Unbaked caseModel = c.model;
         BlockModel bakedCaseModel = caseModel.bake(context, transformation);

         for (T value : c.values) {
            bakedModels.put(value, bakedCaseModel);
         }
      }

      bakedModels.defaultReturnValue(fallback);
      return new SelectBlockModel<>(this.property, bakedModels::get);
   }
}
