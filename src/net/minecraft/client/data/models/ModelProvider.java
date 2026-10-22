package net.minecraft.client.data.models;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.PackOutput$PathProvider;
import net.minecraft.data.PackOutput$Target;

@Environment(EnvType.CLIENT)
public class ModelProvider implements DataProvider {
   private final PackOutput$PathProvider blockStatePathProvider;
   private final PackOutput$PathProvider itemInfoPathProvider;
   private final PackOutput$PathProvider modelPathProvider;

   public ModelProvider(final PackOutput output) {
      this.blockStatePathProvider = output.createPathProvider(PackOutput$Target.RESOURCE_PACK, "blockstates");
      this.itemInfoPathProvider = output.createPathProvider(PackOutput$Target.RESOURCE_PACK, "items");
      this.modelPathProvider = output.createPathProvider(PackOutput$Target.RESOURCE_PACK, "models");
   }

   @Override
   public CompletableFuture<?> run(final CachedOutput cache) {
      ModelProvider$ItemInfoCollector itemModels = new ModelProvider$ItemInfoCollector();
      ModelProvider$BlockStateGeneratorCollector blockStateGenerators = new ModelProvider$BlockStateGeneratorCollector();
      ModelProvider$SimpleModelCollector simpleModels = new ModelProvider$SimpleModelCollector();
      new BlockModelGenerators(blockStateGenerators, itemModels, simpleModels).run();
      new ItemModelGenerators(itemModels, simpleModels).run();
      blockStateGenerators.validate();
      itemModels.finalizeAndValidate();
      return CompletableFuture.allOf(
         blockStateGenerators.save(cache, this.blockStatePathProvider),
         simpleModels.save(cache, this.modelPathProvider),
         itemModels.save(cache, this.itemInfoPathProvider)
      );
   }

   @Override
   public final String getName() {
      return "Model Definitions";
   }
}
