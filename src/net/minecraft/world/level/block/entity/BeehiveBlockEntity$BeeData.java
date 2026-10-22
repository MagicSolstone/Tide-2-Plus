package net.minecraft.world.level.block.entity;

class BeehiveBlockEntity$BeeData {
   private final BeehiveBlockEntity$Occupant occupant;
   private int ticksInHive;

   private BeehiveBlockEntity$BeeData(final BeehiveBlockEntity$Occupant occupant) {
      this.occupant = occupant;
      this.ticksInHive = occupant.ticksInHive();
   }

   public boolean tick() {
      return this.ticksInHive++ > this.occupant.minTicksInHive;
   }

   public BeehiveBlockEntity$Occupant toOccupant() {
      return new BeehiveBlockEntity$Occupant(this.occupant.entityData, this.ticksInHive, this.occupant.minTicksInHive);
   }

   public boolean hasNectar() {
      return this.occupant.entityData.getUnsafe().getBooleanOr("HasNectar", false);
   }
}
