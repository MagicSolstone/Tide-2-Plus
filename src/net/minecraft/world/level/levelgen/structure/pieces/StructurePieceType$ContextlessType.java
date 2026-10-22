package net.minecraft.world.level.levelgen.structure.pieces;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.structure.StructurePiece;

public interface StructurePieceType$ContextlessType extends StructurePieceType {
   StructurePiece load(final CompoundTag tag);

   @Override
   default StructurePiece load(final StructurePieceSerializationContext context, final CompoundTag tag) {
      return this.load(tag);
   }
}
