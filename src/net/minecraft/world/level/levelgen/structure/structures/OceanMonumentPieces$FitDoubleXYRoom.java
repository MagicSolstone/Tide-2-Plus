package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;

class OceanMonumentPieces$FitDoubleXYRoom implements OceanMonumentPieces$MonumentRoomFitter {
   private OceanMonumentPieces$FitDoubleXYRoom() {
   }

   @Override
   public boolean fits(final OceanMonumentPieces$RoomDefinition definition) {
      if (definition.hasOpening[Direction.EAST.get3DDataValue()]
         && !definition.connections[Direction.EAST.get3DDataValue()].claimed
         && definition.hasOpening[Direction.UP.get3DDataValue()]
         && !definition.connections[Direction.UP.get3DDataValue()].claimed) {
         OceanMonumentPieces$RoomDefinition east = definition.connections[Direction.EAST.get3DDataValue()];
         return east.hasOpening[Direction.UP.get3DDataValue()] && !east.connections[Direction.UP.get3DDataValue()].claimed;
      } else {
         return false;
      }
   }

   @Override
   public OceanMonumentPieces$OceanMonumentPiece create(
      final Direction orientation, final OceanMonumentPieces$RoomDefinition definition, final RandomSource random
   ) {
      definition.claimed = true;
      definition.connections[Direction.EAST.get3DDataValue()].claimed = true;
      definition.connections[Direction.UP.get3DDataValue()].claimed = true;
      definition.connections[Direction.EAST.get3DDataValue()].connections[Direction.UP.get3DDataValue()].claimed = true;
      return new OceanMonumentPieces$OceanMonumentDoubleXYRoom(orientation, definition);
   }
}
