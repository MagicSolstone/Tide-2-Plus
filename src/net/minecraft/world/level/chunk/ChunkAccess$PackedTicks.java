package net.minecraft.world.level.chunk;

import java.util.List;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.ticks.SavedTick;

public record ChunkAccess$PackedTicks(List<SavedTick<Block>> blocks, List<SavedTick<Fluid>> fluids) {
}
