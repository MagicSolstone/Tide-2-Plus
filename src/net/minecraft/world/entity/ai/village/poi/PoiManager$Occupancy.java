package net.minecraft.world.entity.ai.village.poi;

import java.util.function.Predicate;

public enum PoiManager$Occupancy {
   HAS_SPACE(PoiRecord::hasSpace),
   IS_OCCUPIED(PoiRecord::isOccupied),
   ANY(poiRecord -> true);

   private final Predicate<? super PoiRecord> test;

   private PoiManager$Occupancy(final Predicate<? super PoiRecord> test) {
      this.test = test;
   }

   public Predicate<? super PoiRecord> getTest() {
      return this.test;
   }
}
