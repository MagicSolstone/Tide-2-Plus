package net.minecraft.world.entity;

import net.minecraft.world.level.levelgen.Heightmap$Types;

record SpawnPlacements$Data(Heightmap$Types heightMap, SpawnPlacementType placement, SpawnPlacements$SpawnPredicate<?> predicate) {
}
