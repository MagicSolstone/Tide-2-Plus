package com.li64.tide.registries;

import com.li64.tide.Tide;
import com.li64.tide.config.TideServerConfig;
import com.li64.tide.data.fishing.FishData;
import com.li64.tide.data.item.TideItemData;
import com.li64.tide.registries.blocks.JellyTorchBlockItem;
import com.li64.tide.registries.items.*;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;

import java.util.*;
import java.util.function.Function;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.BundleContents;

public class TideItems {
    public static final HashMap<ResourceKey<Item>, Item> ITEMS = new HashMap<>();
    private static final ArrayList<ItemStack> DISPLAY_ITEMS = new ArrayList<>();

    public static final Item STONE_FISHING_ROD = register("stone_fishing_rod", properties -> new TideFishingRodItem(1, 48, properties));
    public static final Item IRON_FISHING_ROD = register("iron_fishing_rod", properties -> new TideFishingRodItem(1, 64, properties));
    public static final Item GOLDEN_FISHING_ROD = register("golden_fishing_rod", properties -> new TideFishingRodItem(2, 36, properties));
    public static final Item CRYSTAL_FISHING_ROD = register("crystal_fishing_rod", properties -> new TideFishingRodItem(2, 80, properties));
    public static final Item DIAMOND_FISHING_ROD = register("diamond_fishing_rod", properties -> new TideFishingRodItem(2, 128, properties));
    public static final Item NETHERITE_FISHING_ROD = register("netherite_fishing_rod", properties -> new TideFishingRodItem(2, 512, properties), new Item.Properties().fireResistant());
    public static final Item ECHO_FISHING_ROD = register("echo_fishing_rod", properties -> new TideFishingRodItem(3, 256, properties));
    public static final Item PRISMARINE_FISHING_ROD = register("prismarine_fishing_rod", properties -> new TideFishingRodItem(3, 256, properties));
    public static final Item SUNFLOWER_FISHING_ROD = register("sunflower_fishing_rod", properties -> new TideFishingRodItem(6, 256, properties));
    public static final Item VILLAGE_FISHING_ROD = register("village_fishing_rod", properties -> new TideFishingRodItem(4, 256, properties));
    public static final Item BLAZING_FISHING_ROD = register("blazing_fishing_rod", properties -> new TideFishingRodItem(4, 256, properties));
    public static final Item HONEYCOMB_FISHING_ROD = register("honeycomb_fishing_rod", properties -> new TideFishingRodItem(5, 256, properties));
    public static final Item MIDAS_FISHING_ROD = register("midas_fishing_rod", properties -> new TideFishingRodItem(3, 256, properties));
    public static final Item KIDS_FISHING_POLE = register("kids_fishing_pole", properties -> new TideFishingRodItem(1, 40, properties));
    public static final Item BAMBOO_FISHING_ROD = register("bamboo_fishing_rod", properties -> new TideFishingRodItem(1, 64, properties));
    public static final Item BONE_FISHING_ROD = register("bone_fishing_rod", properties -> new TideFishingRodItem(1, 96, properties));
    public static final Item COPPER_FISHING_ROD = register("copper_fishing_rod", properties -> new TideFishingRodItem(1, 80, properties));
    public static final Item AMETHYST_FISHING_ROD = register("amethyst_fishing_rod", properties -> new TideFishingRodItem(2, 128, properties));
    public static final Item MANGROVE_FISHING_ROD = register("mangrove_fishing_rod", properties -> new TideFishingRodItem(1, 80, properties));
    public static final Item CHERRY_FISHING_ROD = register("cherry_fishing_rod", properties -> new TideFishingRodItem(1, 80, properties));
    public static final Item FROST_FISHING_ROD = register("frost_fishing_rod", properties -> new TideFishingRodItem(1, 96, properties));
    public static final Item CACTUS_FISHING_ROD = register("cactus_fishing_rod", properties -> new TideFishingRodItem(1, 64, properties));
    public static final Item CONDUIT_FISHING_ROD = register("conduit_fishing_rod", properties -> new TideFishingRodItem(3, 384, properties));
    public static final Item CLOCKWORK_FISHING_ROD = register("clockwork_fishing_rod", properties -> new TideFishingRodItem(2, 256, properties));
    public static final Item WAYFINDER_FISHING_ROD = register("wayfinder_fishing_rod", properties -> new TideFishingRodItem(3, 768, properties));

