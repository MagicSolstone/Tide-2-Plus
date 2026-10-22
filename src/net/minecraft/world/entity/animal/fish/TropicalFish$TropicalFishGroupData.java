package net.minecraft.world.entity.animal.fish;

class TropicalFish$TropicalFishGroupData extends AbstractSchoolingFish$SchoolSpawnGroupData {
   private final TropicalFish$Variant variant;

   private TropicalFish$TropicalFishGroupData(final TropicalFish leader, final TropicalFish$Variant variant) {
      super(leader);
      this.variant = variant;
   }
}
