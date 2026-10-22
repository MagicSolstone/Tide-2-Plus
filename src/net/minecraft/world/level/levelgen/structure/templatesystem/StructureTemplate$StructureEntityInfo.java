package net.minecraft.world.level.levelgen.structure.templatesystem;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

public class StructureTemplate$StructureEntityInfo {
   public final Vec3 pos;
   public final BlockPos blockPos;
   public final CompoundTag nbt;

   public StructureTemplate$StructureEntityInfo(final Vec3 pos, final BlockPos blockPos, final CompoundTag nbt) {
      this.pos = pos;
      this.blockPos = blockPos;
      this.nbt = nbt;
   }
}
