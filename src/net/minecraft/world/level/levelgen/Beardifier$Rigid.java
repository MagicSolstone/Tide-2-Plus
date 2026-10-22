package net.minecraft.world.level.levelgen;

import com.google.common.annotations.VisibleForTesting;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;

@VisibleForTesting
public record Beardifier$Rigid(BoundingBox box, TerrainAdjustment terrainAdjustment, int groundLevelDelta) {
}
