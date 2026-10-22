package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;

class OceanMonumentPieces$FitDoubleYRoom implements OceanMonumentPieces$MonumentRoomFitter {
   private OceanMonumentPieces$FitDoubleYRoom() {
   }

   @Override
   public boolean fits(final OceanMonumentPieces$RoomDefinition definition) {
      return definition.hasOpening[Direction.UP.get3DDataValue()] && !definition.connections[Direction.UP.get3DDataValue()].claimed;
   }

   @Override
   public OceanMonumentPieces$OceanMonumentPiece create(
      final Direction orientation, final OceanMonumentPieces$RoomDefinition definition, final RandomSource random
   ) {
      definition.claimed = true;
      definition.connections[Direction.UP.get3DDataValue()].claimed = true;
      return new OceanMonumentPieces$OceanMonumentDoubleYRoom(orientation, definition);
   }
}
