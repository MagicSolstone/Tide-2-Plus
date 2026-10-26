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
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
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
        // items removed in 1.1.1: old ones in chests and on rods turn into what replaced them
        var items = (net.fabricmc.fabric.api.event.registry.FabricRegistry) (Object) net.minecraft.core.registries.BuiltInRegistries.ITEM;
        items.addAlias(com.li64.tide.Tide.resource("coal_line"), com.li64.tide.Tide.resource("fishing_line"));
        items.addAlias(com.li64.tide.Tide.resource("bone_hook"), com.li64.tide.Tide.resource("fish_bone_hook"));
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
                TideItems.getCreativeTab(FabricCreativeModeTab.builder()).build());

        TideEntityAttributes.REGISTRY.forEach(FabricEntrypoint::registerAttributes);

        // furnace fuel is an item component in 26.x
        DefaultItemComponentEvents.MODIFY.register(context -> {
            fuel(context, TideItems.WOODEN_CRATE, 300);
            fuel(context, TideItems.ANGLING_TABLE, 300);
            fuel(context, TideItems.FISH_DISPLAY, 300);
            fuel(context, TideItems.FISHY_NOTE, 200);
            fuel(context, TideFish.INFERNO_GUPPY, 3200);
        });

        TideData.onRegisterReloadListeners((id, listener) ->
                ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(id, listener));

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

    private static void fuel(DefaultItemComponentEvents.ModifyContext context, net.minecraft.world.item.Item item, int ticks) {
        context.modify(item, builder -> builder.set(net.minecraft.core.component.DataComponents.COOKING_FUEL,
                new net.minecraft.world.item.component.CookingFuel(
                        new net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt.Constant(ticks),
                        net.minecraft.world.level.storage.loot.providers.number.floats.ResolvableFloat.fromKey(
                                net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders.COOKING_DEFAULT_SPEED_MULTIPLIER))));
    }

    public static <T extends LivingEntity> void registerAttributes(TideEntityAttributes.Registration<T> reg) {
        FabricDefaultAttributeRegistry.register(reg.entityType(), reg.attributes());
    }
}