    public static final Item BAIT = register("bait", Item::new);
    public static final Item LUCKY_BAIT = register("lucky_bait", Item::new);
    public static final Item MAGNETIC_BAIT = register("magnetic_bait", Item::new);
    public static final Item INCANDESCENT_BAIT = register("incandescent_bait", Item::new);
    public static final Item ABYSS_BAIT = register("abyss_bait", Item::new);

    public static final Item RED_BOBBER = register("red_bobber", FishingBobberItem::new);
    public static final Item ORANGE_BOBBER = register("orange_bobber", FishingBobberItem::new);
    public static final Item YELLOW_BOBBER = register("yellow_bobber", FishingBobberItem::new);
    public static final Item LIME_BOBBER = register("lime_bobber", FishingBobberItem::new);
    public static final Item GREEN_BOBBER = register("green_bobber", FishingBobberItem::new);
    public static final Item CYAN_BOBBER = register("cyan_bobber", FishingBobberItem::new);
    public static final Item LIGHT_BLUE_BOBBER = register("light_blue_bobber", FishingBobberItem::new);
    public static final Item BLUE_BOBBER = register("blue_bobber", FishingBobberItem::new);
    public static final Item PURPLE_BOBBER = register("purple_bobber", FishingBobberItem::new);
    public static final Item MAGENTA_BOBBER = register("magenta_bobber", FishingBobberItem::new);
    public static final Item PINK_BOBBER = register("pink_bobber", FishingBobberItem::new);
    public static final Item WHITE_BOBBER = register("white_bobber", FishingBobberItem::new);
    public static final Item LIGHT_GRAY_BOBBER = register("light_gray_bobber", FishingBobberItem::new);
    public static final Item GRAY_BOBBER = register("gray_bobber", FishingBobberItem::new);
    public static final Item BLACK_BOBBER = register("black_bobber", FishingBobberItem::new);
    public static final Item BROWN_BOBBER = register("brown_bobber", FishingBobberItem::new);
    public static final Item APPLE_BOBBER = register("apple_bobber", FishingBobberItem::new, new Item.Properties().food(Foods.APPLE));
    public static final Item GOLDEN_APPLE_BOBBER = register("golden_apple_bobber", FishingBobberItem::new, new Item.Properties().food(Foods.GOLDEN_APPLE, net.minecraft.world.item.component.Consumables.GOLDEN_APPLE));
    public static final Item ENCHANTED_GOLDEN_APPLE_BOBBER = register("enchanted_golden_apple_bobber", EnchantedFishingBobberItem::new, new Item.Properties()
            .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
            .food(Foods.ENCHANTED_GOLDEN_APPLE, net.minecraft.world.item.component.Consumables.ENCHANTED_GOLDEN_APPLE));
    public static final Item IRON_BOBBER = register("iron_bobber", FishingBobberItem::new);
    public static final Item GOLDEN_BOBBER = register("golden_bobber", FishingBobberItem::new);
    public static final Item DIAMOND_BOBBER = register("diamond_bobber", FishingBobberItem::new);
    public static final Item NETHERITE_BOBBER = register("netherite_bobber", FishingBobberItem::new, new Item.Properties().fireResistant());
    public static final Item AMETHYST_BOBBER = register("amethyst_bobber", FishingBobberItem::new);
    public static final Item ECHO_BOBBER = register("echo_bobber", FishingBobberItem::new);
    public static final Item CHORUS_BOBBER = register("chorus_bobber", FishingBobberItem::new);
    public static final Item FEATHER_BOBBER = register("feather_bobber", FishingBobberItem::new);
    public static final Item LICHEN_BOBBER = register("lichen_bobber", FishingBobberItem::new);
    public static final Item NAUTILUS_BOBBER = register("nautilus_bobber", FishingBobberItem::new);
    public static final Item PEARL_BOBBER = register("pearl_bobber", FishingBobberItem::new);
    public static final Item HEART_BOBBER = register("heart_bobber", FishingBobberItem::new);
    public static final Item GRASSY_BOBBER = register("grassy_bobber", FishingBobberItem::new);
    public static final Item DUCK_BOBBER = register("duck_bobber", FishingBobberItem::new);
    public static final Item SLIME_BOBBER = register("slime_bobber", FishingBobberItem::new);
    public static final Item CHERRY_BOBBER = register("cherry_bobber", FishingBobberItem::new);
    public static final Item GLOW_BERRY_BOBBER = register("glow_berry_bobber", FishingBobberItem::new);
    public static final Item PUMPKIN_BOBBER = register("pumpkin_bobber", FishingBobberItem::new);
    public static final Item LILY_PAD_BOBBER = register("lily_pad_bobber", FishingBobberItem::new);
    public static final Item COPPER_BOBBER = register("copper_bobber", FishingBobberItem::new);
    public static final Item SCULK_BOBBER = register("sculk_bobber", FishingBobberItem::new);
    public static final Item PRISMARINE_BOBBER = register("prismarine_bobber", FishingBobberItem::new);
    public static final Item SNOWBALL_BOBBER = register("snowball_bobber", FishingBobberItem::new);
    public static final Item CACTUS_BOBBER = register("cactus_bobber", FishingBobberItem::new);
    public static final Item TURTLE_BOBBER = register("turtle_bobber", FishingBobberItem::new);
    public static final Item MUSHROOM_BOBBER = register("mushroom_bobber", FishingBobberItem::new);
    public static final Item HONEY_BOBBER = register("honey_bobber", FishingBobberItem::new);
    public static final Item BELL_BOBBER = register("bell_bobber", FishingBobberItem::new);
    public static final Item ENDER_EYE_BOBBER = register("ender_eye_bobber", FishingBobberItem::new);
    public static final Item SPINNER_LURE = register("spinner_lure", com.li64.tide.registries.items.FishingLureItem::new);
    public static final Item GOLDEN_SPOON_LURE = register("golden_spoon_lure", com.li64.tide.registries.items.FishingLureItem::new);
    public static final Item GLOW_LURE = register("glow_lure", com.li64.tide.registries.items.FishingLureItem::new);
    public static final Item ECHO_LURE = register("echo_lure", com.li64.tide.registries.items.FishingLureItem::new);
    public static final Item FLY_LURE = register("fly_lure", com.li64.tide.registries.items.FishingLureItem::new);
    public static final Item JIG_LURE = register("jig_lure", com.li64.tide.registries.items.FishingLureItem::new);
    public static final Item POPPER_LURE = register("popper_lure", com.li64.tide.registries.items.FishingLureItem::new);
    public static final Item MAGNET_LURE = register("magnet_lure", com.li64.tide.registries.items.FishingLureItem::new);
    public static final Item MAGMA_LURE = register("magma_lure", com.li64.tide.registries.items.FishingLureItem::new);
    public static final Item CHORUS_LURE = register("chorus_lure", com.li64.tide.registries.items.FishingLureItem::new);
    public static final Item STORM_LURE = register("storm_lure", com.li64.tide.registries.items.FishingLureItem::new);
    public static final Item INK_LURE = register("ink_lure", com.li64.tide.registries.items.FishingLureItem::new);
    public static final Item FROG_LURE = register("frog_lure", com.li64.tide.registries.items.FishingLureItem::new);
    public static final Item SQUID_LURE = register("squid_lure", com.li64.tide.registries.items.FishingLureItem::new);
    public static final Item ICE_JIG_LURE = register("ice_jig_lure", com.li64.tide.registries.items.FishingLureItem::new);
    public static final Item CUT_BAIT = register("cut_bait", Item::new);
    public static final Item DOUGH_BALL = register("dough_ball", Item::new);
    public static final Item GLOW_WORMS = register("glow_worms", Item::new);
    public static final Item CHUM = register("chum", Item::new);
    public static final Item GOLDEN_BAIT = register("golden_bait", Item::new);
    public static final Item CAVE_GRUBS = register("cave_grubs", Item::new);
    public static final Item MOON_BAIT = register("moon_bait", Item::new);
    public static final Item JUNGLE_BAIT = register("jungle_bait", Item::new);
    public static final Item SWAMP_BAIT = register("swamp_bait", Item::new);
    public static final Item DESERT_BAIT = register("desert_bait", Item::new);
    public static final Item FROST_BAIT = register("frost_bait", Item::new);
    public static final Item SPORE_BAIT = register("spore_bait", Item::new);
    public static final Item BLOSSOM_BAIT = register("blossom_bait", Item::new);
    public static final Item COASTAL_BAIT = register("coastal_bait", Item::new);
    public static final Item DRIPSTONE_BAIT = register("dripstone_bait", Item::new);
    public static final Item SCULK_BAIT = register("sculk_bait", Item::new);
    public static final Item BRINE_SHRIMP = register("brine_shrimp", Item::new);

