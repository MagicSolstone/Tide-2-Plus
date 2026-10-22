package net.minecraft.world.level.storage.loot.functions;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;

public class ExplorationMapFunction$Builder extends LootItemConditionalFunction$Builder<ExplorationMapFunction$Builder> {
   private final HolderSet<Structure> destination;
   private Holder<MapDecorationType> mapDecoration = ExplorationMapFunction.DEFAULT_DECORATION;
   private byte zoom = 2;
   private int searchRadius = 50;
   private boolean skipKnownStructures = true;

   public ExplorationMapFunction$Builder(final HolderSet<Structure> destination) {
      this.destination = destination;
   }

   protected ExplorationMapFunction$Builder getThis() {
      return this;
   }

   public ExplorationMapFunction$Builder setMapDecoration(final Holder<MapDecorationType> mapDecoration) {
      this.mapDecoration = mapDecoration;
      return this;
   }

   public ExplorationMapFunction$Builder setZoom(final byte zoom) {
      this.zoom = zoom;
      return this;
   }

   public ExplorationMapFunction$Builder setSearchRadius(final int searchRadius) {
      this.searchRadius = searchRadius;
      return this;
   }

   public ExplorationMapFunction$Builder setSkipKnownStructures(final boolean skipKnownStructures) {
      this.skipKnownStructures = skipKnownStructures;
      return this;
   }

   @Override
   public LootItemFunction build() {
      return new ExplorationMapFunction(this.getCondition(), this.destination, this.mapDecoration, this.zoom, this.searchRadius, this.skipKnownStructures);
   }
}
