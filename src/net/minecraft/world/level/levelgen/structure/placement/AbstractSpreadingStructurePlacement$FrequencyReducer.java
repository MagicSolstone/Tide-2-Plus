package net.minecraft.world.level.levelgen.structure.placement;

@FunctionalInterface
public interface AbstractSpreadingStructurePlacement$FrequencyReducer {
   boolean shouldGenerate(long seed, final int salt, final int sourceX, final int sourceZ, float probability);
}