    public static final Item FISHING_HOOK = register("fishing_hook", FishingHookItem::new);
    public static final Item FIERY_HOOK = register("fiery_hook", properties -> new FishingHookItem(properties, "item.tide.fiery_hook.desc"));
    public static final Item PERMAFROST_HOOK = register("permafrost_hook", properties -> new FishingHookItem(properties, "item.tide.permafrost_hook.desc"));
    public static final Item TWILIGHT_HOOK = register("twilight_hook", properties -> new FishingHookItem(properties, "item.tide.twilight_hook.desc"));
    public static final Item LAVAPROOF_HOOK = register("lavaproof_hook", properties -> new FishingHookItem(properties, "item.tide.lavaproof_hook.desc"));
    public static final Item VOID_HOOK = register("void_hook", properties -> new FishingHookItem(properties, "item.tide.void_hook.desc"));
    public static final Item WOODEN_HOOK = register("wooden_hook", FishingHookItem::new);
    public static final Item STONE_HOOK = register("stone_hook", FishingHookItem::new);
    public static final Item COPPER_HOOK = register("copper_hook", FishingHookItem::new);
    public static final Item GOLDEN_HOOK = register("golden_hook", FishingHookItem::new);
    public static final Item DIAMOND_HOOK = register("diamond_hook", FishingHookItem::new);
    public static final Item EMERALD_HOOK = register("emerald_hook", FishingHookItem::new);
    public static final Item AMETHYST_HOOK = register("amethyst_hook", FishingHookItem::new);
    public static final Item NETHERITE_HOOK = register("netherite_hook", FishingHookItem::new);
    public static final Item BONE_HOOK = register("bone_hook", FishingHookItem::new);
    public static final Item PRISMARINE_HOOK = register("prismarine_hook", FishingHookItem::new);
    public static final Item GLOW_HOOK = register("glow_hook", FishingHookItem::new);
    public static final Item FEATHERED_HOOK = register("feathered_hook", FishingHookItem::new);
    public static final Item BARBED_HOOK = register("barbed_hook", FishingHookItem::new);
    public static final Item TREBLE_HOOK = register("treble_hook", FishingHookItem::new);
    public static final Item CIRCLE_HOOK = register("circle_hook", FishingHookItem::new);
    public static final Item FISH_BONE_HOOK = register("fish_bone_hook", FishingHookItem::new);
    public static final Item LUCKY_HOOK = register("lucky_hook", FishingHookItem::new);

