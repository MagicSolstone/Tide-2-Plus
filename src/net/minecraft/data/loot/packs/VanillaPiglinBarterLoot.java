package net.minecraft.data.loot.packs;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.data.loot.LootTableSubProvider$Context;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction$Builder;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

public class VanillaPiglinBarterLoot implements LootTableSubProvider {
   private final LootTableSubProvider$Context output;
   private final HolderGetter<Enchantment> enchantments;

   public VanillaPiglinBarterLoot(final LootTableSubProvider$Context output) {
      this.output = output;
      this.enchantments = output.lookup(Registries.ENCHANTMENT);
   }

   @Override
   public void run() {
      this.output
         .accept(
            BuiltInLootTables.PIGLIN_BARTERING,
            LootTable.lootTable()
               .withPool(
                  LootPool.lootPool()
                     .setRolls(ContextIntProviders.exactly(1))
                     .add(
                        LootItem.lootTableItem(Items.BOOK)
                           .setWeight(5)
                           .apply(new EnchantRandomlyFunction$Builder().withEnchantment(this.enchantments.getOrThrow(Enchantments.SOUL_SPEED)))
                     )
                     .add(
                        LootItem.lootTableItem(Items.IRON_BOOTS)
                           .setWeight(8)
                           .apply(new EnchantRandomlyFunction$Builder().withEnchantment(this.enchantments.getOrThrow(Enchantments.SOUL_SPEED)))
                     )
                     .add(LootItem.lootTableItem(Items.POTION).setWeight(8).apply(SetPotionFunction.setPotion(Potions.FIRE_RESISTANCE)))
                     .add(LootItem.lootTableItem(Items.SPLASH_POTION).setWeight(8).apply(SetPotionFunction.setPotion(Potions.FIRE_RESISTANCE)))
                     .add(LootItem.lootTableItem(Items.POTION).setWeight(10).apply(SetPotionFunction.setPotion(Potions.WATER)))
                     .add(LootItem.lootTableItem(Items.IRON_NUGGET).setWeight(10).apply(SetItemCountFunction.setCount(ContextIntProviders.between(10, 36))))
                     .add(LootItem.lootTableItem(Items.ENDER_PEARL).setWeight(10).apply(SetItemCountFunction.setCount(ContextIntProviders.between(2, 4))))
                     .add(LootItem.lootTableItem(Items.DRIED_GHAST).setWeight(10).apply(SetItemCountFunction.setCount(ContextIntProviders.exactly(1))))
                     .add(LootItem.lootTableItem(Items.STRING).setWeight(20).apply(SetItemCountFunction.setCount(ContextIntProviders.between(3, 9))))
                     .add(LootItem.lootTableItem(Items.QUARTZ).setWeight(20).apply(SetItemCountFunction.setCount(ContextIntProviders.between(5, 12))))
                     .add(LootItem.lootTableItem(Items.OBSIDIAN).setWeight(40))
                     .add(LootItem.lootTableItem(Items.CRYING_OBSIDIAN).setWeight(40).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 3))))
                     .add(LootItem.lootTableItem(Items.FIRE_CHARGE).setWeight(40))
                     .add(LootItem.lootTableItem(Items.LEATHER).setWeight(40).apply(SetItemCountFunction.setCount(ContextIntProviders.between(2, 4))))
                     .add(LootItem.lootTableItem(Items.SOUL_SAND).setWeight(40).apply(SetItemCountFunction.setCount(ContextIntProviders.between(2, 8))))
                     .add(LootItem.lootTableItem(Items.NETHER_BRICK).setWeight(40).apply(SetItemCountFunction.setCount(ContextIntProviders.between(2, 8))))
                     .add(LootItem.lootTableItem(Items.SPECTRAL_ARROW).setWeight(40).apply(SetItemCountFunction.setCount(ContextIntProviders.between(6, 12))))
                     .add(LootItem.lootTableItem(Items.GRAVEL).setWeight(40).apply(SetItemCountFunction.setCount(ContextIntProviders.between(8, 16))))
                     .add(LootItem.lootTableItem(Items.BLACKSTONE).setWeight(40).apply(SetItemCountFunction.setCount(ContextIntProviders.between(8, 16))))
               )
         );
   }
}
