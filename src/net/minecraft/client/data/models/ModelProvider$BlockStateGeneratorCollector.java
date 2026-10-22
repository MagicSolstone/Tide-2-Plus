package net.minecraft.client.data.models;

import com.google.common.collect.Maps;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelDispatcher;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput$PathProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

@Environment(EnvType.CLIENT)
class ModelProvider$BlockStateGeneratorCollector implements Consumer<BlockModelDefinitionGenerator> {
   private final Map<Block, BlockModelDefinitionGenerator> generators = new HashMap<>();

   private ModelProvider$BlockStateGeneratorCollector() {
   }

   public void accept(final BlockModelDefinitionGenerator generator) {
      Block block = generator.block();
      BlockModelDefinitionGenerator prev = this.generators.put(block, generator);
      if (prev != null) {
         throw new IllegalStateException("Duplicate blockstate definition for " + block);
      }
   }

   public void validate() {
      List<Identifier> missingDefinitions = BuiltInRegistries.BLOCK
         .listElements()
         .filter(e -> !this.generators.containsKey(e.value()))
         .map(e -> e.key().identifier())
         .toList();
      if (!missingDefinitions.isEmpty()) {
         throw new IllegalStateException("Missing blockstate definitions for: " + missingDefinitions);
      }
   }

   public CompletableFuture<?> save(final CachedOutput cache, final PackOutput$PathProvider pathProvider) {
      Map<Block, BlockStateModelDispatcher> definitions = Maps.transformValues(this.generators, BlockModelDefinitionGenerator::create);
      Function<Block, Path> pathGetter = block -> pathProvider.json(block.builtInRegistryHolder().key().identifier());
      return DataProvider.saveAll(cache, BlockStateModelDispatcher.CODEC, pathGetter, definitions);
   }
}
