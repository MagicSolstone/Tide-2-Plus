package net.minecraft.data.worldgen;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public record AbandonedCampStructurePools$AbandonedCampStructure(
   ResourceKey<StructureTemplatePool> campStructureDirectory, ResourceKey<StructureTemplatePool> tentStructureDirectory, String name
) {
}
