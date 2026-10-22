package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;

class OceanMonumentPieces$FitDoubleYZRoom implements OceanMonumentPieces$MonumentRoomFitter {
   private OceanMonumentPieces$FitDoubleYZRoom() {
   }

   @Override
   public boolean fits(final OceanMonumentPieces$RoomDefinition definition) {
      if (definition.hasOpening[Direction.NORTH.get3DDataValue()]
         && !definition.connections[Direction.NORTH.get3DDataValue()].claimed
         && definition.hasOpening[Direction.UP.get3DDataValue()]
         && !definition.connections[Direction.UP.get3DDataValue()].claimed) {
         OceanMonumentPieces$RoomDefinition north = definition.connections[Direction.NORTH.get3DDataValue()];
         return north.hasOpening[Direction.UP.get3DDataValue()] && !north.connections[Direction.UP.get3DDataValue()].claimed;
      } else {
         return false;
      }
   }

   @Override
   public OceanMonumentPieces$OceanMonumentPiece create(
      final Direction orientation, final OceanMonumentPieces$RoomDefinition definition, final RandomSource random
   ) {
      definition.claimed = true;
      definition.connections[Direction.NORTH.get3DDataValue()].claimed = true;
      definition.connections[Direction.UP.get3DDataValue()].claimed = true;
      definition.connections[Direction.NORTH.get3DDataValue()].connections[Direction.UP.get3DDataValue()].claimed = true;
      return new OceanMonumentPieces$OceanMonumentDoubleYZRoom(orientation, definition);
   }
}