    public static final Item TACKLE_BOX = register("tackle_box", com.li64.tide.registries.items.TackleBoxItem::new);
    public static final Item FISHING_LINE = register("fishing_line", FishingLineItem::new);
    public static final Item COPPER_LINE = register("copper_line", FishingLineItem::new);
    public static final Item IRON_LINE = register("iron_line", FishingLineItem::new);
    public static final Item GOLDEN_LINE = register("golden_line", FishingLineItem::new);
    public static final Item DIAMOND_LINE = register("diamond_line", FishingLineItem::new);
    public static final Item SLIME_LINE = register("slime_line", FishingLineItem::new);
    public static final Item COAL_LINE = register("coal_line", FishingLineItem::new);
    public static final Item KELP_LINE = register("kelp_line", FishingLineItem::new);
    public static final Item HONEY_LINE = register("honey_line", FishingLineItem::new);
    public static final Item SILK_LINE = register("silk_line", FishingLineItem::new);
    public static final Item PHANTOM_LINE = register("phantom_line", FishingLineItem::new);
    public static final Item BRAIDED_LINE = register("braided_line", FishingLineItem::new);
    public static final Item VINE_LINE = register("vine_line", FishingLineItem::new);
    public static final Item SPIDER_LINE = register("spider_line", FishingLineItem::new);
    public static final Item REDSTONE_LINE = register("redstone_line", FishingLineItem::new);
    public static final Item WHITE_LINE = register("white_line", FishingLineItem::new);
    public static final Item ORANGE_LINE = register("orange_line", FishingLineItem::new);
    public static final Item MAGENTA_LINE = register("magenta_line", FishingLineItem::new);
    public static final Item LIGHT_BLUE_LINE = register("light_blue_line", FishingLineItem::new);
    public static final Item YELLOW_LINE = register("yellow_line", FishingLineItem::new);
    public static final Item LIME_LINE = register("lime_line", FishingLineItem::new);
    public static final Item PINK_LINE = register("pink_line", FishingLineItem::new);
    public static final Item GRAY_LINE = register("gray_line", FishingLineItem::new);
    public static final Item LIGHT_GRAY_LINE = register("light_gray_line", FishingLineItem::new);
    public static final Item CYAN_LINE = register("cyan_line", FishingLineItem::new);
    public static final Item PURPLE_LINE = register("purple_line", FishingLineItem::new);
    public static final Item BLUE_LINE = register("blue_line", FishingLineItem::new);
    public static final Item BROWN_LINE = register("brown_line", FishingLineItem::new);
    public static final Item GREEN_LINE = register("green_line", FishingLineItem::new);
    public static final Item RED_LINE = register("red_line", FishingLineItem::new);
    public static final Item BLACK_LINE = register("black_line", FishingLineItem::new);

