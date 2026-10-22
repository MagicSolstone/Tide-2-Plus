package net.minecraft.server;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Stream;
import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.core.HolderLookup$RegistryLookup;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.Registry$PendingTags;
import net.minecraft.core.RegistryAccess$Frozen;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.tags.TagLoader;
import net.minecraft.world.level.WorldDataConfiguration;

public class WorldLoader {
   public static <D, R> CompletableFuture<R> load(
      final WorldLoader$InitConfig config,
      final WorldLoader$WorldDataSupplier<D> worldDataSupplier,
      final WorldLoader$ResultFactory<D, R> resultFactory,
      final Executor backgroundExecutor,
      final Executor mainThreadExecutor
   ) {
      return CompletableFuture.supplyAsync(config.packConfig::createResourceManager, mainThreadExecutor)
         .thenComposeAsync(
            packsAndResourceManager -> {
               CloseableResourceManager resources = (CloseableResourceManager)packsAndResourceManager.getSecond();
               LayeredRegistryAccess<RegistryLayer> initialLayers = RegistryLayer.createRegistryAccess();
               List<Registry$PendingTags<?>> staticLayerTags = TagLoader.loadTagsForExistingRegistries(resources, initialLayers.getLayer(RegistryLayer.STATIC));
               RegistryAccess$Frozen worldLoadContext = initialLayers.getAccessForLoading(RegistryLayer.WORLD);
               List<HolderLookup$RegistryLookup<?>> worldContextRegistries = TagLoader.buildUpdatedLookups(worldLoadContext, staticLayerTags);
               return RegistryDataLoader.load(resources, worldContextRegistries, RegistryDataLoader.WORLD_REGISTRIES, backgroundExecutor)
                  .thenComposeAsync(
                     loadedWorldRegistries -> {
                        List<HolderLookup$RegistryLookup<?>> dimensionContextRegistries = Stream.concat(
                              worldContextRegistries.stream(), loadedWorldRegistries.listRegistries()
                           )
                           .toList();
                        return RegistryDataLoader.load(resources, dimensionContextRegistries, RegistryDataLoader.DIMENSION_REGISTRIES, backgroundExecutor)
                           .thenComposeAsync(
                              initialWorldgenDimensions -> {
                                 WorldDataConfiguration worldDataConfiguration = (WorldDataConfiguration)packsAndResourceManager.getFirst();
                                 HolderLookup$Provider dimensionContextProvider = HolderLookup$Provider.create(dimensionContextRegistries.stream());
                                 WorldLoader$DataLoadOutput<D> worldDataAndRegistries = worldDataSupplier.get(
                                    new WorldLoader$DataLoadContext(resources, worldDataConfiguration, dimensionContextProvider, initialWorldgenDimensions)
                                 );
                                 LayeredRegistryAccess<RegistryLayer> resourcesLoadContext = initialLayers.replaceFrom(
                                    RegistryLayer.WORLD, loadedWorldRegistries, worldDataAndRegistries.finalDimensions
                                 );
                                 return ReloadableServerResources.loadResources(
                                       resources,
                                       resourcesLoadContext,
                                       staticLayerTags,
                                       worldDataConfiguration.enabledFeatures(),
                                       config.commandSelection(),
                                       config.functionCompilationPermissions(),
                                       backgroundExecutor,
                                       mainThreadExecutor
                                    )
                                    .whenComplete((managers, throwable) -> {
                                       if (throwable != null) {
                                          resources.close();
                                       }
                                    })
                                    .thenApplyAsync(managers -> {
                                       managers.updateComponentsAndStaticRegistryTags();
                                       return resultFactory.create(resources, managers, resourcesLoadContext, worldDataAndRegistries.cookie);
                                    }, mainThreadExecutor);
                              },
                              backgroundExecutor
                           );
                     },
                     backgroundExecutor
                  );
            },
            backgroundExecutor
         );
   }
}
