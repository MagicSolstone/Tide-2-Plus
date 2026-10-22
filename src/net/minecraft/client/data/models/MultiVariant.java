package net.minecraft.client.data.models;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel$Unbaked;
import net.minecraft.client.renderer.block.dispatch.SingleVariant$Unbaked;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.client.renderer.block.dispatch.WeightedVariants$Unbaked;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;

@Environment(EnvType.CLIENT)
public record MultiVariant(WeightedList<Variant> variants) {
   public MultiVariant(WeightedList<Variant> variants) {
      if (variants.isEmpty()) {
         throw new IllegalArgumentException("Variant list must contain at least one element");
      } else {
         this.variants = variants;
      }
   }

   public MultiVariant with(final VariantMutator mutator) {
      return new MultiVariant(this.variants.map(mutator));
   }

   public BlockStateModel$Unbaked toUnbaked() {
      List<Weighted<Variant>> entries = this.variants.unwrap();
      return (BlockStateModel$Unbaked)(entries.size() == 1
         ? new SingleVariant$Unbaked(entries.getFirst().value())
         : new WeightedVariants$Unbaked(this.variants.map(SingleVariant$Unbaked::new)));
   }
}
