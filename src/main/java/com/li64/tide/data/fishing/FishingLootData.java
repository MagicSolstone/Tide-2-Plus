package com.li64.tide.data.fishing;

import com.google.common.collect.ImmutableList;
import com.li64.tide.Tide;
import com.li64.tide.data.ModAssociatedEntry;
import com.li64.tide.data.loot.LootTableRef;
import com.li64.tide.datagen.fabric.providers.SimpleDataOutput;
import com.li64.tide.data.fishing.conditions.FishingCondition;
import com.li64.tide.data.fishing.conditions.types.*;
import com.li64.tide.data.fishing.mediums.FishingMedium;
import com.li64.tide.data.fishing.modifiers.FishingModifier;
import com.li64.tide.data.fishing.selector.FishingEntry;
import com.li64.tide.util.TideUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;

import net.minecraft.core.registries.Registries;

import java.util.ArrayList;
import java.util.List;

public record FishingLootData(ResourceKey<LootTable> lootTable,
                              List<String> associatedMods,
                              List<FishingCondition> conditions,
                              List<FishingModifier> modifiers,
                              double weight, double quality) implements FishingEntry, ModAssociatedEntry {

    public static final Codec<FishingLootData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("loot_table").forGetter(FishingLootData::lootTable),
            Codec.STRING.listOf().optionalFieldOf("associated_mods", List.of()).forGetter(FishingLootData::associatedMods),
            FishingCondition.CODEC.listOf().optionalFieldOf("conditions", List.of()).forGetter(FishingLootData::conditions),
            FishingModifier.CODEC.listOf().optionalFieldOf("modifiers", List.of()).forGetter(FishingLootData::modifiers),
            Codec.DOUBLE.optionalFieldOf("weight", 0.0).forGetter(FishingLootData::weight),
            Codec.DOUBLE.optionalFieldOf("quality", 0.0).forGetter(FishingLootData::quality)
    ).apply(instance, FishingLootData::new));

    @Override
    public double weight(FishingContext context) {
        return this.modifyWeight(weight, quality, modifiers, context);
    }

    @Override
    public boolean shouldKeep(FishingContext context) {
        if (conditions().isEmpty()) Tide.LOG.error("{} has no conditions!", getTestKey().getString());
        return conditions().stream().allMatch(condition -> condition.test(context));
    }

    @Override
    public CatchResult getResult(FishingContext context) {
        MinecraftServer server = context.level().getServer();
        LootTable table = TideUtils.getLootTable(lootTable, server);
        return createResult(table.getRandomItems(context.createFishingLootParams()));
    }

    @Override
    public MutableComponent getTestKey() {
        return Component.translatable("commands.fishing.entries.loot_table").append(" \"").append(Component.literal(
                lootTable.location().toString())).append("\"");
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ResourceKey<LootTable> lootKey;

        private final List<FishingCondition> conditions = new ArrayList<>();
        private final List<FishingModifier> modifiers = new ArrayList<>();

        private double weight = 0.0;
        private double quality = 0.0;

        private Builder() {}

        public Builder lootTable(LootTableRef lootKey) {
            return this.lootTable(lootKey.get());
        }

        public Builder lootTable(ResourceKey<LootTable> lootKey) {
            this.lootKey = lootKey;
            return this;
        }

        public Builder condition(FishingCondition condition) {
            this.conditions.add(condition);
            return this;
        }

        public Builder conditions(List<FishingCondition> conditions) {
            this.conditions.addAll(conditions);
            return this;
        }

        public Builder modifier(FishingModifier modifier) {
            this.modifiers.add(modifier);
            return this;
        }

        public Builder modifiers(List<FishingModifier> modifiers) {
            this.modifiers.addAll(modifiers);
            return this;
        }

        public Builder overworld() {
            return dimensions(List.of(Level.OVERWORLD));
        }

        public Builder nether() {
            return dimensions(List.of(Level.NETHER));
        }

        public Builder water() {
            return medium(FishingMedium.WATER);
        }

        public Builder lava() {
            return medium(FishingMedium.LAVA);
        }

        public Builder surface() {
            return above(40);
        }

        public Builder above(int y) {
            return condition(new AboveCondition(y));
        }

        public Builder below(int y) {
            return condition(new BelowCondition(y));
        }

        public Builder openWater(boolean inOpenWater) {
            return condition(new OpenWaterCondition(inOpenWater));
        }

        public Builder dimensions(List<ResourceKey<Level>> dimensions) {
            return condition(new DimensionsCondition(dimensions));
        }

        public Builder medium(FishingMedium medium) {
            return condition(new FishingMediumCondition(medium));
        }

        public Builder selectionWeight(double weight) {
            this.weight = weight;
            return this;
        }

        public Builder selectionQuality(double quality) {
            this.quality = quality;
            return this;
        }

        public void build(SimpleDataOutput<FishingLootData> output) {
            this.build(this.lootKey.location(), output);
        }

        public void build(String path, SimpleDataOutput<FishingLootData> output) {
            this.build(Tide.resource(path), output);
        }

        public void build(ResourceLocation path, SimpleDataOutput<FishingLootData> output) {
            output.accept(path, build());
        }

        public FishingLootData build() {
            if (lootKey == null) throw new IllegalStateException("Loot table key must be provided");
            return new FishingLootData(lootKey,
                    List.of(),
                    ImmutableList.copyOf(conditions),
                    ImmutableList.copyOf(modifiers),
                    weight, quality
            );
        }
    }
}
