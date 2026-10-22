package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;

interface OceanMonumentPieces$MonumentRoomFitter {
   boolean fits(OceanMonumentPieces$RoomDefinition definition);

   OceanMonumentPieces$OceanMonumentPiece create(Direction orientation, OceanMonumentPieces$RoomDefinition definition, RandomSource random);
}
