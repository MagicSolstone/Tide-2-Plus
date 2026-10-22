package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;

class OceanMonumentPieces$FitSimpleRoom implements OceanMonumentPieces$MonumentRoomFitter {
   private OceanMonumentPieces$FitSimpleRoom() {
   }

   @Override
   public boolean fits(final OceanMonumentPieces$RoomDefinition definition) {
      return true;
   }

   @Override
   public OceanMonumentPieces$OceanMonumentPiece create(
      final Direction orientation, final OceanMonumentPieces$RoomDefinition definition, final RandomSource random
   ) {
      definition.claimed = true;
      return new OceanMonumentPieces$OceanMonumentSimpleRoom(orientation, definition, random);
   }
}
