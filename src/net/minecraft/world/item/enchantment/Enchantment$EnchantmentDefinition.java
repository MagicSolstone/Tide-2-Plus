package net.minecraft.world.item.enchantment;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;

public record Enchantment$EnchantmentDefinition(
   HolderSet<Item> supportedItems,
   Optional<HolderSet<Item>> primaryItems,
   int weight,
   int maxLevel,
   Enchantment$Cost minCost,
   Enchantment$Cost maxCost,
   int anvilCost,
   List<EquipmentSlotGroup> slots
) {
   public static final MapCodec<Enchantment$EnchantmentDefinition> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            RegistryCodecs.holderSet(Registries.ITEM).fieldOf("supported_items").forGetter(Enchantment$EnchantmentDefinition::supportedItems),
            RegistryCodecs.holderSet(Registries.ITEM).optionalFieldOf("primary_items").forGetter(Enchantment$EnchantmentDefinition::primaryItems),
            ExtraCodecs.intRange(1, 1024).fieldOf("weight").forGetter(Enchantment$EnchantmentDefinition::weight),
            ExtraCodecs.intRange(1, 255).fieldOf("max_level").forGetter(Enchantment$EnchantmentDefinition::maxLevel),
            Enchantment$Cost.CODEC.fieldOf("min_cost").forGetter(Enchantment$EnchantmentDefinition::minCost),
            Enchantment$Cost.CODEC.fieldOf("max_cost").forGetter(Enchantment$EnchantmentDefinition::maxCost),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("anvil_cost").forGetter(Enchantment$EnchantmentDefinition::anvilCost),
            EquipmentSlotGroup.CODEC.listOf().fieldOf("slots").forGetter(Enchantment$EnchantmentDefinition::slots)
         )
         .apply(i, Enchantment$EnchantmentDefinition::new)
   );
}
