package net.minecraft.world.item;

import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jspecify.annotations.Nullable;

class Item$TooltipContext$1 implements Item$TooltipContext {
   @Nullable
   @Override
   public HolderLookup$Provider registries() {
      return null;
   }

   @Override
   public float tickRate() {
      return 20.0F;
   }

   @Nullable
   @Override
   public MapItemSavedData mapData(final MapId id) {
      return null;
   }

   @Override
   public boolean isPeaceful() {
      return false;
   }
}