    public static final Item FISHING_JOURNAL = register("fishing_journal", FishingJournalItem::new);
    public static final Item FISHY_NOTE = register("fishy_note", FishyNoteItem::new, new Item.Properties().stacksTo(1));
    public static final Item FISH_SATCHEL = register("fish_satchel", FishSatchelItem::new, new Item.Properties()
            .component(DataComponents.BUNDLE_CONTENTS, new BundleContents(List.of()))
            .stacksTo(1));

    public static final Item POCKET_WATCH = register("pocket_watch", PocketWatchItem::new);
    public static final Item LUNAR_CALENDAR = register("lunar_calendar", properties -> new LunarCalendarItem(properties.useBlockDescriptionPrefix()));
    public static final Item CLIMATE_GAUGE = register("climate_gauge", ClimateGaugeItem::new);
    public static final Item DEPTH_METER = register("depth_meter", DepthMeterItem::new);
    public static final Item WEATHER_RADIO = register("weather_radio", properties -> new WeatherRadioItem(properties.useBlockDescriptionPrefix()));
    public static final Item FISH_FINDER = register("fish_finder", FishFinderItem::new, new Item.Properties().rarity(Rarity.RARE));

    public static final Item ENCHANTED_POCKET_WATCH = register("enchanted_pocket_watch", EnchantedPocketWatchItem::new, new Item.Properties()
            .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
            .rarity(Rarity.EPIC));
    public static final Item STARLIGHT_BOW = register("starlight_bow", StarlightBowItem::new,
            new Item.Properties().rarity(Rarity.EPIC).durability(750));
    public static final Item DRAGONFIN_BOOTS = register("dragonfin_boots", DragonfinBootsItem::new, new Item.Properties()
            .humanoidArmor(TideArmorMaterials.DRAGONFIN, net.minecraft.world.item.equipment.ArmorType.BOOTS)
            .rarity(Rarity.EPIC).fireResistant());

