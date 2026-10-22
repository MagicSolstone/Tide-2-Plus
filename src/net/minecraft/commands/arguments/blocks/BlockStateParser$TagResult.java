package net.minecraft.commands.arguments.blocks;

import java.util.Map;
import net.minecraft.core.HolderSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;

public record BlockStateParser$TagResult(HolderSet<Block> tag, Map<String, String> vagueProperties, @Nullable CompoundTag nbt) {
}
