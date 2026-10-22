package net.minecraft.client.renderer.block.dispatch;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvableModel$Resolver;
import net.minecraft.util.random.WeightedList;

@Environment(EnvType.CLIENT)
public record WeightedVariants$Unbaked(WeightedList<BlockStateModel$Unbaked> entries) implements BlockStateModel$Unbaked {
   @Override
   public BlockStateModel bake(final ModelBaker modelBakery) {
      return new WeightedVariants(this.entries.map(m -> m.bake(modelBakery)));
   }

   @Override
   public void resolveDependencies(final ResolvableModel$Resolver resolver) {
      this.entries.unwrap().forEach(v -> v.value().resolveDependencies(resolver));
   }
}
