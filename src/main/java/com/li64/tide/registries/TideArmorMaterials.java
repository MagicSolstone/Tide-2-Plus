package com.li64.tide.registries;
import com.li64.tide.Tide;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class TideArmorMaterials {
    public static final Holder<ArmorMaterial> DRAGONFIN = register("dragonfin", Util.make(new EnumMap<>(ArmorItem.Type.class),enumMap -> {
        enumMap.put(ArmorItem.Type.BOOTS, 5);
        enumMap.put(ArmorItem.Type.LEGGINGS, 6);
        enumMap.put(ArmorItem.Type.CHESTPLATE, 8);
        enumMap.put(ArmorItem.Type.HELMET, 3);
        enumMap.put(ArmorItem.Type.BODY, 11);
    }), 12, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0f, 0.1f, () -> Ingredient.of(Items.DRAGON_BREATH));

    private static Holder<ArmorMaterial> register(
            String name,
            EnumMap<ArmorItem.Type, Integer> defense,
            int enchantmentValue,
            Holder<SoundEvent> equipSound,
            float toughness,
            float knockbackResistance,
            Supplier<Ingredient> repairIngredient
    ) {
        List<ArmorMaterial.Layer> list = List.of(new ArmorMaterial.Layer(Tide.resource(name)));
        return register(name, defense, enchantmentValue, equipSound, toughness, knockbackResistance, repairIngredient, list);
    }

    private static Holder<ArmorMaterial> register(
            String name,
            EnumMap<ArmorItem.Type, Integer> defense,
            int enchantmentValue,
            Holder<SoundEvent> equipSound,
            float toughness,
            float knockbackResistance,
            Supplier<Ingredient> repairIngredient,
            List<ArmorMaterial.Layer> layers
    ) {
        EnumMap<ArmorItem.Type, Integer> enumMap = new EnumMap<>(ArmorItem.Type.class);
        for (ArmorItem.Type type : ArmorItem.Type.values()) enumMap.put(type, defense.get(type));
        return Tide.PLATFORM.registerForHolder(BuiltInRegistries.ARMOR_MATERIAL, Tide.resource(name),
                new ArmorMaterial(enumMap, enchantmentValue, equipSound, repairIngredient, layers, toughness, knockbackResistance));
    }

    public static void init() {}
}