package com.li64.tide.loaders.fabric;

import com.li64.tide.Tide;
import com.li64.tide.client.gui.TideMenuTypes;
import com.li64.tide.data.TideCriteriaTriggers;
import com.li64.tide.data.TideData;
import com.li64.tide.data.TideRecipeSerializers;
import com.li64.tide.network.TideMessages;
import com.li64.tide.registries.*;
import com.li64.tide.registries.entities.TideSpawnConfigs;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.NotNull;

import com.li64.tide.data.item.TideDataComponents;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class FabricEntrypoint implements ModInitializer {
    @Override
    public void onInitialize() {
        Tide.initialize();
        TideMessages.init(Tide.NETWORK);
        FabricEventHandler.init();

        TideRegistries.init();
        TideItems.init();
        TideBlocks.init();
        TideBlockEntities.init();
        TideEntityTypes.init();
        TideMenuTypes.init();
        TideSoundEvents.init();
        TideParticleTypes.init();
        TideLootFunctions.init();
        TideCriteriaTriggers.init();
        TideRecipeSerializers.init();
        TideArmorMaterials.init();
        TideDataComponents.init();

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Tide.resource("tide"),
                TideItems.getCreativeTab(FabricItemGroup.builder()).build());

        TideEntityAttributes.REGISTRY.forEach(FabricEntrypoint::registerAttributes);

        FuelRegistry.INSTANCE.add(TideItems.WOODEN_CRATE, 300);
        FuelRegistry.INSTANCE.add(TideItems.ANGLING_TABLE, 300);
        FuelRegistry.INSTANCE.add(TideItems.FISH_DISPLAY, 300);
        FuelRegistry.INSTANCE.add(TideItems.FISHY_NOTE, 200);
        FuelRegistry.INSTANCE.add(TideFish.INFERNO_GUPPY, 3200);

        TideData.onRegisterReloadListeners((id, listener) -> ResourceManagerHelper.get(PackType.SERVER_DATA)
            .registerReloadListener(new IdentifiableResourceReloadListener() {
                @Override
                public Identifier getFabricId() {
                    return id;
                }

                @Override
                public @NotNull CompletableFuture<Void> reload(PreparationBarrier synchronizer, ResourceManager manager,
                                                               ProfilerFiller prepareProfiler, ProfilerFiller applyProfiler,
                                                               Executor prepareExecutor, Executor applyExecutor) {
                    return listener.reload(synchronizer, manager, prepareProfiler, applyProfiler, prepareExecutor, applyExecutor);
                }
            }));

        TideSpawnConfigs.onRegisterSpawnConfigs(config -> {
            BiomeModifications.addSpawn(
                    BiomeSelectors.tag(config.biomes()),
                    config.mobCategory(), config.entityType(),
                    config.weight(), config.minGroup(), config.maxGroup()
            );
            SpawnPlacements.register(
                    config.entityType(),
                    config.placementType(),
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    config.spawnPredicate()
            );
        });
    }

    public static <T extends LivingEntity> void registerAttributes(TideEntityAttributes.Registration<T> reg) {
        FabricDefaultAttributeRegistry.register(reg.entityType(), reg.attributes());
    }
}