    // fishing gear (see RodEffects for what each piece does)
    public static final Item ANGLER_HAT = register("angler_hat", Item::new, new Item.Properties()
            .humanoidArmor(TideArmorMaterials.ANGLER, net.minecraft.world.item.equipment.ArmorType.HELMET)
            .component(DataComponents.DYED_COLOR, new net.minecraft.world.item.component.DyedItemColor(0xC8B48A)));
    public static final Item FISHING_VEST = register("fishing_vest", Item::new, new Item.Properties()
            .humanoidArmor(TideArmorMaterials.ANGLER, net.minecraft.world.item.equipment.ArmorType.CHESTPLATE)
            .component(DataComponents.DYED_COLOR, new net.minecraft.world.item.component.DyedItemColor(0x6B7A3A)));
    public static final Item WADERS = register("waders", Item::new, new Item.Properties()
            .humanoidArmor(TideArmorMaterials.ANGLER, net.minecraft.world.item.equipment.ArmorType.LEGGINGS)
            .attributes(TideArmorMaterials.ANGLER.createAttributes(net.minecraft.world.item.equipment.ArmorType.LEGGINGS).withModifierAdded(
                    net.minecraft.world.entity.ai.attributes.Attributes.WATER_MOVEMENT_EFFICIENCY,
                    new net.minecraft.world.entity.ai.attributes.AttributeModifier(Tide.resource("waders"), 0.33,
                            net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE),
                    net.minecraft.world.entity.EquipmentSlotGroup.LEGS))
            .component(DataComponents.DYED_COLOR, new net.minecraft.world.item.component.DyedItemColor(0x4F5A3C)));
    public static final Item WADING_BOOTS = register("wading_boots", Item::new, new Item.Properties()
            .humanoidArmor(TideArmorMaterials.ANGLER, net.minecraft.world.item.equipment.ArmorType.BOOTS)
            .component(DataComponents.DYED_COLOR, new net.minecraft.world.item.component.DyedItemColor(0x5C4033)));

    public static final Item ANGLING_TABLE = register("angling_table", properties -> new BlockItem(TideBlocks.ANGLING_TABLE, properties.useBlockDescriptionPrefix()));
    public static final Item FISH_DISPLAY = register("fish_display", properties -> new BlockItem(TideBlocks.FISH_DISPLAY, properties.useBlockDescriptionPrefix()));

    public static final Item WOODEN_CRATE = register("wooden_crate", properties -> new BlockItem(TideBlocks.WOODEN_CRATE, properties.useBlockDescriptionPrefix()));
    public static final Item OBSIDIAN_CRATE = register("obsidian_crate", properties -> new BlockItem(TideBlocks.OBSIDIAN_CRATE, properties.useBlockDescriptionPrefix()));
    public static final Item PURPUR_CRATE = register("purpur_crate", properties -> new BlockItem(TideBlocks.PURPUR_CRATE, properties.useBlockDescriptionPrefix()));

    public static final Item FISH_BONE = register("fish_bone", Item::new, new Item.Properties());
    public static final Item OBSIDIAN_FRAGMENT = register("obsidian_fragment", Item::new, new Item.Properties().fireResistant());

    public static final Item JELLY_TORCH = register("jelly_torch",  properties -> new JellyTorchBlockItem(TideBlocks.JELLY_TORCH, TideBlocks.JELLY_WALL_TORCH, Direction.DOWN, properties.useBlockDescriptionPrefix()), new Item.Properties());

    public static final Item COOKED_FISH = register("cooked_fish", Item::new, new Item.Properties().food(TideFoods.COOKED_FISH.toProperties(), TideFoods.COOKED_FISH.toConsumable()));
    public static final Item SMALL_COOKED_FISH = register("small_cooked_fish", Item::new, new Item.Properties().food(TideFoods.TINY_COOKED_FISH.toProperties(), TideFoods.TINY_COOKED_FISH.toConsumable()));
    public static final Item LARGE_COOKED_FISH = register("large_cooked_fish", Item::new, new Item.Properties().food(TideFoods.BIG_COOKED_FISH.toProperties(), TideFoods.BIG_COOKED_FISH.toConsumable()));
    public static final Item GRILLED_TUNA = register("grilled_tuna", Item::new, new Item.Properties().food(TideFoods.GRILLED_TUNA.toProperties(), TideFoods.GRILLED_TUNA.toConsumable()));

    public static final Item FISH_SLICE = register("fish_slice", Item::new, new Item.Properties().food(TideFoods.FISH_SLICE.toProperties(), TideFoods.FISH_SLICE.toConsumable()));
    public static final Item COOKED_FISH_SLICE = register("cooked_fish_slice", Item::new, new Item.Properties().food(TideFoods.COOKED_FISH_SLICE.toProperties(), TideFoods.COOKED_FISH_SLICE.toConsumable()));

