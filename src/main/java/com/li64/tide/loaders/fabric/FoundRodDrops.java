package com.li64.tide.loaders.fabric;

import com.li64.tide.registries.TideItems;
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchBlock;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

/**
 * The rods that come from the world rather than a crafting table: rare drops from mobs a player kills
 * (Looting helps), from fully grown wheat a player harvests, and from leaves (broken by a player, or,
 * far more rarely, falling off on their own).
 */
public final class FoundRodDrops {
    private FoundRodDrops() {}

    public static final float ZOMBIE = 1 / 400f, SPIDER = 1 / 300f, DROWNED = 1 / 300f, WITCH = 1 / 150f, LOOTING = 1 / 1000f;
    public static final float WHEAT = 1 / 1500f, LEAVES_BY_HAND = 1 / 3000f, LEAVES_DECAY = 1 / 40000f;

    public static void modify(ResourceKey<LootTable> key, LootTable.Builder table, HolderLookup.Provider registries) {
        var enchantments = registries.lookupOrThrow(Registries.ENCHANTMENT);
        mob(key, table, EntityTypes.ZOMBIE, TideItems.ROTTEN_FISHING_ROD, ZOMBIE, enchantments);
        mob(key, table, EntityTypes.HUSK, TideItems.ROTTEN_FISHING_ROD, ZOMBIE, enchantments);
        mob(key, table, EntityTypes.SPIDER, TideItems.WIDOW_FISHING_ROD, SPIDER, enchantments);
        mob(key, table, EntityTypes.CAVE_SPIDER, TideItems.WIDOW_FISHING_ROD, SPIDER, enchantments);
        mob(key, table, EntityTypes.DROWNED, TideItems.DROWNED_FISHING_ROD, DROWNED, enchantments);
        mob(key, table, EntityTypes.WITCH, TideItems.HEX_FISHING_ROD, WITCH, enchantments);

        LootItemCondition.Builder byPlayer = LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                EntityPredicate.Builder.entity().of(registries.lookupOrThrow(Registries.ENTITY_TYPE), EntityTypes.PLAYER));
        if (Blocks.WHEAT.getLootTable().map(key::equals).orElse(false)) {
            table.pool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
                    .add(LootItem.lootTableItem(TideItems.SICKLE_FISHING_ROD))
                    .when(MatchBlock.blockMatches(registries.lookupOrThrow(Registries.BLOCK), Blocks.WHEAT,
                            StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, CropBlock.MAX_AGE)))
                    .when(byPlayer)
                    .when(LootItemRandomChanceCondition.randomChance(WHEAT))
                    .build());
        }
        if (isLeaves(key)) {
            table.pool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
                    .add(LootItem.lootTableItem(TideItems.GROVE_FISHING_ROD))
                    .when(byPlayer)
                    .when(LootItemRandomChanceCondition.randomChance(LEAVES_BY_HAND))
                    .build());
            table.pool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
                    .add(LootItem.lootTableItem(TideItems.GROVE_FISHING_ROD))
                    .when(InvertedLootItemCondition.invert(byPlayer))
                    .when(LootItemRandomChanceCondition.randomChance(LEAVES_DECAY))
                    .build());
        }
    }

    private static void mob(ResourceKey<LootTable> key, LootTable.Builder table, EntityType<?> type, Item rod, float chance,
                            HolderLookup.RegistryLookup<net.minecraft.world.item.enchantment.Enchantment> enchantments) {
        if (!type.getDefaultLootTable().map(key::equals).orElse(false)) return;
        table.pool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
                .add(LootItem.lootTableItem(rod))
                .when(LootItemKilledByPlayerCondition.killedByPlayer())
                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(enchantments, chance, LOOTING))
                .build());
    }

    private static boolean isLeaves(ResourceKey<LootTable> key) {
        for (Block block : BuiltInRegistries.BLOCK)
            if (block instanceof LeavesBlock && block.getLootTable().map(key::equals).orElse(false)) return true;
        return false;
    }
}
