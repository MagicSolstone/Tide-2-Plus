package com.li64.tide.data.fishing.conditions.types;

import com.li64.tide.data.fishing.FishingContext;
import com.li64.tide.data.fishing.conditions.FishingCondition;
import com.li64.tide.data.fishing.conditions.FishingConditionType;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.List;

public class StructuresCondition extends FishingCondition {
    public static final MapCodec<StructuresCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceKey.codec(Registries.STRUCTURE).listOf().fieldOf("structures").forGetter(StructuresCondition::getStructures)
    ).apply(instance, StructuresCondition::new));

    private final List<ResourceKey<Structure>> structures;

    public StructuresCondition(List<ResourceKey<Structure>> structures) {
        this.structures = structures;
    }

    public static StructuresCondition only(ResourceKey<Structure> structure) {
        return inStructures(List.of(structure));
    }

    public static StructuresCondition inStructures(List<ResourceKey<Structure>> structures) {
        return new StructuresCondition(structures);
    }

    public List<ResourceKey<Structure>> getStructures() {
        return structures;
    }

    @Override
    public FishingConditionType<?> type() {
        return FishingConditionType.STRUCTURES;
    }

    @Override
    public boolean test(FishingContext context) {
        return context.level().structureManager().getStructureWithPieceAt(context.blockPos(),
                holder -> structures.stream().anyMatch(holder::is)).isValid();
    }
}
