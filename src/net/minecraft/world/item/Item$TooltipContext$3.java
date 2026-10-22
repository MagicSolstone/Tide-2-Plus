package net.minecraft.world.item;

import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jspecify.annotations.Nullable;

class Item$TooltipContext$3 implements Item$TooltipContext {
   Item$TooltipContext$3(final HolderLookup$Provider val$registries) {
      this.val$registries = val$registriesx;
   }

   @Override
   public HolderLookup$Provider registries() {
      return this.val$registries;
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
