package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;

class OceanMonumentPieces$FitDoubleXRoom implements OceanMonumentPieces$MonumentRoomFitter {
   private OceanMonumentPieces$FitDoubleXRoom() {
   }

   @Override
   public boolean fits(final OceanMonumentPieces$RoomDefinition definition) {
      return definition.hasOpening[Direction.EAST.get3DDataValue()] && !definition.connections[Direction.EAST.get3DDataValue()].claimed;
   }

   @Override
   public OceanMonumentPieces$OceanMonumentPiece create(
      final Direction orientation, final OceanMonumentPieces$RoomDefinition definition, final RandomSource random
   ) {
      definition.claimed = true;
      definition.connections[Direction.EAST.get3DDataValue()].claimed = true;
      return new OceanMonumentPieces$OceanMonumentDoubleXRoom(orientation, definition);
   }
}