    public static Item register(String name, Function<Item.Properties, Item> factory) {
        return register(name, factory, new Item.Properties());
    }

    public static Item register(String name, Function<Item.Properties, Item> factory, Item.Properties properties) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Tide.resource(name));
        Item item = factory.apply(properties.setId(key));
        return register(key, item);
    }

    static Item register(ResourceKey<Item> key, Item item) {
        ITEMS.put(key, item);
        if (item instanceof BlockItem blockItem) blockItem.registerBlocks(Item.BY_BLOCK, item);
        return Tide.PLATFORM.register(BuiltInRegistries.ITEM, key, item);
    }

    public static void init() {
        TideFish.init();
    }

    public static ArrayList<ItemStack> getCreativeModeItemList() {
        if (DISPLAY_ITEMS.isEmpty()) {
            addDisplayItems(
                    FISHING_JOURNAL, FISHY_NOTE, FISH_SATCHEL,

                    Items.FISHING_ROD,
                    STONE_FISHING_ROD, IRON_FISHING_ROD, GOLDEN_FISHING_ROD,
                    CRYSTAL_FISHING_ROD, DIAMOND_FISHING_ROD, NETHERITE_FISHING_ROD,
                    ECHO_FISHING_ROD, PRISMARINE_FISHING_ROD, SUNFLOWER_FISHING_ROD,
                    VILLAGE_FISHING_ROD, BLAZING_FISHING_ROD, HONEYCOMB_FISHING_ROD,
                    MIDAS_FISHING_ROD,
                    KIDS_FISHING_POLE, BAMBOO_FISHING_ROD, BONE_FISHING_ROD, COPPER_FISHING_ROD, AMETHYST_FISHING_ROD, MANGROVE_FISHING_ROD, CHERRY_FISHING_ROD, FROST_FISHING_ROD, CACTUS_FISHING_ROD, CONDUIT_FISHING_ROD, CLOCKWORK_FISHING_ROD, WAYFINDER_FISHING_ROD,

                    BAIT, LUCKY_BAIT, MAGNETIC_BAIT, INCANDESCENT_BAIT, ABYSS_BAIT, CUT_BAIT, DOUGH_BALL, GLOW_WORMS, CHUM, GOLDEN_BAIT, CAVE_GRUBS, MOON_BAIT, JUNGLE_BAIT, SWAMP_BAIT, DESERT_BAIT, FROST_BAIT, SPORE_BAIT, BLOSSOM_BAIT, COASTAL_BAIT, DRIPSTONE_BAIT, SCULK_BAIT, BRINE_SHRIMP,

                    POCKET_WATCH, LUNAR_CALENDAR, CLIMATE_GAUGE,
                    DEPTH_METER, WEATHER_RADIO, FISH_FINDER,

                    ENCHANTED_POCKET_WATCH, STARLIGHT_BOW, DRAGONFIN_BOOTS,
                    ANGLER_HAT, FISHING_VEST, WADERS, WADING_BOOTS,

                    ANGLING_TABLE, FISH_DISPLAY,
                    WOODEN_CRATE, OBSIDIAN_CRATE, PURPUR_CRATE,

                    COOKED_FISH, GRILLED_TUNA, FISH_SLICE, COOKED_FISH_SLICE,

                    FISH_BONE, OBSIDIAN_FRAGMENT,

                    JELLY_TORCH,

                    TACKLE_BOX,
                    FISHING_HOOK, FIERY_HOOK, PERMAFROST_HOOK,
                    TWILIGHT_HOOK, LAVAPROOF_HOOK, VOID_HOOK,
                    WOODEN_HOOK, STONE_HOOK, COPPER_HOOK, GOLDEN_HOOK, DIAMOND_HOOK, EMERALD_HOOK, AMETHYST_HOOK, NETHERITE_HOOK, BONE_HOOK, PRISMARINE_HOOK, GLOW_HOOK, FEATHERED_HOOK, BARBED_HOOK, TREBLE_HOOK, CIRCLE_HOOK, FISH_BONE_HOOK, LUCKY_HOOK,

                    FISHING_LINE, COPPER_LINE, IRON_LINE, GOLDEN_LINE, DIAMOND_LINE, SLIME_LINE, COAL_LINE, KELP_LINE, HONEY_LINE, SILK_LINE, PHANTOM_LINE, BRAIDED_LINE, VINE_LINE, SPIDER_LINE, REDSTONE_LINE, WHITE_LINE, ORANGE_LINE, MAGENTA_LINE, LIGHT_BLUE_LINE, YELLOW_LINE, LIME_LINE, PINK_LINE, GRAY_LINE, LIGHT_GRAY_LINE, CYAN_LINE, PURPLE_LINE, BLUE_LINE, BROWN_LINE, GREEN_LINE, RED_LINE, BLACK_LINE,

                    RED_BOBBER, ORANGE_BOBBER,
                    YELLOW_BOBBER, LIME_BOBBER,
                    GREEN_BOBBER, CYAN_BOBBER,
                    LIGHT_BLUE_BOBBER, BLUE_BOBBER,
                    PURPLE_BOBBER, MAGENTA_BOBBER,
                    PINK_BOBBER, WHITE_BOBBER,
                    LIGHT_GRAY_BOBBER, GRAY_BOBBER,
                    BLACK_BOBBER, BROWN_BOBBER,
                    APPLE_BOBBER, GOLDEN_APPLE_BOBBER,
                    ENCHANTED_GOLDEN_APPLE_BOBBER, IRON_BOBBER,
                    GOLDEN_BOBBER, DIAMOND_BOBBER,
                    NETHERITE_BOBBER, AMETHYST_BOBBER,
                    ECHO_BOBBER, CHORUS_BOBBER,
                    FEATHER_BOBBER, LICHEN_BOBBER,
                    NAUTILUS_BOBBER, PEARL_BOBBER,
                    HEART_BOBBER, GRASSY_BOBBER,
                    DUCK_BOBBER, SLIME_BOBBER, CHERRY_BOBBER, GLOW_BERRY_BOBBER, PUMPKIN_BOBBER, LILY_PAD_BOBBER, COPPER_BOBBER, SCULK_BOBBER, PRISMARINE_BOBBER, SNOWBALL_BOBBER, CACTUS_BOBBER, TURTLE_BOBBER, MUSHROOM_BOBBER, HONEY_BOBBER, BELL_BOBBER, ENDER_EYE_BOBBER, SPINNER_LURE, GOLDEN_SPOON_LURE, GLOW_LURE, ECHO_LURE, FLY_LURE, JIG_LURE, POPPER_LURE, MAGNET_LURE, MAGMA_LURE, CHORUS_LURE, STORM_LURE, INK_LURE, FROG_LURE, SQUID_LURE, ICE_JIG_LURE
            );

            TideFish.ORDERED.forEach(item -> {
                ItemStack stack = new ItemStack(item);
                if (Tide.SERVER_CONFIG.items.fishItemSizes == TideServerConfig.Items.SizeMode.ALWAYS && FishData.get(stack).map(FishData::size).isPresent())
                    FishData.get(stack).ifPresent(data -> TideItemData.FISH_LENGTH.set(stack, data.getAverageLength()));
                DISPLAY_ITEMS.add(stack);

                Identifier itemKey = BuiltInRegistries.ITEM.getKey(item);

                var bucketItem = BuiltInRegistries.ITEM.getOptional(itemKey.withSuffix("_bucket"));
                bucketItem.ifPresent(value -> DISPLAY_ITEMS.add(new ItemStack(value)));

                var spawnEggItem = BuiltInRegistries.ITEM.getOptional(itemKey.withSuffix("_spawn_egg"));
                spawnEggItem.ifPresent(value -> DISPLAY_ITEMS.add(new ItemStack(value)));
            });
        }
        return DISPLAY_ITEMS;
    }

    private static void addDisplayItems(Item... items) {
        DISPLAY_ITEMS.addAll(Arrays.stream(items).map(ItemStack::new).toList());
    }

    public static CreativeModeTab.Builder getCreativeTab(CreativeModeTab.Builder builder) {
        return builder.title(Component.translatable("itemGroup.tide.main"))
                .icon(() -> new ItemStack(TideFish.ANGELFISH))
                .displayItems(TideItems::getAllDisplayItems);
    }

    public static void getAllDisplayItems(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
        TideItems.getCreativeModeItemList().forEach(output::accept);
    }
}
