package net.minecraft.world.level.levelgen.structure.pieces;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public interface StructurePieceType$StructureTemplateType extends StructurePieceType {
   StructurePiece load(final StructureTemplateManager structureTemplateManager, final CompoundTag tag);

   @Override
   default StructurePiece load(final StructurePieceSerializationContext context, final CompoundTag tag) {
      return this.load(context.structureTemplateManager(), tag);
   }
}
