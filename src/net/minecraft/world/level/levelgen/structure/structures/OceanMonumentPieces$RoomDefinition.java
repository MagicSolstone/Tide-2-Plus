package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.Direction;

class OceanMonumentPieces$RoomDefinition {
   private final int index;
   private final OceanMonumentPieces$RoomDefinition[] connections = new OceanMonumentPieces$RoomDefinition[6];
   private final boolean[] hasOpening = new boolean[6];
   private boolean claimed;
   private boolean isSource;
   private int scanIndex;

   public OceanMonumentPieces$RoomDefinition(final int roomIndex) {
      this.index = roomIndex;
   }

   public void setConnection(final Direction direction, final OceanMonumentPieces$RoomDefinition definition) {
      this.connections[direction.get3DDataValue()] = definition;
      definition.connections[direction.getOpposite().get3DDataValue()] = this;
   }

   public void updateOpenings() {
      for (int i = 0; i < 6; i++) {
         this.hasOpening[i] = this.connections[i] != null;
      }
   }

   public boolean findSource(final int scanIndex) {
      if (this.isSource) {
         return true;
      } else {
         this.scanIndex = scanIndex;

         for (int i = 0; i < 6; i++) {
            if (this.connections[i] != null && this.hasOpening[i] && this.connections[i].scanIndex != scanIndex && this.connections[i].findSource(scanIndex)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean isSpecial() {
      return this.index >= 75;
   }

   public int countOpenings() {
      int c = 0;

      for (int i = 0; i < 6; i++) {
         if (this.hasOpening[i]) {
            c++;
         }
      }

      return c;
   }
}
