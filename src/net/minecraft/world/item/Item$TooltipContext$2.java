package net.minecraft.world.item;

import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jspecify.annotations.Nullable;

class Item$TooltipContext$2 implements Item$TooltipContext {
   Item$TooltipContext$2(final Level val$level) {
      this.val$level = val$levelx;
   }

   @Override
   public HolderLookup$Provider registries() {
      return this.val$level.registryAccess();
   }

   @Override
   public float tickRate() {
      return this.val$level.tickRateManager().tickrate();
   }

   @Nullable
   @Override
   public MapItemSavedData mapData(final MapId id) {
      return this.val$level.getMapData(id);
   }

   @Override
   public boolean isPeaceful() {
      return this.val$level.getDifficulty() == Difficulty.PEACEFUL;
   }
}
