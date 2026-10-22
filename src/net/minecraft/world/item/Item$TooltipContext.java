package net.minecraft.world.item;

import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jspecify.annotations.Nullable;

public interface Item$TooltipContext {
   Item$TooltipContext EMPTY = new Item$TooltipContext$1();

   @Nullable
   HolderLookup$Provider registries();

   float tickRate();

   @Nullable
   MapItemSavedData mapData(MapId id);

   boolean isPeaceful();

   static Item$TooltipContext of(@Nullable final Level level) {
      return (Item$TooltipContext)(level == null ? EMPTY : new Item$TooltipContext$2(level));
   }

   static Item$TooltipContext of(final HolderLookup$Provider registries) {
      return new Item$TooltipContext$3(registries);
   }
}
