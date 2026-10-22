package com.li64.tide.data.fishing.conditions.types;

import com.li64.tide.data.fishing.FishingContext;
import com.li64.tide.data.fishing.conditions.FishingCondition;
import com.li64.tide.data.fishing.conditions.FishingConditionType;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.List;

public class BiomeWhitelistCondition extends FishingCondition {
    public static final MapCodec<BiomeWhitelistCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.TAG_OR_ELEMENT_ID.listOf().fieldOf("biomes").forGetter(BiomeWhitelistCondition::toData)
    ).apply(instance, BiomeWhitelistCondition::fromData));

    private final List<TagKey<Biome>> tags;
    private final List<Identifier> biomes;

    private static BiomeWhitelistCondition fromData(List<ExtraCodecs.TagOrElementLocation> data) {
        List<TagKey<Biome>> tags = data.stream()
                .filter(ExtraCodecs.TagOrElementLocation::tag)
                .map(entry -> TagKey.create(Registries.BIOME, entry.id()))
                .toList();
        List<Identifier> biomes = data.stream()
                .filter(entry -> !entry.tag())
                .map(ExtraCodecs.TagOrElementLocation::id)
                .toList();
        return new BiomeWhitelistCondition(tags, biomes);
    }

    public BiomeWhitelistCondition(List<TagKey<Biome>> tags, List<Identifier> biomes) {
        this.tags = tags;
        this.biomes = biomes;
    }

    public static BiomeWhitelistCondition fromTag(TagKey<Biome> tag) {
        return fromTagList(List.of(tag));
    }

    public static BiomeWhitelistCondition fromTagList(List<TagKey<Biome>> tagList) {
        return new BiomeWhitelistCondition(tagList, List.of());
    }

    public List<ExtraCodecs.TagOrElementLocation> toData() {
        ArrayList<ExtraCodecs.TagOrElementLocation> data = new ArrayList<>(tags.size() + biomes.size());
        data.addAll(tags.stream().map(tag -> new ExtraCodecs.TagOrElementLocation(tag.location(), true)).toList());
        data.addAll(biomes.stream().map(id -> new ExtraCodecs.TagOrElementLocation(id, false)).toList());
        return data;
    }

    @Override
    public FishingConditionType<?> type() {
        return FishingConditionType.BIOME_WHITELIST;
    }

    @Override
    public boolean test(FishingContext context) {
        return tags.stream().anyMatch(tag -> context.nearestBiome().is(tag) || context.exactBiome().is(tag))
                || biomes.stream().anyMatch(id -> context.nearestBiome().is(id) || context.exactBiome().is(id));
    }
}
