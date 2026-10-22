package net.minecraft.client.data.models.blockstates;

import java.util.List;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;

@Environment(EnvType.CLIENT)
public class MultiVariantGenerator$Empty {
   private final Block block;

   public MultiVariantGenerator$Empty(final Block block) {
      this.block = block;
   }

   public MultiVariantGenerator with(final PropertyDispatch<MultiVariant> newStage) {
      Set<Property<?>> newSeenProperties = MultiVariantGenerator.validateAndExpandProperties(Set.of(), this.block, newStage);
      List<MultiVariantGenerator$Entry> newEntries = newStage.getEntries()
         .entrySet()
         .stream()
         .map(e -> new MultiVariantGenerator$Entry(e.getKey(), e.getValue()))
         .toList();
      return new MultiVariantGenerator(this.block, newEntries, newSeenProperties);
   }
}
