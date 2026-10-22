package net.minecraft.client.data.models.blockstates;

import java.util.stream.Stream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;

@Environment(EnvType.CLIENT)
record MultiVariantGenerator$Entry(PropertyValueList properties, MultiVariant variant) {
   public Stream<MultiVariantGenerator$Entry> apply(final PropertyDispatch<VariantMutator> stage) {
      return stage.getEntries().entrySet().stream().map(property -> {
         PropertyValueList newSelector = this.properties.extend(property.getKey());
         MultiVariant newVariants = this.variant.with(property.getValue());
         return new MultiVariantGenerator$Entry(newSelector, newVariants);
      });
   }

   public Stream<MultiVariantGenerator$Entry> apply(final VariantMutator mutator) {
      return Stream.of(new MultiVariantGenerator$Entry(this.properties, this.variant.with(mutator)));
   }
}
