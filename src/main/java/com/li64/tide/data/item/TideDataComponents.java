package com.li64.tide.data.item;

import com.li64.tide.Tide;
import com.li64.tide.data.rods.BaitContents;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.HashMap;

public class TideDataComponents {
    public static final HashMap<String, DataComponentType<?>> DATA_COMPONENT_TYPES = new HashMap<>();

    public static final DataComponentType<Double> FISH_LENGTH = register(
            "fish_length", DataComponentType.<Double>builder()
                    .persistent(Codec.DOUBLE)
                    .networkSynchronized(ByteBufCodecs.DOUBLE).build());

    public static final DataComponentType<Integer> FISH_QUALITY = register(
            "fish_quality", DataComponentType.<Integer>builder()
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.VAR_INT).build());

    /** Grams. */
    public static final DataComponentType<Double> FISH_WEIGHT = register(
            "fish_weight", DataComponentType.<Double>builder()
                    .persistent(Codec.DOUBLE)
                    .networkSynchronized(ByteBufCodecs.DOUBLE).build());

    public static final DataComponentType<Boolean> IS_BUCKETABLE = register(
            "is_bucketable", DataComponentType.<Boolean>builder()
                    .persistent(Codec.BOOL)
                    .networkSynchronized(ByteBufCodecs.BOOL).build());

    public static final DataComponentType<CompoundTag> FISHING_LINE = register(
            "fishing_line", DataComponentType.<CompoundTag>builder()
                    .persistent(CompoundTag.CODEC)
                    .networkSynchronized(ByteBufCodecs.COMPOUND_TAG).build());

    public static final DataComponentType<CompoundTag> FISHING_BOBBER = register(
            "fishing_bobber", DataComponentType.<CompoundTag>builder()
                    .persistent(CompoundTag.CODEC)
                    .networkSynchronized(ByteBufCodecs.COMPOUND_TAG).build());

    public static final DataComponentType<CompoundTag> FISHING_HOOK = register(
            "fishing_hook", DataComponentType.<CompoundTag>builder()
                    .persistent(CompoundTag.CODEC)
                    .networkSynchronized(ByteBufCodecs.COMPOUND_TAG).build());

    public static final DataComponentType<CompoundTag> FISHING_LURE = register(
            "fishing_lure", DataComponentType.<CompoundTag>builder()
                    .persistent(CompoundTag.CODEC)
                    .networkSynchronized(ByteBufCodecs.COMPOUND_TAG).build());

    public static final DataComponentType<BaitContents> BAIT_CONTENTS = register(
            "bait_contents", DataComponentType.<BaitContents>builder()
                    .persistent(BaitContents.CODEC)
                    .networkSynchronized(BaitContents.STREAM_CODEC)
                    .cacheEncoding().build());

    public static final DataComponentType<ResourceKey<Item>> FISHY_NOTE_VARIANT = register(
            "fishy_note_variant", DataComponentType.<ResourceKey<Item>>builder()
                    .persistent(ResourceKey.codec(Registries.ITEM))
                    .networkSynchronized(ResourceKey.streamCodec(Registries.ITEM)).build());

    public static final DataComponentType<SatchelContents> SATCHEL_CONTENTS = register(
            "satchel_contents", DataComponentType.<SatchelContents>builder()
                    .persistent(SatchelContents.CODEC)
                    .networkSynchronized(SatchelContents.STREAM_CODEC)
                    .cacheEncoding().build());

    public static final DataComponentType<Boolean> FISH_SATCHEL_OPENED = register(
            "fish_satchel_opened", DataComponentType.<Boolean>builder()
                    .persistent(Codec.BOOL)
                    .networkSynchronized(ByteBufCodecs.BOOL).build());

    public static <T> DataComponentType<T> register(String key, DataComponentType<T> component) {
        DATA_COMPONENT_TYPES.put(key, component);
        return Tide.PLATFORM.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Tide.resource(key), component);
    }

    public static void init() {}
}