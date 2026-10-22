package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;

class OceanMonumentPieces$FitSimpleTopRoom implements OceanMonumentPieces$MonumentRoomFitter {
   private OceanMonumentPieces$FitSimpleTopRoom() {
   }

   @Override
   public boolean fits(final OceanMonumentPieces$RoomDefinition definition) {
      return !definition.hasOpening[Direction.WEST.get3DDataValue()]
         && !definition.hasOpening[Direction.EAST.get3DDataValue()]
         && !definition.hasOpening[Direction.NORTH.get3DDataValue()]
         && !definition.hasOpening[Direction.SOUTH.get3DDataValue()]
         && !definition.hasOpening[Direction.UP.get3DDataValue()];
   }

   @Override
   public OceanMonumentPieces$OceanMonumentPiece create(
      final Direction orientation, final OceanMonumentPieces$RoomDefinition definition, final RandomSource random
   ) {
      definition.claimed = true;
      return new OceanMonumentPieces$OceanMonumentSimpleTopRoom(orientation, definition);
   }
}
