package com.li64.tide.registries;

import com.li64.tide.Tide;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.EnumMap;
import java.util.Map;

/** In 26.x armor materials are plain values (no registry); the look comes from assets/tide/equipment/. */
public class TideArmorMaterials {
    public static final TagKey<Item> REPAIRS_DRAGONFIN = TagKey.create(Registries.ITEM, Tide.resource("repairs_dragonfin_armor"));
    public static final ResourceKey<EquipmentAsset> DRAGONFIN_ASSET = ResourceKey.create(EquipmentAssets.ROOT_ID, Tide.resource("dragonfin"));

    public static final ArmorMaterial DRAGONFIN = new ArmorMaterial(37, new EnumMap<>(Map.of(
            ArmorType.BOOTS, 5, ArmorType.LEGGINGS, 6, ArmorType.CHESTPLATE, 8, ArmorType.HELMET, 3, ArmorType.BODY, 11)),
            12, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0f, 0.1f, REPAIRS_DRAGONFIN, DRAGONFIN_ASSET);

    /** Fishing gear: leather-strength, worn with the game's own leather look in fishing colours (re-dyeable). */
    public static final ArmorMaterial ANGLER = new ArmorMaterial(8, new EnumMap<>(Map.of(
            ArmorType.BOOTS, 1, ArmorType.LEGGINGS, 2, ArmorType.CHESTPLATE, 3, ArmorType.HELMET, 1, ArmorType.BODY, 3)),
            15, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0f, 0.0f, net.minecraft.tags.ItemTags.REPAIRS_LEATHER_ARMOR, EquipmentAssets.LEATHER);

    public static void init() {}
}
