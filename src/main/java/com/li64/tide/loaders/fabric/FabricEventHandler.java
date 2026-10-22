package com.li64.tide.loaders.fabric;

import com.li64.tide.data.TidePlayer;
import com.li64.tide.data.commands.FishingCommand;
import com.li64.tide.data.commands.JournalCommand;
import com.li64.tide.data.fishing.FishData;
import com.li64.tide.events.TideEventHandler;
import com.li64.tide.registries.TideItems;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.world.item.trading.ItemCost;

public class FabricEventHandler {
    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            new JournalCommand(dispatcher, registryAccess);
            new FishingCommand(dispatcher, registryAccess);
            new com.li64.tide.data.commands.FishingLevelCommand(dispatcher);
        });

        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            CompoundTag data = ((TidePlayer) oldPlayer).tide$getTidePlayerData();
            ((TidePlayer) newPlayer).tide$setTidePlayerData(data);
        });

        ServerLifecycleEvents.SERVER_STARTED.register(server -> TideEventHandler.serverStarted());

        ServerTickEvents.END_SERVER_TICK.register(TideEventHandler::endServerTick);

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                TideEventHandler.onPlayerJoinWorld(handler.getPlayer()));

        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (key == BuiltInLootTables.FISHING_JUNK) {
                tableBuilder.modifyPools(builder -> builder
                        .add(LootItem.lootTableItem(TideItems.FISH_BONE).setWeight(8)));
            }

            if (key == BuiltInLootTables.UNDERWATER_RUIN_BIG || key == BuiltInLootTables.UNDERWATER_RUIN_SMALL) {
                tableBuilder.pool(new LootPool.Builder().setRolls(ContextIntProviders.between(1, 2))
                        .add(LootItem.lootTableItem(TideItems.BAIT).setWeight(20)
                                .apply(SetItemCountFunction.setCount(ContextIntProviders.exactly(4))))
                        .build()
                );
            }

            if (key == BuiltInLootTables.SHIPWRECK_SUPPLY) {
                tableBuilder.pool(new LootPool.Builder().setRolls(ContextIntProviders.between(1, 2))
                        .add(LootItem.lootTableItem(TideItems.BAIT).setWeight(10)
                                .apply(SetItemCountFunction.setCount(ContextIntProviders.exactly(4))))
                        .add(LootItem.lootTableItem(TideItems.LUCKY_BAIT).setWeight(10)
                                .apply(SetItemCountFunction.setCount(ContextIntProviders.exactly(4))))
                        .build()
                );
            }

            if (key == BuiltInLootTables.BURIED_TREASURE) {
                tableBuilder.pool(new LootPool.Builder().setRolls(ContextIntProviders.exactly(2))
                        .add(LootItem.lootTableItem(TideItems.LUCKY_BAIT).setWeight(5)
                                .apply(SetItemCountFunction.setCount(ContextIntProviders.exactly(4))))
                        .add(LootItem.lootTableItem(TideItems.MAGNETIC_BAIT).setWeight(5)
                                .apply(SetItemCountFunction.setCount(ContextIntProviders.exactly(6))))
                        .build()
                );
            }

            if (key == BuiltInLootTables.ANCIENT_CITY) {
                tableBuilder.pool(new LootPool.Builder().setRolls(ContextIntProviders.exactly(1))
                        .add(LootItem.lootTableItem(TideItems.ECHO_FISHING_ROD))
                                .when(LootItemRandomChanceCondition.randomChance(0.1f))
                        .build()
                );
            }

            if (key == BuiltInLootTables.NETHER_BRIDGE) {
                tableBuilder.pool(new LootPool.Builder().setRolls(ContextIntProviders.exactly(1))
                        .add(LootItem.lootTableItem(TideItems.BLAZING_FISHING_ROD))
                        .when(LootItemRandomChanceCondition.randomChance(0.3f))
                        .build()
                );
            }

            if (EntityTypes.ELDER_GUARDIAN.getDefaultLootTable().map(key::equals).orElse(false)) {
                tableBuilder.pool(new LootPool.Builder().setRolls(ContextIntProviders.exactly(1))
                        .add(LootItem.lootTableItem(TideItems.PRISMARINE_FISHING_ROD))
                        .when(LootItemRandomChanceCondition.randomChance(0.5f))
                        .build()
                );
            }
        });

        // (the Village Fishing Rod trade is data now: data/tide/villager_trade/fisherman/)

        LootTableEvents.ALL_LOADED.register((resourceManager, registry) ->
                FishData.VANILLA_FISH_TABLE = registry.getValue(BuiltInLootTables.FISHING_FISH));
    }
}