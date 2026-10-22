package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;

class OceanMonumentPieces$FitDoubleZRoom implements OceanMonumentPieces$MonumentRoomFitter {
   private OceanMonumentPieces$FitDoubleZRoom() {
   }

   @Override
   public boolean fits(final OceanMonumentPieces$RoomDefinition definition) {
      return definition.hasOpening[Direction.NORTH.get3DDataValue()] && !definition.connections[Direction.NORTH.get3DDataValue()].claimed;
   }

   @Override
   public OceanMonumentPieces$OceanMonumentPiece create(
      final Direction orientation, final OceanMonumentPieces$RoomDefinition definition, final RandomSource random
   ) {
      OceanMonumentPieces$RoomDefinition source = definition;
      if (!definition.hasOpening[Direction.NORTH.get3DDataValue()] || definition.connections[Direction.NORTH.get3DDataValue()].claimed) {
         source = definition.connections[Direction.SOUTH.get3DDataValue()];
      }

      source.claimed = true;
      source.connections[Direction.NORTH.get3DDataValue()].claimed = true;
      return new OceanMonumentPieces$OceanMonumentDoubleZRoom(orientation, source);
   }
}
