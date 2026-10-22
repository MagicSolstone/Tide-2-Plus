package net.minecraft.world.level.levelgen.structure.templatesystem;

import java.util.Locale;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public record StructureTemplate$StructureBlockInfo(BlockPos pos, BlockState state, @Nullable CompoundTag nbt) {
   @Override
   public String toString() {
      return String.format(Locale.ROOT, "<StructureBlockInfo | %s | %s | %s>", this.pos, this.state, this.nbt);
   }
}
